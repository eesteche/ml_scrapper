package com.colab.app.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Item {

	@Id
	@OnDelete(action = OnDeleteAction.CASCADE)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long db_id;

	
	@Column(unique=true,nullable = false, length = 250)
	private String idml;

	@Column(nullable = false, length = 250)
	private String title;

	@Column(nullable = false, length = 225)
	private String thumbnail_id;

	@Column(nullable = false, length = 225)
	private String catalog_product_id;

	@Column(nullable = false, length = 400)
	private String permalink;

	@Column(nullable = false, length = 225)
	private String category_id;

	@Column(nullable = false, length = 225)
	private String domain_id;

	@Column(nullable = false, length = 225)
	private String thumbnail;

	private double price;
	private double original_price;
	private double sale_price;
	private int sold_quantity;
	private int available_quantity;

	private java.util.Date alta_fecha;
	private java.util.Date baja_fecha;
	private java.util.Date modi_fecha;

	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	private List<History> history;

	public Item(String id, String title, String thumbnail_id, String catalog_product_id, String permalink,
			String category_id, String domain_id, String thumbnail, double price, double original_price,
			double sale_price, int sold_quantity, int available_quantity) {
		this.idml = id;
		this.title = title;
		this.thumbnail_id = thumbnail_id;
		this.catalog_product_id = catalog_product_id;
		this.permalink = permalink;
		this.category_id = category_id;
		this.domain_id = domain_id;
		this.thumbnail = thumbnail;
		this.price = price;
		this.original_price = original_price;
		this.sale_price = sale_price;
		this.sold_quantity = sold_quantity;
		this.available_quantity = available_quantity;
	}
	
	public Item() {
		
	}
	
	@ManyToMany(mappedBy = "item")
    Set<QueryList> queryList;
	
	// *********************************GETTERS&SETTERS*********************************//

	
	@Override
	public int hashCode() {
		return Objects.hash(available_quantity, catalog_product_id, category_id, 
				domain_id, idml, original_price, permalink, price, sale_price, sold_quantity,
				thumbnail, thumbnail_id, title);
	}


	public String getIdml() {
		return idml;
	}

	public void setIdml(String ml_id) {
		this.idml = ml_id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return  available_quantity == other.available_quantity				
				&& Objects.equals(catalog_product_id, other.catalog_product_id)
				&& Objects.equals(category_id, other.category_id) 
				&& Objects.equals(domain_id, other.domain_id) 
				&& Objects.equals(idml, other.idml) 
				&& Double.doubleToLongBits(original_price) == Double.doubleToLongBits(other.original_price)
				&& Objects.equals(permalink, other.permalink)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Double.doubleToLongBits(sale_price) == Double.doubleToLongBits(other.sale_price)
				&& sold_quantity == other.sold_quantity && Objects.equals(thumbnail, other.thumbnail)
				&& Objects.equals(thumbnail_id, other.thumbnail_id) && Objects.equals(title, other.title);
	}


	
	public Set<QueryList> getQueryList() {
		return queryList;
	}


	public void setQueryList(Set<QueryList> queryList) {
		this.queryList = queryList;
	}

	public void addQueryList(QueryList q) {
		this.queryList.add(q);
		q.getItem().add(this);
	}

	public void removeQueryList(QueryList q) {
		this.getQueryList().remove(q);
		q.getItem().remove(this);
	}
	
	public void removeQueryLists() {
		for (QueryList q : new HashSet<>(queryList)) {
			removeQueryList(q);
		}
	}
	
	public List<History> getHistory() {
		return history;
	}

	public void setHistory(List<History> history) {
		this.history = history;
	}

	public Long getDb_id() {
		return db_id;
	}

	public void setDb_id(Long db_id) {
		this.db_id = db_id;
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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getDomain_id() {
		return domain_id;
	}

	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
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

	public int getSold_quantity() {
		return sold_quantity;
	}

	public void setSold_quantity(int sold_quantity) {
		this.sold_quantity = sold_quantity;
	}

	public int getAvailable_quantity() {
		return available_quantity;
	}

	public void setAvailable_quantity(int available_quantity) {
		this.available_quantity = available_quantity;
	}

	public java.util.Date getAlta_fecha() {
		return alta_fecha;
	}

	public void setAlta_fecha(java.util.Date alta_fecha) {
		this.alta_fecha = alta_fecha;
	}

	public java.util.Date getBaja_fecha() {
		return baja_fecha;
	}

	public void setBaja_fecha(java.util.Date baja_fecha) {
		this.baja_fecha = baja_fecha;
	}

	public java.util.Date getModi_fecha() {
		return modi_fecha;
	}

	public void setModi_fecha(java.util.Date modi_fecha) {
		this.modi_fecha = modi_fecha;
	}

}
