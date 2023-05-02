package com.colab.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colab.app.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
