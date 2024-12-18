package com.featherbook.featherbookbackend.usermanagement.infrastructure.repository;

import com.featherbook.featherbookbackend.usermanagement.domain.model.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
}
