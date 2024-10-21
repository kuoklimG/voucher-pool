package com.kl.voucher_pool.repository;

import com.kl.voucher_pool.model.SpecialOffer;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing SpecialOffer entities.
 * This interface extends MongoRepository to provide CRUD operations for SpecialOffer objects.
 *
 * @author [Kuok Lim Goh]
 * @since [2024-10-21]
 */
public interface SpecialOfferRepository extends MongoRepository<SpecialOffer, String> {
    Optional<SpecialOffer> findByName(String name);
}