package com.alvas.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvas.dto.PaymentDto;
import com.alvas.response.ApiResponse;
import com.alvas.service.PaymentService;


@RestController
@RequestMapping("/users")
public class PaymentController {
	@Autowired
	PaymentService paymentService;

	@PostMapping("/{user-id}/purchase")
	public ResponseEntity<ApiResponse> payment(@Valid @RequestHeader long userID, @RequestBody PaymentDto paymentDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.payment(userID, paymentDto));

	}
	@GetMapping("/{userId}")
	public ResponseEntity<List<PurchaseHistoryResponse>> getUserPurchasesForMonth(@PathVariable Long userId,
			@RequestParam("year") int year, @RequestParam("month") int month, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		List<PurchaseHistoryResponse> purchases = paymentService.getUserPurchasesForMonth(userId,
				LocalDate.of(year, month, 1), pageNumber, pageSize);

		return ResponseEntity.ok(purchases);
	}

}
