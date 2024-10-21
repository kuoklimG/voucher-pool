package com.kl.voucher_pool.repository;

import com.kl.voucher_pool.model.Recipient;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for managing Recipient entities.
 * This interface extends MongoRepository to provide CRUD operations for Recipient objects.
 *
 * @author [Kuok Lim Goh]
 * @since [2024-10-21]
 */
public interface RecipientRepository extends MongoRepository<Recipient, String> {
    Optional<Recipient> findByEmail(String email);
}
