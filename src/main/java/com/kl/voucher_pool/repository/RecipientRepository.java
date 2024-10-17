package com.example.voucherpool.repository;

import com.example.voucherpool.model.Recipient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecipientRepository extends MongoRepository<Recipient, String> {
    Optional<Recipient> findByEmail(String email);
}