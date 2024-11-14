package com.example.featherbookbackend.usermanagement.application.commandservices;

import com.example.featherbookbackend.shared.infrastructure.PaymentGateway;
import com.example.featherbookbackend.usermanagement.domain.model.entities.Subscription;
import com.example.featherbookbackend.usermanagement.infrastructure.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SubscriptionCommandService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentGateway paymentGateway;

    /**
     * Crea una nueva suscripción y verifica el nivel permitido.
     *
     * @param subscription La suscripción a crear.
     * @return La suscripción creada, o null si el nivel de suscripción no es válido.
     */
    @Transactional
    public Subscription createSubscription(Subscription subscription) {
        if (!isValidSubscriptionLevel(subscription.getLevel())) {
            System.out.println("Invalid subscription level for user " + subscription.getUser().getId());
            return null;
        }

        boolean paymentSuccess = paymentGateway.processPayment(subscription.getPrice(), subscription.getUser().getId());
        if (paymentSuccess) {
            return subscriptionRepository.save(subscription);
        }
        return null;  // Implementar manejo de errores en caso de falla de pago
    }

    /**
     * Actualiza una suscripción existente y procesa el pago para el nuevo nivel.
     *
     * @param subscriptionId El ID de la suscripción a actualizar.
     * @param updatedSubscription Los detalles de la suscripción actualizados.
     * @return La suscripción actualizada o null si el pago falla o la suscripción no existe.
     */
    @Transactional
    public Subscription updateSubscription(String subscriptionId, Subscription updatedSubscription) {
        Optional<Subscription> subscription = subscriptionRepository.findById(subscriptionId);
        if (subscription.isPresent()) {
            // Procesar el pago para la actualización de suscripción
            boolean paymentSuccess = paymentGateway.processPayment(updatedSubscription.getPrice(), updatedSubscription.getUser().getId());
            if (paymentSuccess) {
                updatedSubscription.setId(subscriptionId);
                return subscriptionRepository.save(updatedSubscription);
            }
            System.out.println("Payment failed for user " + updatedSubscription.getUser().getId());
        }
        return null;  // Implementar manejo de errores adecuado
    }

    /**
     * Elimina una suscripción por su ID.
     *
     * @param subscriptionId El ID de la suscripción a eliminar.
     */
    @Transactional
    public void deleteSubscription(String subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }

    /**
     * Verifica si el nivel de suscripción es válido.
     *
     * @param level Nivel de suscripción.
     * @return true si es válido, false en caso contrario.
     */
    private boolean isValidSubscriptionLevel(int level) {
        // Definir los niveles permitidos (ejemplo: 1 = Básico, 2 = Premium, etc.)
        return level >= 1 && level <= 3;  // Asumiendo que hay 3 niveles de suscripción
    }
}
