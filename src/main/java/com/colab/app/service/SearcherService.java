package com.colab.app.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.colab.app.dto.MeliCategoryDto;
import com.colab.app.model.Item;;

@Service
public class SearcherService {

	public SearcherService() {

	}

	Logger logger = LoggerFactory.getLogger(SearcherService.class);
	List<Item> items = new CopyOnWriteArrayList<>();

	public JSONObject searchItem(String q) throws Exception {

		JSONObject jsonObj = new JSONObject();
		JSONObject results = resultsByPage(q, 0);

		if (!results.isNull("results")) {
			logger.debug("resultado con items: " + results.getJSONArray("results").length() + ", agrego results");
			jsonObj.put("results", results.getJSONArray("results"));
		}

		JSONObject obj1 = results.getJSONObject("paging");

		int totalResults = obj1.isNull("total") ? 0 : obj1.getInt("total");
		int offsetAux = 50;

		while (totalResults % 50 > 0) {
			results = resultsByPage(q, offsetAux);
			results.getJSONArray("results").forEach(item -> {
				jsonObj.getJSONArray("results").put(item);
			});
			offsetAux = offsetAux + 50;
			totalResults = totalResults - 50;
		}
		logger.info("searchItem(String q): Resultados totales: " + jsonObj.getJSONArray("results").length());
		logger.debug("Devuelvo: " + jsonObj.toString());
		return jsonObj;

	}

	public List<Item> searchItemByCategory(String q) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		JSONObject jsonObj = new JSONObject();
		JSONObject results = resultsByPageCategory(q, 0);

		if (!results.isNull("results")) {
			logger.debug("resultado con items: " + results.getJSONArray("results").length() + ", agrego results");
			jsonObj.put("results", results.getJSONArray("results"));
		}

		JSONObject obj1 = results.getJSONObject("paging");

		int totalResults = obj1.isNull("total") ? 0 : obj1.getInt("total");
		int offsetAux = 50;
		if (totalResults == 0) {
			logger.debug("devuelvo vacio. totalResults: " + totalResults + " q: " + q);
			return new ArrayList<Item>();
		}
		// TODO: public user limit = 1000/ Add token.
		List<URL> totalDownloads = getDownloadList(totalResults, offsetAux, q);

		for (URL url : totalDownloads) {
			executor.execute(() -> {
				JSONObject aux = null;
				try {
					aux = resultsByCategory(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (aux != null) {
					items.addAll(JsonToItem(aux));
					logger.debug("searchItemByCategory: " + items.size() + " Items added on URL: " + url.toString());
				}

			});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info("searchItemByCategory(String " + q + "): Resultados totales: " + items.size());
		List<Item> returns = new ArrayList<Item>(items);

		items.clear();
		return returns;

	}

	private JSONObject resultsByPage(String q, int offset) throws Exception {

		URL url = new URL("https://api.mercadolibre.com/sites/MLA/search?q=" + q
				+ "&condition=new&shipping=fulfillment&offset=" + offset);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestMethod("GET");
		BufferedReader br = null;
		if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		String output;
		JSONObject jsonObj = new JSONObject();

		StringBuffer response = new StringBuffer();
		while ((output = br.readLine()) != null) {
			response.append(output + "\n");
			jsonObj = new JSONObject(output);
		}

		br.close();
		return jsonObj;

	}

	private JSONObject resultsByPageCategory(String q, int offset) throws Exception {
		URL url = new URL("https://api.mercadolibre.com/sites/MLA/search?category=" + q
				+ "&condition=new&shipping=fulfillment&offset=" + offset);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestMethod("GET");
		BufferedReader br = null;
		if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		String output;
		JSONObject jsonObj = new JSONObject();

		StringBuffer response = new StringBuffer();
		while ((output = br.readLine()) != null) {
			response.append(output + "\n");
			jsonObj = new JSONObject(output);
		}

		br.close();
		return jsonObj;

	}

	private JSONObject resultsByCategory(URL url) throws Exception {
		logger.debug("Init on: " + url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestMethod("GET");
		BufferedReader br = null;
		if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		String output;
		JSONObject jsonObj = new JSONObject();

		StringBuffer response = new StringBuffer();
		while ((output = br.readLine()) != null) {
			response.append(output + "\n");
			jsonObj = new JSONObject(output);
		}

		br.close();
		return jsonObj;

	}

	public JSONArray getCategories() throws Exception {
		URL url = new URL("https://api.mercadolibre.com/sites/MLA/categories");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestMethod("GET");
		BufferedReader br = null;
		if (100 <= conn.getResponseCode() && conn.getResponseCode() <= 399) {
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		String output;
		JSONArray jsonObj = new JSONArray();

		StringBuffer response = new StringBuffer();
		while ((output = br.readLine()) != null) {
			response.append(output + "\n");
			jsonObj = new JSONArray(output);
		}

		br.close();
		return jsonObj;

	}

	public List<MeliCategoryDto> getCategoriesFromJson(JSONArray jsonArray) {
		List<MeliCategoryDto> categories = new ArrayList<MeliCategoryDto>();

		if (jsonArray.length() == 0 || jsonArray == null) {
			return categories;
		}

		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			MeliCategoryDto category = new MeliCategoryDto(jsonArray.getJSONObject(i));
			categories.add(category);
		}

		return categories;
	}

	public List<URL> getDownloadList(int totalResults, int offset, String q) throws MalformedURLException {
		List<URL> urlList = new CopyOnWriteArrayList<URL>();
		logger.debug(4.1 + " totalResults: " + totalResults + " offset: " + offset + " q: " + q);
		while (totalResults % 50 > 0 && offset <= 1000) {
			URL url = new URL("https://api.mercadolibre.com/sites/MLA/search?category=" + q
					+ "&condition=new&shipping=fulfillment&offset=" + offset);
			urlList.add(url);
			offset = offset + 50;
		}
		return urlList;
	}

	public List<Item> JsonToItem(JSONObject json) {
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
					String catalog_product_id = obj1.isNull("catalog_product_id") ? ""
							: obj1.getString("catalog_product_id");
					String permalink = obj1.getString("permalink");
					String category_id = obj1.getString("category_id");
					String domain_id = obj1.getString("domain_id");
					String thumbnail = obj1.getString("thumbnail");
					double price = obj1.isNull("price") ? 0.00 : obj1.getDouble("price");
					double original_price = obj1.isNull("original_price") ? 0.00 : obj1.getDouble("original_price");
					double sale_price = obj1.isNull("sale_price") ? 0.00 : obj1.getDouble("sale_price");
					int sold_quantity = obj1.getInt("sold_quantity");
					int available_quantity = obj1.getInt("available_quantity");
					String model = getModel(obj1);

					Item aux = new Item(id, title, thumbnail_id, catalog_product_id, permalink, category_id, domain_id,
							thumbnail, price, original_price, sale_price, sold_quantity, available_quantity, model);
					itemList.add(aux);
				}
			}

		});
		return itemList;

	}

	private String getModel(JSONObject obj1) {
		String model = "";

		for (String s : obj1.keySet()) {
			if (s.equals("attributes")) {
				JSONArray attributes = obj1.getJSONArray("attributes");
				if (attributes.length() == 0) {
					return model;
				}
				for (int i = 0; i < attributes.length(); i++) {
					JSONObject obj2 = attributes.getJSONObject(i);					
					if (!obj2.getString("id").equals("MODEL")) {
						continue;
					}
					if(obj2.isNull("value_name") || obj2.getString("value_name").equals("-1")) {
						continue;
					}
					model = obj2.getString("value_name");
				}
			}
		}
		return model;
	}
}