package com.vkdev.urlshortner.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "urls")
public class URLEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(name="url")
    private String url;

    @Column(name="short_code")
    private String shortCode;

    @Column(name="expiry_date")
    private Date expiryDate;
}
