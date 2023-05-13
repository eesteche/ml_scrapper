package com.colab.app.scrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.colab.app.dto.MeliCategoryDto;
import com.colab.app.model.History;
import com.colab.app.model.Item;
import com.colab.app.model.QueryList;
import com.colab.app.repository.*;
import com.colab.app.service.SearcherService;
import com.colab.app.utils.GeneralUtils;

@Service
public class ScrapperOperation {

	@Resource
	private QueryListRepository qRepo;
	@Resource
	private ItemRepository iRepo;
	@Resource
	private HistoryRepository hRepo;
	@Autowired
	GeneralUtils utils;
	@Autowired
	SearcherService finder;

	Logger logger = LoggerFactory.getLogger(ScrapperOperation.class);

	@Transactional
	@Scheduled(initialDelay = 5000L, fixedRateString = "${scrapperful.Delay}")	
	public void init() {
		logger.info("Starts scrap operation");	
		logger.info("Finding categories...");
		JSONArray categoriesArray = new JSONArray();
		List<MeliCategoryDto> categories = new ArrayList<MeliCategoryDto>();
		try {
			categoriesArray = finder.getCategories();
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}		
		if(categoriesArray.length() > 0) {
			categories = finder.getCategoriesFromJson(categoriesArray);
		}
		int count = 0;
		if(categories.size() > 0) {
			for(MeliCategoryDto dto : categories) {
				count++;
				List<Item> aux = new ArrayList<Item>();
				try {
					aux.addAll(finder.searchItemByCategory(dto.getId().trim()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(aux.size() < 1) {
					continue;
				}				
				logger.info("Founded: " + aux.size() + " on query: " + dto.getName() + " . " + count + " / " + categories.size() + " done.");
				SaveScrapResults(aux, dto.getName());
			}
			logger.info("Categories done.");
		}
		
		List<QueryList> queryList = qRepo.findAll();
		logger.info("QueryList: " + String.join(",", queryList.toString()));
		for (QueryList q : queryList) {
			JSONObject aux = null;
			try {
				aux = finder.searchItem(q.getQ().trim().replace(" " , "%20"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(aux == null) {
				return;
			}
			List<Item> itemList = finder.JsonToItem(aux);
			logger.info("Founded: " + itemList.size() + " on query: " + q.getQ());
			SaveScrapResults(itemList, q);
		}
		
		logger.info("Operation done.");
	}
		
	private void SaveScrapResults(List<Item> lastScrap, QueryList q) {
		logger.info("SaveScrapResults(Querylist): Saving: " + q.getQ() + "... ");
		int cantidadNueva;
						
		Set<Item> itemsActual = iRepo.findByQueryList(q);
		
		/*
		itemsActual.stream().forEach(x -> {
			System.out.println("itemsActual: " + x.getId());
		});
		*/
		
		Set<Item> aux = new HashSet<Item>(itemsActual);
		Set<Item> lastScrapSet = new HashSet<Item>(lastScrap);
		
		aux.retainAll(lastScrap);				
		lastScrapSet.removeAll(aux);
		
		cantidadNueva = lastScrapSet.size();
		if (cantidadNueva == 0) {
			logger.info("SaveScrapResults(QueryList): No new items.");
			return;
		}		
		logger.info("SaveScrapResults(Querylist):" + cantidadNueva + " new Items. Now saving. Items on DB: " + itemsActual.size());
		
		for (Item scrappedItem : lastScrapSet) {			
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			
			//TODO este segundo acceso a la bbdd es necesario (aunque no querido) porque al ser muy grande la bbdd de ML, el scaneo se hace por "sectores" (querylist)
			//Esto genera que un item pueda ser nuevo en una querylist (se cambia el titulo y ahora es un resultado en la busqueda) pero no nuevo en la BBDD (pertenece a otra querylist
			Item itemActual = iRepo.findByIdml(scrappedItem.getIdml());

			double precio_viejo = itemActual == null ? 0.00 : itemActual.getPrice();
			double precio_nuevo = scrappedItem.getPrice();
			scrappedItem.setAlta_fecha(utils.parseTimestamp(date));						

			if (itemActual != null) {
				scrappedItem.setQueryList(itemActual.getQueryList());
				if (!scrappedItem.getQueryList().contains(q)) {
					//scrappedItem.addQueryList(q);
				}
			} else {
				scrappedItem.setQueryList(new HashSet<>());
			}

			if(precio_viejo != precio_nuevo) {
				History h = new History(q.getQ(), scrappedItem, precio_viejo, precio_nuevo, utils.parseTimestamp(date));
				hRepo.save(h);
			}						
			
			Item newItem = null;
			if (itemActual == null) {
				newItem = iRepo.save(scrappedItem);

			} else {
				scrappedItem.setDb_id(itemActual.getDb_id());
				scrappedItem.setModi_fecha(utils.parseTimestamp(date));
				newItem = iRepo.save(scrappedItem);
			}
			if (itemActual == null) {
				q.addItem(newItem);
				qRepo.save(q);
			}			
		}

		logger.info("SaveScrapResults(QueryList): Saved: " + lastScrap.size() + " Items");		
	}
		
	private void SaveScrapResults(List<Item> lastScrap, String category) {
		logger.info("SaveScrapResults(category): Saving: category: " + category + "... ");
		int cantidadNueva;
		
		List<String> idList = lastScrap.stream().map(Item::getIdml).collect(Collectors.toList());
		Set<Item> itemsActual = iRepo.findByIdmlIn(idList);
				
		Set<Item> aux = new HashSet<Item>(itemsActual);
		Set<Item> lastScrapSet = new HashSet<Item>(lastScrap);
		
		aux.retainAll(lastScrap);				
		lastScrapSet.removeAll(aux);
		
		cantidadNueva = lastScrapSet.size();
		if (cantidadNueva == 0) {
			logger.info("SaveScrapResults(category): No new items.");
			return;
		}		
		logger.info("SaveScrapResults(category):" + cantidadNueva + " new Items. Now saving. Items on current db: " + itemsActual.size());		
		
		for (Item scrappedItem : lastScrapSet) {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());								

			//TODO este segundo acceso a la bbdd es necesario (aunque no querido) esta vez ocurre porque al estar guardando muchos items. No podemos confiar en el get inicial a la DB
			//Esto se debe a que al estar en un proceso transaccional, los items guardados al inicio no serán grabados hasta terminar la transaccion. LO que puede generar que items que 
			//compartan la categoria esten en 2 procesos de guardados simultaneos. (uno esperando en cola para ser grabado y otro concurrente)
			Item itemActual = iRepo.findByIdml(scrappedItem.getIdml());
			
			double precio_viejo = itemActual == null ? 0.00 : itemActual.getPrice();
			double precio_nuevo = scrappedItem.getPrice();
			scrappedItem.setAlta_fecha(utils.parseTimestamp(date));						

			if(precio_viejo != precio_nuevo) {
				History h = new History(category, scrappedItem, precio_viejo, precio_nuevo, utils.parseTimestamp(date));
				hRepo.save(h);
			}						
						
			if (itemActual == null) {
				iRepo.save(scrappedItem);

			} else {
				scrappedItem.setDb_id(itemActual.getDb_id());
				scrappedItem.setModi_fecha(utils.parseTimestamp(date));
				iRepo.save(scrappedItem);
			}
	
		}

		logger.info("SaveScrapResults(Category): Saved: " + lastScrap.size() + " Items");		
	}
	
}
