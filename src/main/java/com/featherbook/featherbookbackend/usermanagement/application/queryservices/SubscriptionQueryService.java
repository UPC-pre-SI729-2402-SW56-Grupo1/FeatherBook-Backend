package com.featherbook.featherbookbackend.usermanagement.application.queryservices;

import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.Subscription;
import com.featherbook.featherbookbackend.usermanagement.infrastructure.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Optional<Subscription> getSubscriptionById(String subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }
    
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
}
