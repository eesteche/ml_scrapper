package com.colab.app.service;

import java.util.List;

import com.colab.app.model.QueryList;

public interface QueryListService {

	public QueryList updateQueryList (QueryList q);

	public String deleteQueryList(Integer id);

	public QueryList addQueryList(QueryList q);

	public List<QueryList> getAllQueryList();

}
