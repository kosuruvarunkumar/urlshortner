package com.vkdev.urlshortner.services;

import com.vkdev.urlshortner.models.ClickEvent;
import com.vkdev.urlshortner.producers.ClickEventProducer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ClickEventService {
    private final ClickEventProducer clickEventProducer;

    public ClickEventService(ClickEventProducer clickEventProducer) {
        this.clickEventProducer = clickEventProducer;
    }

    public void publishClickEvent(String code, HttpServletRequest request) {
        ClickEvent clickEvent = new ClickEvent(
                code,
                request.getHeader("User-Agent"),
                System.currentTimeMillis()
        );
        clickEventProducer.clickEvent(clickEvent);
    }
}
