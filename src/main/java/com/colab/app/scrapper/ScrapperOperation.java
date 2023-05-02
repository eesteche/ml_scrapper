package com.colab.app.scrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

	@Scheduled(initialDelay = 5000L, fixedRateString = "${scrapperful.Delay}")
	@Transactional
	public void init() {
		logger.info("Starts scrap operation");		
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
			List<Item> itemList = JsonToItem(aux);
			logger.info("Founded: " + itemList.size() + " on query: " + q.getQ());
			SaveScrapResults(itemList, q);
		}
	}

	private List<Item> JsonToItem(JSONObject json) {
		List<Item> itemList = new ArrayList<Item>();

		json.keySet().forEach(keyStr -> {
			if (keyStr.equals("results")) {

				JSONArray results = json.getJSONArray("results");
				if (results.length() == 0) {
					return;
				}

				for (int i = 0; i < results.length(); i++) {
					JSONObject obj1 = results.getJSONObject(i);
					String id = obj1.getString("id");					
					String title = obj1.getString("title");
					String thumbnail_id = obj1.getString("thumbnail_id");
					String catalog_product_id = obj1.isNull("catalog_product_id") ? "" : obj1.getString("catalog_product_id");
					String permalink = obj1.getString("permalink");
					String category_id = obj1.getString("category_id");
					String domain_id = obj1.getString("domain_id");
					String thumbnail = obj1.getString("thumbnail");
					double price = obj1.isNull("price") ? 0.00 : obj1.getDouble("price");
					double original_price = obj1.isNull("original_price") ? 0.00 : obj1.getDouble("original_price");
					double sale_price = obj1.isNull("sale_price") ? 0.00 : obj1.getDouble("sale_price");
					int sold_quantity = obj1.getInt("sold_quantity");
					int available_quantity = obj1.getInt("available_quantity");

					Item aux = new Item(id, title, thumbnail_id, catalog_product_id, permalink, category_id, domain_id,
							thumbnail, price, original_price, sale_price, sold_quantity, available_quantity);
					itemList.add(aux);
				}
			}

		});
		return itemList;

	}

	private void SaveScrapResults(List<Item> lastScrap, QueryList q) {
		logger.info("Saving: " + q.getQ() + "... ");
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
			logger.info("No new items.");
			return;
		}		
		logger.info(cantidadNueva + " new Items. Now saving.");
		logger.info(itemsActual.size() + "");
		
		for (Item scrappedItem : lastScrap) {
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			logger.info(scrappedItem.getIdml());
			
			//TODO este segundo acceso a la bbdd es necesario (aunque no querido) porque al ser muy grande la bbdd de ML, el scaneo se hace por "sectores" (querylist)
			//Esto genera que un item pueda ser nuevo en una querylist (se cambia el titulo y ahora es un resultado en la busqueda) pero no nuevo en la BBDD (pertenece a otra querylist
			Item itemActual = iRepo.findByIdml(scrappedItem.getIdml());

			double precio_viejo = itemActual == null ? 0.00 : itemActual.getPrice();
			double precio_nuevo = scrappedItem.getPrice();
			scrappedItem.setAlta_fecha(utils.parseTimestamp(date));						

			if (itemActual != null) {
				scrappedItem.setQueryList(itemActual.getQueryList());
				if (!scrappedItem.getQueryList().contains(q)) {
					scrappedItem.addQueryList(q);
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

		logger.info("Saved: " + lastScrap.size() + " Items");		
	}

}
