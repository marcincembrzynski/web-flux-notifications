package com.inandi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inandi.model.Notification;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void shouldGetNotifications() throws InterruptedException, JsonProcessingException {

        Flux<String> actual = WebClient.create().get().uri(URI.create("http://localhost:" + port + "/notifications"))
                .header("authentication", "token1")
                .retrieve().bodyToFlux(String.class);
        stringRedisTemplate.convertAndSend("notifications", new ObjectMapper().writeValueAsString(new Notification("token1", "foo")));
        actual.subscribe(e -> System.out.println("#### " + e));
    }
}
