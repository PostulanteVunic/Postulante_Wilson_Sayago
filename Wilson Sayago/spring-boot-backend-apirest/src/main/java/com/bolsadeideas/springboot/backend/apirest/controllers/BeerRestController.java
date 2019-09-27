package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.BeerBox;
import com.bolsadeideas.springboot.backend.apirest.models.entity.BeerItem;
import com.bolsadeideas.springboot.backend.apirest.models.services.BeerItemService;
import com.bolsadeideas.springboot.backend.apirest.models.services.currency.CurrencylayerService;

@CrossOrigin
@RestController
@RequestMapping("/")
public class BeerRestController {

	@Autowired
	private BeerItemService beerService;
	
	@Autowired
	private CurrencylayerService currencyService;

	@GetMapping("/beers")
	public ResponseEntity<?> index() {
		return new ResponseEntity<List<BeerItem>>(beerService.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/beers/{beerID}")
	public ResponseEntity<?> show(@PathVariable Long beerID) {
		BeerItem beerItem = null;
		try {
			beerItem = beerService.findById(beerID);
		} catch(DataAccessException e) {
			return new ResponseEntity<BeerItem>(beerItem,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(beerItem == null) {
			return new ResponseEntity<BeerItem>(beerItem,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<BeerItem>(beerItem,HttpStatus.OK);
	}
	
	@PostMapping("/beers")
	public ResponseEntity<?> create(@Valid @RequestBody BeerItem beerItem, BindingResult result) {
		
		BeerItem beerItemNew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {

			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			beerItemNew = beerService.save(beerItem);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La Beer ha sido creada con éxito!");
		response.put("cliente", beerItemNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/beers/{beerID}/boxprice/{cantidad}")
	public ResponseEntity<?> showBoxPrice(@PathVariable Long beerID, @PathVariable Long cantidad) {
		BeerItem beerItem = null;
		try {
			beerItem = beerService.findById(beerID);
		} catch(DataAccessException e) {
			return new ResponseEntity<BeerItem>(beerItem,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(beerItem == null) {
			return new ResponseEntity<BeerItem>(beerItem,HttpStatus.NOT_FOUND);
		}
		Double monto = currencyService.consultarApi(beerItem.getCurrency());
		return new ResponseEntity<BeerBox>(new BeerBox(monto*cantidad),HttpStatus.OK);
	}
}
