package com.colab.app.service;

import java.util.List;

import com.colab.app.dto.Offer;
import com.colab.app.model.History;

public interface OfferService {

	List<History> getHistoryOffers();
	List<Offer> getOffers();
}
