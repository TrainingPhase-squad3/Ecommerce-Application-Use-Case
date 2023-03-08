package com.alvas.service;

import java.time.LocalDate;
import java.util.List;

import com.alvas.response.PurchaseHistoryResponse;

public interface PaymentService {

	List<PurchaseHistoryResponse> getUserPurchasesForMonth(Long userId, LocalDate monthStartDate, int pageNumber,
			int pageSize);



	

	





	

	

	




	

	
}
