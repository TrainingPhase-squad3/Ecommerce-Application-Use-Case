package com.alvas.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alvas.entity.Cart;
import com.alvas.entity.CartProduct;
import com.alvas.entity.Payment;
import com.alvas.entity.User;
import com.alvas.exception.NoPurchaseHistoryFoundException;
import com.alvas.exception.UserIdNotFoundException;
import com.alvas.repository.CartRepository;
import com.alvas.repository.PaymentRepository;
import com.alvas.repository.UserRepository;
import com.alvas.response.PurchaseHistoryResponse;
import com.alvas.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	CartRepository cartRepository;
	@Autowired
	UserRepository userRepository;

	public List<PurchaseHistoryResponse> getUserPurchasesForMonth(Long userId, LocalDate monthStartDate, int pageNumber,
			int pageSize) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserIdNotFoundException("User not found with id: " + userId));
		LocalDate monthEndDate = monthStartDate.withDayOfMonth(monthStartDate.lengthOfMonth());
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Payment> paymentPage = paymentRepository.findByUserUserIdAndPaymentDateBetween(user.getUserId(),
				monthStartDate, monthEndDate, pageable);

		if (paymentPage.isEmpty()) {
			throw new NoPurchaseHistoryFoundException("No purchase history found for user id: " + userId);
		}

		List<PurchaseHistoryResponse> purchaseHistoryResponses = new ArrayList<>();
		paymentPage.getContent().forEach(payment -> {
			Cart cart = payment.getCart();
			List<CartProduct> cartProducts = cart.getCartProducts().stream()
					.map(cartProduct -> new CartProduct(cartProduct.getCartProductId(), cartProduct.getProductId(),
							cartProduct.getQuantity()))
					.collect(Collectors.toList());

			PurchaseHistoryResponse purchaseHistoryResponse = new PurchaseHistoryResponse(payment.getPaymentDate(),
					payment.getTotalPrice(), cartProducts);
			purchaseHistoryResponses.add(purchaseHistoryResponse);
		});

		return purchaseHistoryResponses;
	}
}


