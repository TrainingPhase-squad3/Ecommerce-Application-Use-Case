package com.alvas.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.alvas.dto.PaymentDto;
import com.alvas.entity.Cart;
import com.alvas.entity.CartProduct;
import com.alvas.entity.Payment;
import com.alvas.entity.Product;
import com.alvas.entity.User;
import com.alvas.entity.Wallet;
import com.alvas.exception.CartNotFoundException;
import com.alvas.exception.InsufficientFundsException;
import com.alvas.exception.RequestedQuantityNotAvailableException;
import com.alvas.exception.UserNotFoundException;
import com.alvas.exception.WalletExpiredException;
import com.alvas.exception.WalletNotFoundException;
import com.alvas.repository.CartRepository;
import com.alvas.repository.PaymentRepository;
import com.alvas.repository.ProductRepository;
import com.alvas.repository.UserRepository;
import com.alvas.repository.WalletRepository;
import com.alvas.response.ApiResponse;

@ExtendWith(SpringExtension.class)
class PaymentServiceImplTest {

	@Mock
	ProductRepository productRepository;

	@Mock
	CartRepository cartRepository;

	@Mock
	WalletRepository walletRepository;

	@Mock
	PaymentRepository paymentRepository;

	@Mock
	UserRepository userRepository;

	@InjectMocks
	PaymentServiceImpl paymentServiceImpl;

	@Test
	void testPaymentSuccess() {
		Product product = new Product(1L, "Bat", 45, 665.45);
		Product product2 = new Product(2L, "Basket ball", 50, 1000);

		User user = new User(12L, "Manoj", "manu@gmail.com");

		Wallet wallet = new Wallet(1L, user, LocalDate.now().plusDays(5), 30000, "Paytm");

		CartProduct cartProduct = new CartProduct(123L, 1L, 3);
		CartProduct cartProduct2 = new CartProduct(234L, 2L, 5);

		List<CartProduct> cartProducts = new ArrayList<>();
		cartProducts.add(cartProduct);
		cartProducts.add(cartProduct2);

		Cart cart = new Cart(1L, 6996.35, 8, user, cartProducts);
		PaymentDto paymentDto = new PaymentDto(1L, 1L);
		Payment payment = new Payment(1L, user, 1L, cart, LocalDate.now());

		Map<Long, Product> productMap = new HashMap<Long, Product>() {
			{
				put(product.getProductId(), product);
				put(product2.getProductId(), product2);
			}
		};
		List<Product> productList = new ArrayList<>(productMap.values());

		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(cartRepository.findById(paymentDto.getCartId())).thenReturn(Optional.of(cart));
		Mockito.when(walletRepository.findById(paymentDto.getWalletId())).thenReturn(Optional.of(wallet));
		Mockito.when(productRepository.findAllById(anyList())).thenReturn(productList);
		Mockito.when(productRepository.saveAll(productMap.values())).thenReturn(productList);
		Mockito.when(walletRepository.save(wallet)).thenReturn(wallet);
		Mockito.when(paymentRepository.save(payment)).thenReturn(payment);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse = paymentServiceImpl.payment(user.getUserId(), paymentDto);

		assertEquals("purchase successfull", apiResponse.getMessage());
		assertEquals(HttpStatus.OK, apiResponse.getStatus());
		verify(cartRepository).findById(paymentDto.getCartId());
		verify(walletRepository).findById(paymentDto.getWalletId());
		verify(productRepository).findAllById(anyList());

	}

	@Test
	void testUserNotFound() {
		long userId = 1L;
		PaymentDto paymentDto = new PaymentDto(1L, 1L);

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> paymentServiceImpl.payment(userId, paymentDto));
		verify(userRepository).findById(userId);
		verifyNoInteractions(cartRepository, walletRepository, productRepository);
	}

	@Test
	void testCartNotFound() {
		long userId = 1L;
		PaymentDto paymentDto = new PaymentDto(1L, 1L);
		User user = new User(userId, "Manoj", "manu@gmail.com");

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		Mockito.when(cartRepository.findById(paymentDto.getCartId())).thenReturn(Optional.empty());

		assertThrows(CartNotFoundException.class, () -> paymentServiceImpl.payment(userId, paymentDto));
		verify(userRepository).findById(userId);
		verify(cartRepository).findById(paymentDto.getCartId());
		verifyNoInteractions(walletRepository, productRepository);
	}

	@Test
	void testPaymentWalletNotFound() {
		long userId = 1L;
		PaymentDto paymentDto = new PaymentDto(1L, 1L);
		User user = new User(userId, "Manoj", "manu@gmail.com");
		Cart cart = new Cart(1L, 8975, 5, user, new ArrayList<CartProduct>());

		Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		Mockito.when(cartRepository.findById(cart.getCartId())).thenReturn(Optional.of(cart));
		Mockito.when(walletRepository.findById(1L)).thenReturn(Optional.empty());

		verifyNoInteractions(paymentRepository);
		assertThrows(WalletNotFoundException.class, () -> paymentServiceImpl.payment(userId, paymentDto));
		verify(userRepository).findById(userId);
		verify(cartRepository).findById(cart.getCartId());
		verify(walletRepository).findById(1L);
		verifyNoInteractions(paymentRepository);

	}

	@Test
	void testRequestedQuantityUnavailable() {
		Product product = new Product(1L, "Bat", 45, 665.45);
		Product product2 = new Product(2L, "Basket ball", 50, 1000);

		User user = new User(12L, "Manoj", "manu@gmail.com");

		Wallet wallet = new Wallet(1L, user, LocalDate.now().plusDays(5), 30000, "Paytm");

		CartProduct cartProduct = new CartProduct(123L, 1L, 3);
		CartProduct cartProduct2 = new CartProduct(234L, 2L, 55);

		List<CartProduct> cartProducts = new ArrayList<>();
		cartProducts.add(cartProduct);
		cartProducts.add(cartProduct2);

		Cart cart = new Cart(1L, 6996.35, 8, user, cartProducts);
		PaymentDto paymentDto = new PaymentDto(1L, 1L);

		Map<Long, Product> productMap = new HashMap<Long, Product>() {
			{
				put(product.getProductId(), product);
				put(product2.getProductId(), product2);
			}
		};
		List<Product> productList = new ArrayList<>(productMap.values());
		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(cartRepository.findById(paymentDto.getCartId())).thenReturn(Optional.of(cart));
		Mockito.when(walletRepository.findById(paymentDto.getWalletId())).thenReturn(Optional.of(wallet));
		Mockito.when(productRepository.findAllById(anyList())).thenReturn(productList);
		Mockito.when(productRepository.saveAll(productMap.values())).thenReturn(productList);
		assertThrows(RequestedQuantityNotAvailableException.class, () -> paymentServiceImpl.payment(12L, paymentDto));

	}

	@Test
	void testInsufficientFund() {
		Product product = new Product(1L, "Bat", 45, 665.45);
		Product product2 = new Product(2L, "Basket ball", 50, 1000);

		User user = new User(12L, "Manoj", "manu@gmail.com");

		Wallet wallet = new Wallet(1L, user, LocalDate.now().plusDays(5), 6000, "Paytm");

		CartProduct cartProduct = new CartProduct(123L, 1L, 3);
		CartProduct cartProduct2 = new CartProduct(234L, 2L, 5);

		List<CartProduct> cartProducts = new ArrayList<>();
		cartProducts.add(cartProduct);
		cartProducts.add(cartProduct2);

		Cart cart = new Cart(1L, 6996.35, 8, user, cartProducts);
		PaymentDto paymentDto = new PaymentDto(1L, 1L);

		Map<Long, Product> productMap = new HashMap<Long, Product>() {
			{
				put(product.getProductId(), product);
				put(product2.getProductId(), product2);
			}
		};
		List<Product> productList = new ArrayList<>(productMap.values());

		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(cartRepository.findById(paymentDto.getCartId())).thenReturn(Optional.of(cart));
		Mockito.when(walletRepository.findById(paymentDto.getWalletId())).thenReturn(Optional.of(wallet));
		Mockito.when(productRepository.findAllById(anyList())).thenReturn(productList);
		Mockito.when(productRepository.saveAll(productMap.values())).thenReturn(productList);
		assertThrows(InsufficientFundsException.class, () -> paymentServiceImpl.payment(12L, paymentDto));
	}

	@Test
	void testWalletExpired() {
		Product product = new Product(1L, "Bat", 45, 665.45);
		Product product2 = new Product(2L, "Basket ball", 50, 1000);

		User user = new User(12L, "Manoj", "manu@gmail.com");

		Wallet wallet = new Wallet(1L, user, LocalDate.now().minusDays(5), 7000, "Paytm");

		CartProduct cartProduct = new CartProduct(123L, 1L, 3);
		CartProduct cartProduct2 = new CartProduct(234L, 2L, 5);

		List<CartProduct> cartProducts = new ArrayList<>();
		cartProducts.add(cartProduct);
		cartProducts.add(cartProduct2);

		Cart cart = new Cart(1L, 6996.35, 8, user, cartProducts);
		PaymentDto paymentDto = new PaymentDto(1L, 1L);

		Map<Long, Product> productMap = new HashMap<Long, Product>() {
			{
				put(product.getProductId(), product);
				put(product2.getProductId(), product2);
			}
		};
		List<Product> productList = new ArrayList<>(productMap.values());

		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		Mockito.when(cartRepository.findById(paymentDto.getCartId())).thenReturn(Optional.of(cart));
		Mockito.when(walletRepository.findById(paymentDto.getWalletId())).thenReturn(Optional.of(wallet));
		Mockito.when(productRepository.findAllById(anyList())).thenReturn(productList);
		Mockito.when(productRepository.saveAll(productMap.values())).thenReturn(productList);
		assertThrows(WalletExpiredException.class, () -> paymentServiceImpl.payment(12L, paymentDto));

	}
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
