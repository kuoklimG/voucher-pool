package com.example.voucherpool.repository;

import com.example.voucherpool.model.VoucherCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherCodeRepository extends MongoRepository<VoucherCode, String> {
    Optional<VoucherCode> findByCode(String code);
    List<VoucherCode> findValidUnusedVouchers(String recipientId, LocalDate currentDate);
}