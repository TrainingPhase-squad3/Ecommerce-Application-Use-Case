package com.alvas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alvas.entity.Cart;
import com.alvas.entity.Payment;
import com.alvas.entity.User;
import com.alvas.exception.NoPurchaseHistoryFoundException;
import com.alvas.repository.PaymentRepository;
import com.alvas.repository.UserRepository;
import com.alvas.response.PurchaseHistoryResponse;

@ExtendWith(SpringExtension.class)

public class PaymentServiceImplTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private PaymentServiceImpl paymentServiceImpl;

	@Test
	public void testGetUserPurchasesForMonth_NoPurchaseHistoryFound() {
		Long userId = 1L;
		LocalDate monthStartDate = LocalDate.now().minusMonths(1);
		int pageNumber = 0;
		int pageSize = 10;

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
		Mockito.when(paymentRepository.findByUserUserIdAndPaymentDateBetween(anyLong(), any(LocalDate.class),
				any(LocalDate.class), any(Pageable.class))).thenReturn(Page.empty());

		paymentServiceImpl.getUserPurchasesForMonth(userId, monthStartDate, pageNumber, pageSize);
		assertThrows(NoPurchaseHistoryFoundException.class,
				() -> paymentServiceImpl.getUserPurchasesForMonth(userId, monthStartDate, pageNumber, pageSize));

	}
	
	
	
	@Test
	public void testGetUserPurchasesForMonth() {
	   
	    User user = new User(1L, "chaitra", "shetty");
	    LocalDate monthStartDate = LocalDate.of(2022, 1, 1);
	    List<Payment> payments = new ArrayList<>();
	    Payment payment1 = new Payment(1L, user,1L, new Cart(),LocalDate.of(2022, 1, 10), 100.0);
	    Payment payment2 = new Payment(2L, user,1L, new Cart(),LocalDate.of(2023, 1, 10), 100.0);
	    payments.add(payment1);
	    payments.add(payment2);
	    Pageable pageable = PageRequest.of(0, 10);
	    Page<Payment> paymentPage = new PageImpl<>(payments, pageable, 2);

	 
	    Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
	    Mockito.when(paymentRepository.findByUserUserIdAndPaymentDateBetween(user.getUserId(), monthStartDate,
	            monthStartDate.withDayOfMonth(monthStartDate.lengthOfMonth()), pageable)).thenReturn(paymentPage);

	    
	    List<PurchaseHistoryResponse> result = paymentServiceImpl.getUserPurchasesForMonth(user.getUserId(), monthStartDate, 0, 10);

	   
	    assertEquals(2, result.size());
	    assertEquals(payment1.getPaymentDate(), result.get(0).getPaymentDate());
	    assertEquals(payment1.getTotalPrice(), result.get(0).getTotalPrice());
	    assertEquals(payment2.getPaymentDate(), result.get(1).getPaymentDate());
	    assertEquals(payment2.getTotalPrice(), result.get(1).getTotalPrice());
	}


}
