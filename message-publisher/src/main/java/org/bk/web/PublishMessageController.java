package org.bk.web;

import org.bk.model.Message;
import org.bk.service.PubSubService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/messages")
public class PublishMessageController {

    private final PubSubService pubSubService;

    public PublishMessageController(PubSubService pubSubService) {
        this.pubSubService = pubSubService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Message>> publishMessage(@RequestBody Message message) {
        return pubSubService.publish(message).thenReturn(ResponseEntity.ok(message));
    }
}
