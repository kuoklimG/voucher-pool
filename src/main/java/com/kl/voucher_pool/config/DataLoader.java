package com.kl.voucher_pool.config;

import com.kl.voucher_pool.model.Recipient;
import com.kl.voucher_pool.model.SpecialOffer;
import com.kl.voucher_pool.repository.RecipientRepository;
import com.kl.voucher_pool.repository.SpecialOfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private final RecipientRepository recipientRepository;
    private final SpecialOfferRepository specialOfferRepository;

    public DataLoader(RecipientRepository recipientRepository, SpecialOfferRepository specialOfferRepository) {
        this.recipientRepository = recipientRepository;
        this.specialOfferRepository = specialOfferRepository;
    }

    @Override
    public void run(String... args) {
        loadRecipients();
        loadSpecialOffers();
    }

    private void loadRecipients() {
        logger.info("Checking if recipients need to be loaded...");
        if (recipientRepository.count() == 0) {
            List<Recipient> recipients = Arrays.asList(
                new Recipient("john@example.com", "John Doe"),
                new Recipient("jane@example.com", "Jane Smith"),
                new Recipient("bob@example.com", "Bob Johnson")
            );
            List<Recipient> savedRecipients = recipientRepository.saveAll(recipients);
            logger.info("Sample recipients loaded. Count: {}", savedRecipients.size());
        } else {
            logger.info("Recipients already exist in the database. Count: {}", recipientRepository.count());
        }
    }

    private void loadSpecialOffers() {
        logger.info("Checking if special offers need to be loaded...");
        if (specialOfferRepository.count() == 0) {
            List<SpecialOffer> specialOffers = Arrays.asList(
                new SpecialOffer("Summer Sale", 20.0),
                new SpecialOffer("Winter Discount", 15.0),
                new SpecialOffer("Spring Promotion", 10.0)
            );
            List<SpecialOffer> savedOffers = specialOfferRepository.saveAll(specialOffers);
            logger.info("Sample special offers loaded. Count: {}", savedOffers.size());
        } else {
            logger.info("Special offers already exist in the database. Count: {}", specialOfferRepository.count());
        }
    }
}
