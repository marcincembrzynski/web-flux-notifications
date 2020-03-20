package com.inandi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inandi.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Service
public class NotificationSubscriber implements MessageListener {

    private final FluxSinkConsumer fluxSinkConsumer;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationSubscriber(FluxSinkConsumer fluxSinkConsumer) {
        this.fluxSinkConsumer = fluxSinkConsumer;
        this.objectMapper = new ObjectMapper();
    }

    public Flux<String> notifications(){
        return Flux.create(fluxSinkConsumer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        deserialize(message.toString()).ifPresent(e -> fluxSinkConsumer.publishEvent(e));
    }

    private Optional<Notification> deserialize(String value){
        try {
            return Optional.of(objectMapper.readValue(value, Notification.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
