package com.kl.voucher_pool.repository;

import com.kl.voucher_pool.model.Recipient;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecipientRepository extends MongoRepository<Recipient, String> {
    Optional<Recipient> findByEmail(String email);
}
