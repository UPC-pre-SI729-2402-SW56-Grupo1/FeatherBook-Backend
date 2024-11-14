package com.example.featherbookbackend.usermanagement.application.queryservices;

import com.example.featherbookbackend.usermanagement.domain.model.entities.Subscription;
import com.example.featherbookbackend.usermanagement.infrastructure.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionQueryService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /**
     * Retrieves a subscription by its ID.
     *
     * @param subscriptionId The ID of the subscription to retrieve.
     * @return An Optional containing the subscription if found.
     */
    public Optional<Subscription> getSubscriptionById(String subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }

    /**
     * Retrieves all subscriptions in the system.
     *
     * @return A list of all subscriptions.
     */
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
}
