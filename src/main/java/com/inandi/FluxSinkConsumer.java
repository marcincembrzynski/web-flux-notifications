package com.inandi;

import com.inandi.model.Notification;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Log
@Component
public class FluxSinkConsumer implements Consumer<FluxSink<String>> {

    private List<Pair<FluxSink<String>, String>> fluxSinks = new ArrayList<>();

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Override
    public void accept(FluxSink<String> fluxSink) {
        String token = httpServletRequest.getHeader("authentication");
        if(token != null) {
            fluxSinks.add(Pair.of(fluxSink, token));
        }
    }

    public void publishEvent(Notification notification){
        setClients();
        log.info("clients: " + this.fluxSinks.size());
        this.fluxSinks.stream().filter(e -> e.getSecond().equals(notification.getToken()))
                .forEach(e -> e.getFirst().next(notification.getMessage()));
    }

    private void setClients() {
        this.fluxSinks = this.fluxSinks.stream()
                .filter(fluxSinkStringPair -> !fluxSinkStringPair.getFirst().isCancelled())
                .collect(Collectors.toList());
        this.fluxSinks.forEach(e -> log.info(e.getFirst().toString()));
    }
}
