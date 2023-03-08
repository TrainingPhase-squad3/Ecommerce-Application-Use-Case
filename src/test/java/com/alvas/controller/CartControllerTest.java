package com.alvas.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alvas.dto.CartDto;
import com.alvas.dto.CartProductDto;
import com.alvas.response.ApiResponse;
import com.alvas.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
class CartControllerTest {
private MockMvc mockMvc;
    
    @Mock
    private CartService cartService;
    
    @InjectMocks
    private CartController cartController;
    
   @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }
    
    @Test
    void testAddToCart() throws Exception {
        long userId = 1L;
        List<CartProductDto> productDtos = Arrays.asList(
            new CartProductDto(1L, 2),
            new CartProductDto(2L, 3)
        );
        CartDto cartDto = new CartDto(productDtos);
        ApiResponse expectedResponse = new ApiResponse("Successfully Added to cart", HttpStatus.CREATED);
        Mockito.when(cartService.addToCart(userId, cartDto)).thenReturn(expectedResponse);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/user-carts")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cartDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(expectedResponse.getMessage())));
        
        Mockito.verify(cartService).addToCart(userId, cartDto);
    }
    
   
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
   
    @Test
    void testAddToCartFailure() throws Exception {
        long userId = 1L;
        List<CartProductDto> productDtos = Arrays.asList(
            new CartProductDto(1L, 2),
            new CartProductDto(2L, 3)
        );
        CartDto cartDto = new CartDto(productDtos);
        ApiResponse expectedResponse = new ApiResponse("Failed to add to cart", HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(cartService.addToCart(userId, cartDto)).thenReturn(expectedResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/user-carts")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cartDto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is(expectedResponse.getMessage())));

        Mockito.verify(cartService).addToCart(userId, cartDto);
    }
    
   
}

