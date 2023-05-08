package com.colab.app.dto;

import com.colab.app.model.History;
import com.colab.app.model.Item;

public class Offer {
	
	private String idml;
	private String title;
	private String thumbnail_id;
	private String catalog_product_id;
	private String permalink;
	private String thumbnail;
	private double price;
	private double original_price;
	private double sale_price;
	private double precio_viejo;
	private double precio_nuevo;
	private java.util.Date modi_fecha;
	
	public Offer(Item item, History history) {
		this.idml = item.getIdml();
		this.title = item.getTitle();
		this.thumbnail_id = item.getThumbnail_id();
		this.catalog_product_id = item.getCatalog_product_id();
		this.permalink = item.getPermalink();
		this.thumbnail = item.getThumbnail();
		this.price = item.getPrice();
		this.original_price = item.getOriginal_price();
		this.sale_price = item.getSale_price();
		this.precio_viejo = history.getPrecio_viejo();
		this.precio_nuevo = history.getPrecio_nuevo();
		this.modi_fecha = item.getModi_fecha();
	}

	public String getIdml() {
		return idml;
	}

	public void setIdml(String idml) {
		this.idml = idml;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail_id() {
		return thumbnail_id;
	}

	public void setThumbnail_id(String thumbnail_id) {
		this.thumbnail_id = thumbnail_id;
	}

	public String getCatalog_product_id() {
		return catalog_product_id;
	}

	public void setCatalog_product_id(String catalog_product_id) {
		this.catalog_product_id = catalog_product_id;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	public double getPrecio_viejo() {
		return precio_viejo;
	}

	public void setPrecio_viejo(double precio_viejo) {
		this.precio_viejo = precio_viejo;
	}

	public double getPrecio_nuevo() {
		return precio_nuevo;
	}

	public void setPrecio_nuevo(double precio_nuevo) {
		this.precio_nuevo = precio_nuevo;
	}

	public java.util.Date getModi_fecha() {
		return modi_fecha;
	}

	public void setModi_fecha(java.util.Date modi_fecha) {
		this.modi_fecha = modi_fecha;
	}
	
	
}
