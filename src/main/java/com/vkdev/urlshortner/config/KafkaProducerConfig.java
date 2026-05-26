package com.vkdev.urlshortner.config;

import com.vkdev.urlshortner.models.ClickEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.acks}")
    private String acksConfig;
    @Value("${spring.kafka.producer.compression-type}")
    private String compressionType;
    @Value("${spring.kafka.producer.properties.linger.ms}")
    private Integer lingerMs;
    @Value("${spring.kafka.producer.properties.batch.size}")
    private String batchSize;
    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private String maxInFlightRequestsPerConnection;
    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private String idempotenceEnabled;
    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private Integer deliveryTimeoutMs;
    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private Integer requestTimeoutMs;
    @Value("${spring.kafka.producer.properties.buffer.memory}")
    private Long bufferMemory;

    @Bean
    public ProducerFactory<String, ClickEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, acksConfig);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotenceEnabled);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, ClickEvent> kafkaTemplate(
            ProducerFactory<String, ClickEvent> factory) {
        return new KafkaTemplate<>(factory);
    }
}
