package com.colab.app.repository;

import java.util.HashMap;
import java.util.HashSet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.colab.app.model.Item;
import com.colab.app.model.QueryList;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	HashSet<Item> findByQueryList(QueryList q); 
	Item findByIdml(String idml);	
	
}
