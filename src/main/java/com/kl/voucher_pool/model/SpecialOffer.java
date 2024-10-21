package com.kl.voucher_pool.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a special offer in the voucher pool system.
 * This class is mapped to the "special_offers" collection in the MongoDB database.
 * 
 * A special offer contains information about a specific promotion or discount,
 * including its name and the percentage of discount offered.
 *
 * @author [Kuok Lim Goh]
 * @since [2024-10-21]
 */
@Document(collection = "special_offers")
public class SpecialOffer {
    @Id
    private String id;
    private String name;
    private double discountPercentage;

    public SpecialOffer(String name, double discountPercentage) {
        this.name = name;
        this.discountPercentage = discountPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}