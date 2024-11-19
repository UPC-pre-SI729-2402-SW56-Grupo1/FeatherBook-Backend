package com.featherbook.featherbookbackend.shared.infrastructure.payment;

public interface PaymentGateway {
    boolean processPayment(double amount, String userId);
}
