package com.vkdev.urlshortner.services;

import com.vkdev.urlshortner.dtos.ShortCodeRequestDTO;
import com.vkdev.urlshortner.models.URLEntity;
import com.vkdev.urlshortner.repositories.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class URLService {
    @Autowired
    private URLRepository urlRepository;

    public URLEntity getURLFromShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode).getFirst();
    }

    public URLEntity createShortCode(ShortCodeRequestDTO requestDTO) {
        URLEntity entity = new URLEntity();
        String shortCode = requestDTO.getAlias();

        entity.setUrl(requestDTO.getUrl());
        if(shortCode == null)
            shortCode = generateShortCode(requestDTO.getUrl());

        entity.setShortCode(shortCode);
        entity.setExpiryDate(requestDTO.getExpiryDate());
        
        return urlRepository.save(entity);
    }

    private String generateShortCode(String url) {
        StringBuilder hexString = new StringBuilder();
        try {
            Date date = new Date();
            url = url + date;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(url.getBytes(StandardCharsets.UTF_8));
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return hexString.substring(0, 8);
    }
}


