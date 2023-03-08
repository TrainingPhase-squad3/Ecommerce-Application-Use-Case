package com.alvas.service;

import com.alvas.dto.PaymentDto;
import com.alvas.response.ApiResponse;

public interface PaymentService {
	ApiResponse payment(long userId, PaymentDto paymentDto);

}
