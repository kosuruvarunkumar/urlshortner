package com.vkdev.urlshortner.models;

public record ClickEvent (
        String code,
        String userAgent,
        long timeStampMs
){}
