package com.colab.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.colab.app.dto.Offer;
import com.colab.app.model.History;
import com.colab.app.repository.HistoryRepository;
import com.colab.app.repository.ItemRepository;

@Service
public class OfferServiceImpl implements OfferService {

	@Resource
	private ItemRepository iRepo;
	@Resource
	private HistoryRepository hRepo;

	@Override
	public List<Offer> getOffers() {

		

		return null;
	}

	@Override
	public List<History> getHistoryOffers() { 				
		return hRepo.findHistoryOffers();
	}

}
