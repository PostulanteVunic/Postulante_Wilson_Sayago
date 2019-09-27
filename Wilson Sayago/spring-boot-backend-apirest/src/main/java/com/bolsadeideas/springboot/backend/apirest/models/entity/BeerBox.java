package com.bolsadeideas.springboot.backend.apirest.models.entity;

public class BeerBox {

	private Double total;

	public BeerBox(Double total)
	{
		this.total = total;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
