package com.colab.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=200)
	private String q;
			
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "db_id")
	private Item item;	
	
	private double precio_viejo;
	private double precio_nuevo;
	
	private java.util.Date alta_fecha;
	private java.util.Date baja_fecha;
	
	public History(String q,Item item,double precio_viejo,double precio_nuevo,java.util.Date alta_fecha) {
		this.q = q;
		this.item = item;
		this.precio_viejo = precio_viejo;
		this.precio_nuevo = precio_nuevo;
		this.alta_fecha = alta_fecha;
	}
	
	public History() {
		
	}
	
	//*********************************GETTERS&SETTERS*********************************//
	
	public Long getDb_id() {
		return id;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setDb_id(Long db_id) {
		this.id = db_id;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
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
	
}
