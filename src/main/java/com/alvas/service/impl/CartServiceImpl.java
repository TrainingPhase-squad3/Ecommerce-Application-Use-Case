package com.alvas.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alvas.dto.CartDto;
import com.alvas.dto.CartProductDto;
import com.alvas.entity.Cart;
import com.alvas.entity.CartProduct;
import com.alvas.entity.Product;
import com.alvas.entity.User;
import com.alvas.exception.ProductNotFoundException;
import com.alvas.exception.UserNotFoundException;
import com.alvas.repository.CartRepository;
import com.alvas.repository.ProductRepository;
import com.alvas.repository.UserRepository;
import com.alvas.response.ApiResponse;
import com.alvas.service.CartService;
@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CartRepository cartRepository;
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	@Override
	public ApiResponse addToCart(long userId, CartDto cartDto) {
		User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id:"+userId+"not found"));
		List<Product> products=productRepository.findByProductIdIn(cartDto.getProductDtos().stream().map(CartProductDto::getProductId).toList());
		if(products.size()<cartDto.getProductDtos().size()) {
			logger.warn("requested product not found");
			throw new ProductNotFoundException("product not found");
		}
		Cart cart=new Cart();
		cart.setUser(user);
		cart.setTotalQuantity(cartDto.getProductDtos().stream().mapToInt(CartProductDto::getQuantity).sum());
		
		
	cart.setCartProducts(cartDto.getProductDtos().stream().map(productDto->{
			CartProduct cartProduct=new CartProduct();
			cartProduct.setQuantity(productDto.getQuantity());
			products.forEach(product->
			{
				
				
		            cartProduct.setProductId(product.getProductId());
		            double productPrice = product.getPrice();
		            double productTotalPrice = productPrice * productDto.getQuantity();
		            cart.setTotalPrice(cart.getTotalPrice() + productTotalPrice);
		        
			});	
			return cartProduct;
		}).toList());
		logger.info("Successfully persisted cart information");
		cartRepository.save(cart);
		
		return new ApiResponse("Successfully Added products to cart",HttpStatus.CREATED);
				
	}

}
