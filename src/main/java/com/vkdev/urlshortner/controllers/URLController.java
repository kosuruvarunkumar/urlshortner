package com.vkdev.urlshortner.controllers;

import com.vkdev.urlshortner.dtos.ShortCodeRequestDTO;
import com.vkdev.urlshortner.models.URLEntity;
import com.vkdev.urlshortner.services.URLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/api")
public class URLController {
    
    @Autowired
    URLService urlService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getURLFromShortCode(@PathVariable String shortCode) {
        String newLocation = urlService.getURLFromShortCode(shortCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", newLocation);

        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @PostMapping
    public ResponseEntity<URLEntity> createShortCode(@RequestBody ShortCodeRequestDTO requestBody) {
        return new ResponseEntity<>(urlService.createShortCode(requestBody), HttpStatusCode.valueOf(201));

    }
}
