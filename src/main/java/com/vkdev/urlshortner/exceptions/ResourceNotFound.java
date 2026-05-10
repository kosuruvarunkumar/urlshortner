package com.vkdev.urlshortner.exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound() {
        super("URL with provided shortCode not found");
    }
}
