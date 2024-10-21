package com.kl.voucher_pool.repository;

import com.kl.voucher_pool.model.VoucherCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherCodeRepository extends MongoRepository<VoucherCode, String> {
    Optional<VoucherCode> findByCode(String code);
    List<VoucherCode> findByRecipientIdAndExpirationDateAfterAndUsageDateIsNull(String recipientId, LocalDate currentDate);
}