package org.bk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Processes messages coming in from Pub/Sub topic
 */
@Component
public class MessageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
    private final PubSubService pubSubService;

    public MessageProcessor(PubSubService pubSubService) {
        this.pubSubService = pubSubService;
    }

    @PostConstruct
    void process() {
        pubSubService.retrieve()
                .doOnNext(message -> {
                    LOGGER.info("Processing message: {}", message.toString());
                })
                .subscribe();
        LOGGER.info("Triggered processing of messages");
    }
}
