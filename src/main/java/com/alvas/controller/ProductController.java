package com.alvas.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alvas.dto.ProductDto;
import com.alvas.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	ProductService productService;
	
	

	@GetMapping
	public ResponseEntity<List<ProductDto>> getProducts(@Valid @RequestParam @Size(min = 2)  String productName, @RequestParam	int pageNumber,
			@RequestParam int pageSize) {
		 Page<ProductDto> productsPage = productService.getProduct(productName, pageNumber, pageSize);
		 List<ProductDto> productsList = productsPage.getContent();
		    return new ResponseEntity<>(productsList, HttpStatus.OK);

	}

}
