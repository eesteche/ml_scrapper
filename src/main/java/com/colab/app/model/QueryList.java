package com.colab.app.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class QueryList {

	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=200)
	private String q;
	
	private java.util.Date alta_fecha;
	private java.util.Date baja_fecha;
	private java.util.Date modi_fecha;
	
		
	@ManyToMany
	@JoinTable(name = "query_list_item", 
    joinColumns = { @JoinColumn(name = "query_list_id") }, 
    inverseJoinColumns = { @JoinColumn(name = "item_db_id") })		
    Set<Item> item;
	//*********************************GETTERS&SETTERS*********************************//
	
	public void addItem(Item item) {
		this.item.add(item);
		item.getQueryList().add(this);
	}
	
	public void removeItem(Item item) {
		this.getItem().remove(item);
		item.getQueryList().remove(this);
	}

	public void removeItems() {
		for (Item i : new HashSet<>(item)) {
			removeItem(i);
		}
	}
	
	public Long getId() {
		return id;
	}
	public Set<Item> getItem() {
		return item;
	}
	public void setItem(Set<Item> item) {
		this.item = item;
	}
	public void setId(Long db_id) {
		this.id = db_id;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
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

	@Override
	public String toString() {
		return "QueryList [q=" + q + "]";
	}
	
	
	
	
}