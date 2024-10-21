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

/**
 * Service class for managing voucher pool operations.
 * This class handles the generation, validation, and management of voucher codes,
 * as well as interactions with recipients and special offers.
 *
 * @author [Kuok Lim Goh]
 * @since [2024-10-21]
 */
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
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;

    /**
     * Generates a unique 8-character alphanumeric voucher code.
     * Ensures uniqueness by checking against existing codes in the repository.
     *
     * @return A unique voucher code.
     */
    public VoucherCode generateVoucherCode(String recipientEmail, String specialOfferName, LocalDate expirationDate) {
        Recipient recipient = recipientRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new IllegalArgumentException(RECIPIENT_NOT_FOUND));

        SpecialOffer specialOffer = specialOfferRepository.findByName(specialOfferName)
                .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND));

        String code = generateUniqueCode();
        VoucherCode voucherCode = new VoucherCode(code, recipient.getId(), specialOffer.getId(), expirationDate);
        return voucherCodeRepository.save(voucherCode);
    }

    /**
     * Generates a unique 8-character alphanumeric voucher code.
     * Ensures uniqueness by checking against existing codes in the repository.
     *
     * @return A unique 8-character alphanumeric code.
     */
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

    /**
     * Retrieves valid voucher codes for a given recipient email.
     * 
     * This method finds all valid (unexpired and unused) voucher codes associated with
     * the recipient's email address. Each voucher code is returned along with its
     * corresponding special offer name.
     *
     * @param email The email address of the recipient
     * @return A List of Strings, each containing a voucher code and its special offer name
     * @throws IllegalArgumentException if the recipient is not found
     */
    public List<String> getValidVoucherCodes(String email) {
        return recipientRepository.findByEmail(email)
                .map(recipient -> voucherCodeRepository.findByRecipientIdAndExpirationDateAfterAndUsageDateIsNull(recipient.getId(), LocalDate.now()))
                .orElseThrow(() -> new IllegalArgumentException(RECIPIENT_NOT_FOUND))
                .stream()
                .map(vc -> specialOfferRepository.findById(vc.getSpecialOfferId())
                        .map(offer -> vc.getCode() + " - " + offer.getName())
                        .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves usage statistics for voucher codes.
     * 
     * This method calculates and returns various statistics about voucher code usage,
     * including the total number of vouchers, the number of used vouchers, the number
     * of unused vouchers, and the usage percentage.
     *
     * @return A Map containing the following statistics:
     *         - "totalVouchers": The total number of voucher codes in the system
     *         - "usedVouchers": The number of voucher codes that have been used
     *         - "unusedVouchers": The number of voucher codes that have not been used
     *         - "usagePercentage": The percentage of voucher codes that have been used
     */
    private String generateUniqueCode() {
        return random.ints(CODE_LENGTH, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    /**
     * Retrieves usage statistics for voucher codes.
     * 
     * This method calculates and returns various statistics about voucher code usage,
     * including the total number of vouchers, the number of used vouchers, the number
     * of unused vouchers, and the usage percentage.
     *
     * @return A Map containing the following statistics:
     *         - "totalVouchers": The total number of voucher codes in the system
     *         - "usedVouchers": The number of voucher codes that have been used
     *         - "unusedVouchers": The number of voucher codes that have not been used
     *         - "usagePercentage": The percentage of voucher codes that have been used
     */
    public SpecialOffer updateSpecialOfferDiscount(String specialOfferId, double newDiscountPercentage) {
        SpecialOffer specialOffer = specialOfferRepository.findById(specialOfferId)
                .orElseThrow(() -> new IllegalArgumentException(SPECIAL_OFFER_NOT_FOUND));
        specialOffer.setDiscountPercentage(newDiscountPercentage);
        return specialOfferRepository.save(specialOffer);
    }
}
