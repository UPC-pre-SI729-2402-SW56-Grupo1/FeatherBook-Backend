package com.example.featherbookbackend.shared.infrastructure;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public boolean processPayment(double amount, String userId) {
        // Simulación de un proceso de pago exitoso
        System.out.println("Processing payment of $" + amount + " for user " + userId);
        return true; // Indica que el pago fue exitoso
    }
}
