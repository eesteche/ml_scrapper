package com.colab.app.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colab.app.model.QueryList;
import com.colab.app.service.QueryListServiceImpl;

@RestController
@RequestMapping("/api/querylist")
public class QueryListController {

	@Autowired
	private QueryListServiceImpl qService;

	@GetMapping()
	public ResponseEntity<List<QueryList>> getAllProveedores() {
		List<QueryList> qList = qService.getAllQueryList();
		return new ResponseEntity<>(qList, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<QueryList> postProveedor(@RequestBody QueryList q) {
		QueryList post = qService.addQueryList(q);
		return new ResponseEntity<>(post, HttpStatus.CREATED);
	}

	
}
