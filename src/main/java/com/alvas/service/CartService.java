package com.alvas.service;

import com.alvas.dto.CartDto;
import com.alvas.response.ApiResponse;

public interface CartService {
	ApiResponse addToCart(long userId,CartDto cartDto);
}
