package com.colab.app.repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.colab.app.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
					
	@Query(
			  value = "SELECT * FROM History h WHERE h.precio_viejo > 0 AND h.precio_nuevo < h.precio_viejo", 
			  nativeQuery = true)
	List<History> findHistoryOffers();
}
