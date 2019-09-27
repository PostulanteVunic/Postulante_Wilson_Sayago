package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IBeerItemDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.BeerItem;

@Service
public class BeerItemService {

	@Autowired
	private IBeerItemDao beerItemDao;
	
	@Transactional(readOnly = true)
	public List<BeerItem> findAll(){
		return  (List<BeerItem>) beerItemDao.findAll();
	}
	
	@Transactional
	public BeerItem save(BeerItem beer) {
		return beerItemDao.save(beer);
	}
	
	@Transactional
	public void delete(Long id) {
		beerItemDao.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public BeerItem findById(Long id) {
		return beerItemDao.findById(id).orElse(null);
	}
}
