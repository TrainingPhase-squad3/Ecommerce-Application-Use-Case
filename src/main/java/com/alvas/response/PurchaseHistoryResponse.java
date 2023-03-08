package com.alvas.response;

import java.time.LocalDate;
import java.util.List;

import com.alvas.entity.CartProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryResponse {
	
	
	public PurchaseHistoryResponse(LocalDate paymentDate2, double totalPrice2,
			List<com.alvas.entity.CartProduct> cartProducts) {
		// TODO Auto-generated constructor stub
	}
	private List<CartProduct> CartProduct;
	private LocalDate paymentDate;
	private Double totalPrice;
	
	


	

}
