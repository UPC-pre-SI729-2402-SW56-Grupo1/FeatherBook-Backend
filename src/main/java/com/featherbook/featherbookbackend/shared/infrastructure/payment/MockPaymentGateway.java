package com.featherbook.featherbookbackend.shared.infrastructure.payment;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String userId) {
        System.out.println("Processing payment of $" + amount + " for user " + userId);
        return true;
    }
}
