package com.bolsadeideas.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.backend.apirest.models.entity.BeerItem;

public interface IBeerItemDao extends CrudRepository<BeerItem, Long> {

}
