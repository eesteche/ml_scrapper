package com.colab.app.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colab.app.model.Item;
import com.colab.app.model.QueryList;

@Repository
public interface QueryListRepository extends JpaRepository<QueryList, Long> {

	Set<QueryList> findByItem(Item item);
}
