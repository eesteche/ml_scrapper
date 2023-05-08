package com.colab.app.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colab.app.dto.Offer;
import com.colab.app.model.History;
import com.colab.app.service.OfferService;

@RestController
@RequestMapping("/api/offer")
public class OffersController {
	
	@Autowired
	private OfferService offerService;
	
	@GetMapping()
	public ResponseEntity<List<History>> getAllProveedores() {
		List<History> offerList = offerService.getHistoryOffers();
		return new ResponseEntity<>(offerList, HttpStatus.OK);
	}

}
