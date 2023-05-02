package com.colab.app.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.colab.app.scrapper.ScrapperOperation;;

public class SearcherService {

	private String token = "${scrapperful.accesstoken}";
	public SearcherService() {
		
	}
	
	Logger logger = LoggerFactory.getLogger(SearcherService.class);
	
	public JSONObject searchItem(String q) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONObject results = resultsByPage(q,0);
		
		if(!results.isNull("results")) {
        	logger.info("resultado con items: "+ results.getJSONArray("results").length() + ", agrego results");
        	jsonObj.put("results", results.getJSONArray("results"));
        }        
        
        JSONObject obj1 = results.getJSONObject("paging");
        
        int totalResults = obj1.isNull("total") ? 0 : obj1.getInt("total");
        int offsetAux = 50;
        
        while (totalResults%50 > 0) {
        	results = resultsByPage(q,offsetAux);
        	results.getJSONArray("results").forEach( item -> {
        		jsonObj.getJSONArray("results").put( item );
        	});
        	offsetAux = offsetAux +50;
        	totalResults = totalResults -50;
        }
        logger.info("Resultados totales: " + jsonObj.getJSONArray("results").length());
        logger.info("Devuelvo: " +jsonObj.toString());
		return jsonObj;
		
		
	}
	
	private JSONObject resultsByPage(String q, int offset) throws Exception {
		
		URL url = new URL("https://api.mercadolibre.com/sites/MLA/search?q="+q+"&condition=new&shipping=fulfillment&offset="+offset);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization","Bearer " + "${scrapperful.accesstoken}");
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Accept","application/json");
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
            response.append(output +"\n");
            jsonObj = new JSONObject(output);                                
        }

        br.close();        
        return jsonObj;
				
	}
}