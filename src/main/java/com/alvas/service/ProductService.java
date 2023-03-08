package com.alvas.service;

import org.springframework.data.domain.Page;

import com.alvas.dto.ProductDto;


public interface ProductService {

	Page<ProductDto> getProduct(String productName, int pageNumber, int pageSize);
}
