package com.alvas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvas.controller.PaymentController;
import com.alvas.repository.PaymentRepository;
import com.alvas.service.PaymentService;
@Service
public class PaymentServiceImpl implements PaymentService {
	
	    @Autowired
	    PaymentRepository paymentRepository ;
	    
	    @Autowired
	    PaymentService paymentService;
	    
	    @Autowired
	    PaymentController paymentController;
	    
	    

	        
	    
	    
	    

	   

	    
	}


