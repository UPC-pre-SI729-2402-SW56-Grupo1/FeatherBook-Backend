package com.example.featherbookbackend.shared.infrastructure;

public interface PaymentGateway {

    /**
     * Procesa un pago para una suscripción.
     *
     * @param amount El monto a cobrar.
     * @param userId El ID del usuario que realiza el pago.
     * @return True si el pago se realizó con éxito, False en caso contrario.
     */
    boolean processPayment(double amount, String userId);
}
