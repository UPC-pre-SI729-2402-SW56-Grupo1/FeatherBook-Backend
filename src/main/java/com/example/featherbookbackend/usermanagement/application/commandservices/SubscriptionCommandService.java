package com.example.featherbookbackend.usermanagement.application.commandservices;

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

    /**
     * Creates a new subscription.
     *
     * @param subscription The subscription to create.
     * @return The created subscription.
     */
    @Transactional
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    /**
     * Updates an existing subscription.
     *
     * @param subscriptionId The ID of the subscription to update.
     * @param updatedSubscription The updated subscription details.
     * @return The updated subscription, or null if the subscription doesn't exist.
     */
    @Transactional
    public Subscription updateSubscription(String subscriptionId, Subscription updatedSubscription) {
        Optional<Subscription> subscription = subscriptionRepository.findById(subscriptionId);
        if (subscription.isPresent()) {
            updatedSubscription.setId(subscriptionId);
            return subscriptionRepository.save(updatedSubscription);
        }
        return null;  // Implement error handling as necessary
    }

    /**
     * Deletes a subscription by its ID.
     *
     * @param subscriptionId The ID of the subscription to delete.
     */
    @Transactional
    public void deleteSubscription(String subscriptionId) {
        subscriptionRepository.deleteById(subscriptionId);
    }
}
