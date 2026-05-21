package com.vkdev.urlshortner.controllers;

import com.vkdev.urlshortner.dtos.ShortCodeRequestDTO;
import com.vkdev.urlshortner.models.URLEntity;
import com.vkdev.urlshortner.services.ClickEventService;
import com.vkdev.urlshortner.services.URLService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/api")
public class URLController {
    private final URLService urlService;

    private final ClickEventService eventService;

    @Autowired
    public URLController(URLService urlService, ClickEventService eventService) {
        this.urlService = urlService;
        this.eventService = eventService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getURLFromShortCode(@PathVariable String shortCode, HttpServletRequest request) {
        String newLocation = urlService.getURLFromShortCode(shortCode);
//        eventService.publishClickEvent(shortCode, request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", newLocation);

        return new ResponseEntity<>(headers, HttpStatus.TEMPORARY_REDIRECT);
    }

    @PostMapping
    public ResponseEntity<URLEntity> createShortCode(@RequestBody ShortCodeRequestDTO requestBody) {
        return new ResponseEntity<>(urlService.createShortCode(requestBody), HttpStatusCode.valueOf(201));

    }
}
