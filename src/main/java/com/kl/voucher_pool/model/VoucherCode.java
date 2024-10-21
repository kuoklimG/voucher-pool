package com.kl.voucher_pool.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "voucher_codes")
public class VoucherCode {
    @Id
    private String id;
    private String code;
    private String recipientId;
    private String specialOfferId;
    private LocalDate expirationDate;
    private LocalDateTime usageDate;

    public VoucherCode(String code, String recipientId, String specialOfferId, LocalDate expirationDate) {
        this.code = code;
        this.recipientId = recipientId;
        this.specialOfferId = specialOfferId;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSpecialOfferId() {
        return specialOfferId;
    }

    public void setSpecialOfferId(String specialOfferId) {
        this.specialOfferId = specialOfferId;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDateTime usageDate) {
        this.usageDate = usageDate;
    }
}