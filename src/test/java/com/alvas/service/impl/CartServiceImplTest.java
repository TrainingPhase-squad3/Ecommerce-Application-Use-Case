package com.alvas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alvas.dto.CartDto;
import com.alvas.dto.CartProductDto;
import com.alvas.entity.Product;
import com.alvas.entity.User;
import com.alvas.exception.ProductNotFoundException;
import com.alvas.exception.UserNotFoundException;
import com.alvas.repository.CartRepository;
import com.alvas.repository.ProductRepository;
import com.alvas.repository.UserRepository;
import com.alvas.response.ApiResponse;

@ExtendWith(SpringExtension.class)

class CartServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private ProductRepository productRepository;
	@Mock
	private CartRepository cartRepository;
	@InjectMocks
	private CartServiceImpl cartService;

	@Test
	void testAddToCart() {
		// create some mock data
		long userId = 1L;
		List<CartProductDto> productDtos = Arrays.asList(new CartProductDto(1L, 2), new CartProductDto(2L, 3));
		CartDto cartDto = new CartDto(productDtos);
		User user = new User();
		user.setUserId(userId);
		Product product1 = new Product();
		product1.setProductName("product1");
		product1.setPrice(10.0);
		product1.setProductId(1L);
		Product product2 = new Product();
		product2.setProductName("product2");
		product2.setPrice(20.0);
		product2.setProductId(2L);
		List<Product> products = Arrays.asList(product1, product2);

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		Mockito.when(productRepository.findByProductIdIn(Mockito.anyList())).thenReturn(products);

		ApiResponse response = cartService.addToCart(userId, cartDto);

		assertEquals("Successfully Added to cart", response.getMessage());
		assertEquals(HttpStatus.CREATED, response.getStatus());
	}

	@Test
	void testAddToCartUserNotFound() {
		long userId = 1L;
		List<CartProductDto> productDtos = Arrays.asList(new CartProductDto(1L, 2), new CartProductDto(2L, 3));
		CartDto cartDto = new CartDto(productDtos);
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> {
			cartService.addToCart(userId, cartDto);
		});
	}

	@Test
	void testAddToCartProductNotFound() {
		long userId = 1L;
		List<CartProductDto> productDtos = Arrays.asList(new CartProductDto(1L, 2), new CartProductDto(2L, 3));
		User user = new User();
		user.setUserId(userId);
		CartDto cartDto = new CartDto(productDtos);
		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		assertThrows(ProductNotFoundException.class, () -> {
			cartService.addToCart(userId, cartDto);
		});

	}

}
