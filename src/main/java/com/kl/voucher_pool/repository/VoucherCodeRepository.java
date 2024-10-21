package com.kl.voucher_pool.repository;

import com.kl.voucher_pool.model.VoucherCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing VoucherCode entities.
 * This interface extends MongoRepository to provide CRUD operations for VoucherCode objects.
 *
 * @author [Kuok Lim Goh]
 * @since [2024-10-21]
 */
public interface VoucherCodeRepository extends MongoRepository<VoucherCode, String> {
    Optional<VoucherCode> findByCode(String code);
    List<VoucherCode> findByRecipientIdAndExpirationDateAfterAndUsageDateIsNull(String recipientId, LocalDate currentDate);
}