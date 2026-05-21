package com.vkdev.urlshortner.producers;

import com.vkdev.urlshortner.models.ClickEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClickEventProducer {
    private final KafkaTemplate<String, ClickEvent> kafkaTemplate;

    private static final String TOPIC = "click-events";

    public void clickEvent(ClickEvent clickEvent) {
        try{
            kafkaTemplate.send(TOPIC, clickEvent);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send click event", e);
        }
    }
}
