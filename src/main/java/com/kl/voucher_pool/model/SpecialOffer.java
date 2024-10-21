package com.kl.voucher_pool.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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