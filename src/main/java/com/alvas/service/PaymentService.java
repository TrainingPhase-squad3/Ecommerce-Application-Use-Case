package com.alvas.service;

import com.alvas.dto.PaymentDto;
import com.alvas.response.ApiResponse;

public interface PaymentService {
	ApiResponse payment(long userId, PaymentDto paymentDto);
	List<PurchaseHistoryResponse> getUserPurchasesForMonth(Long userId, LocalDate monthStartDate, int pageNumber,
			int pageSize);

}
