package com.alvas.dto;

import lombok.Data;

@Data
public class ProductDto {
	private String productName;

	private int availableQuantity;

	private double price;
}
