package com.inandi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import javax.servlet.http.HttpServletRequest;

@RestController
public class NotificationController {

    @Autowired
    private NotificationSubscriber notificationSubscriber;

    @GetMapping(path = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux(HttpServletRequest httpServletRequest) {
        return notificationSubscriber.notifications();
    }
}
