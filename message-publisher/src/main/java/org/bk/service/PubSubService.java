package org.bk.service;

import org.bk.model.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Responsible for publishing and retrieving content from a Cloud Pub/Sub topic
 */
public interface PubSubService {
    Mono<Void> publish(Message message);

    Flux<Message> retrieve();
}
