package com.kl.voucher_pool.service;

import com.kl.voucher_pool.model.Recipient;
import com.kl.voucher_pool.model.SpecialOffer;
import com.kl.voucher_pool.model.VoucherCode;
import com.kl.voucher_pool.repository.RecipientRepository;
import com.kl.voucher_pool.repository.SpecialOfferRepository;
import com.kl.voucher_pool.repository.VoucherCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class VoucherPoolService {
    private final RecipientRepository recipientRepository;
    private final SpecialOfferRepository specialOfferRepository;
    private final VoucherCodeRepository voucherCodeRepository;

    @Autowired
    public VoucherPoolService(RecipientRepository recipientRepository,
                              SpecialOfferRepository specialOfferRepository,
                              VoucherCodeRepository voucherCodeRepository) {
        this.recipientRepository = recipientRepository;
        this.specialOfferRepository = specialOfferRepository;
        this.voucherCodeRepository = voucherCodeRepository;
    }

    private final Random random = new Random();
    private static final String RECIPIENT_NOT_FOUND = "Recipient not found";
    private static final String SPECIAL_OFFER_NOT_FOUND = "Special offer not found";

    public VoucherCode generateVoucherCode(String recipientEmail, String specialOfferName, LocalDate expirationDate) {
        Recipient recipient = recipientRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new IllegalArgumentException(RECIPIENT_NOT_FOUND));

        SpecialOffer specialOffer = specialOfferRepository.findByName(specialOfferName)
                .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND));

        String code = generateUniqueCode();
        VoucherCode voucherCode = new VoucherCode(code, recipient.getId(), specialOffer.getId(), expirationDate);
        return voucherCodeRepository.save(voucherCode);
    }

    public Map<String, Object> validateVoucherCode(String code, String email) {
        VoucherCode voucherCode = voucherCodeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Invalid voucher code"));

        Recipient recipient = recipientRepository.findById(voucherCode.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException(RECIPIENT_NOT_FOUND));

        if (!recipient.getEmail().equals(email)) {
            throw new IllegalArgumentException("Invalid recipient email");
        }

        if (voucherCode.getUsageDate() != null) {
            throw new IllegalArgumentException("Voucher code has already been used");
        }

        if (voucherCode.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Voucher code has expired");
        }

        SpecialOffer specialOffer = specialOfferRepository.findById(voucherCode.getSpecialOfferId())
                .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND));

        LocalDateTime usageDate = LocalDateTime.now();
        voucherCode.setUsageDate(usageDate);
        voucherCodeRepository.save(voucherCode);

        return Map.of(
            "discount", specialOffer.getDiscountPercentage(),
            "offerName", specialOffer.getName(),
            "expirationDate", voucherCode.getExpirationDate(),
            "usageDate", usageDate
        );
    }

    public List<String> getValidVoucherCodes(String email) {
        Recipient recipient = recipientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(RECIPIENT_NOT_FOUND));

        List<VoucherCode> validVoucherCodes = voucherCodeRepository.findByRecipientIdAndExpirationDateAfterAndUsageDateIsNull(recipient.getId(), LocalDate.now());
        return validVoucherCodes.stream()
                .map(vc -> {
                    SpecialOffer specialOffer = specialOfferRepository.findById(vc.getSpecialOfferId())
                            .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND));
                    return vc.getCode() + " - " + specialOffer.getName();
                })
                .collect(Collectors.toList());
    }

    private String generateUniqueCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code;
        do {
            code = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                code.append(characters.charAt(random.nextInt(characters.length())));
            }
        } while (voucherCodeRepository.findByCode(code.toString()).isPresent());
        return code.toString();
    }

    public SpecialOffer updateSpecialOfferDiscount(String specialOfferId, double newDiscountPercentage) {
        SpecialOffer specialOffer = specialOfferRepository.findById(specialOfferId)
                .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND));
        specialOffer.setDiscountPercentage(newDiscountPercentage);
        return specialOfferRepository.save(specialOffer);
    }
}
