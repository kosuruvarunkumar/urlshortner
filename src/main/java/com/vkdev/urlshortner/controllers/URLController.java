package com.vkdev.urlshortner.controllers;

import com.vkdev.urlshortner.dtos.ShortCodeRequestDTO;
import com.vkdev.urlshortner.models.URLEntity;
import com.vkdev.urlshortner.services.URLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/api")
public class URLController {
    
    @Autowired
    URLService urlService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<URLEntity> getURLFromShortCode(@PathVariable String shortCode) {
        return new ResponseEntity<>(urlService.getURLFromShortCode(shortCode), HttpStatusCode.valueOf(200));
    }

    @PostMapping
    public ResponseEntity<URLEntity> createShortCode(@RequestBody ShortCodeRequestDTO requestBody) {
        return new ResponseEntity<>(urlService.createShortCode(requestBody), HttpStatusCode.valueOf(201));

    }
}
