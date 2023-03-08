package com.alvas.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alvas.dto.ProductDto;
import com.alvas.entity.Product;
import com.alvas.exception.ProductNotFoundException;
import com.alvas.repository.ProductRepository;
import com.alvas.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepository productRepository;

	@Override
	public Page<ProductDto> getProduct(String productName, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(productName, pageable);
		if (productPage.isEmpty()) {
			throw new ProductNotFoundException("product is not available");
		} else {
			return productPage.map(p -> {
				ProductDto productDto = new ProductDto();
				BeanUtils.copyProperties(p, productDto);
				return productDto;
			});
		}

	}

}
