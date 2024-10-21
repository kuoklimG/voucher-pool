package com.kl.voucher_pool.repository;

import com.kl.voucher_pool.model.SpecialOffer;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecialOfferRepository extends MongoRepository<SpecialOffer, String> {
    Optional<SpecialOffer> findByName(String name);
}