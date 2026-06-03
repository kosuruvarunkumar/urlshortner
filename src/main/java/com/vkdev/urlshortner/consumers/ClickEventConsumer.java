package com.vkdev.urlshortner.consumers;

import com.vkdev.urlshortner.models.ClickEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClickEventConsumer {
    @KafkaListener(
            topics = "click-events",
            groupId = "click-analytics-v1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(List<ConsumerRecord<String, ClickEvent>> clickEvents, Acknowledgment acknowledgment) {
        try {
            List<ClickEvent> events = clickEvents.stream().map(ConsumerRecord::value)
                    .filter(Objects::nonNull)
                    .toList();
            if (!events.isEmpty()) {
                //add to click house repo
            }

            acknowledgment.acknowledge();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

