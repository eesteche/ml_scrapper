package com.colab.app.dto;

import org.json.JSONObject;

public class MeliCategoryDto {

	private String id;
	private String name;

	public MeliCategoryDto() {

	}

	public MeliCategoryDto(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public MeliCategoryDto(JSONObject jsonObj) {
		this.id = jsonObj.getString("id");
		this.name = jsonObj.getString("name");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
