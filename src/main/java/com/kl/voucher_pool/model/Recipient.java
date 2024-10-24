package com.kl.voucher_pool.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Represents a recipient in the voucher pool system.
 * This class is mapped to the "recipients" collection in the MongoDB database.
 * 
 * @author [Kuok Lim Goh]
 * @since [2024-10-21]
 */
@Document(collection = "recipients")
public class Recipient {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;

    public Recipient(String email, String name) {
        this.email = email;
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}