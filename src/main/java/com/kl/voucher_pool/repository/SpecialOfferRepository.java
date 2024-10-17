package com.example.voucherpool.repository;

import com.example.voucherpool.model.SpecialOffer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecialOfferRepository extends MongoRepository<SpecialOffer, String> {
}