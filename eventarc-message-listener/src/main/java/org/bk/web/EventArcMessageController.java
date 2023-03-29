package org.bk.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bk.model.PubSubBody;
import org.bk.service.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

@RestController
public class EventArcMessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventArcMessageController.class);
    private final ObjectMapper objectMapper;

    public EventArcMessageController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Mono<ResponseEntity<PubSubBody>> receiveMessage(
            @RequestBody PubSubBody body, @RequestHeader Map<String, String> headers) {
        String rawData = new String(Base64.getDecoder().decode(body.message().data()));
        LOGGER.info("Received message: {}, headers: {}", JsonUtils.writeValueAsString(body, objectMapper), headers);
        LOGGER.info("Payload: {}", rawData);
        return Mono.just(ResponseEntity.ok(body));
    }
}
