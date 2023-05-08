package com.colab.app.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.colab.app.model.QueryList;
import com.colab.app.repository.QueryListRepository;

@Service
public class QueryListServiceImpl implements QueryListService {

	@Resource
	private QueryListRepository qRepo;
	
	@Override
	public QueryList updateQueryList(QueryList q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteQueryList(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryList addQueryList(QueryList q) {		
		QueryList savedQ = qRepo.save(q);
		return savedQ;
	}

	@Override
	public List<QueryList> getAllQueryList() {
		List<QueryList> qList = qRepo.findAll();
		return qList;
	}

	
}
