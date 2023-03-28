package org.bk.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.PubSubAdmin;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.reactive.PubSubReactiveFactory;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.Topic;
import org.bk.model.Message;
import org.bk.model.PubSubProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class CloudPubSubService implements PubSubService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloudPubSubService.class);

    private final PubSubTemplate pubSubTemplate;
    private final PubSubAdmin pubSubAdmin;
    private final PubSubProperties pubSubProperties;
    private final ObjectMapper objectMapper;
    private final PubSubReactiveFactory pubSubReactiveFactory;

    public CloudPubSubService(PubSubTemplate pubSubTemplate,
                              PubSubAdmin pubSubAdmin, PubSubProperties pubSubProperties,
                              PubSubReactiveFactory pubSubReactiveFactory,
                              ObjectMapper objectMapper) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubAdmin = pubSubAdmin;
        this.pubSubProperties = pubSubProperties;
        this.pubSubReactiveFactory = pubSubReactiveFactory;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> publish(Message message) {
        ByteString data = ByteString.copyFromUtf8(JsonUtils.writeValueAsString(message, objectMapper));
        PubsubMessage pubSubMessage = PubsubMessage.newBuilder().setData(data).build();
        return Mono.fromFuture(pubSubTemplate.publish(pubSubProperties.topic(), pubSubMessage)).then();
    }

    @Override
    public Flux<Message> retrieve() {
        return pubSubReactiveFactory
                .poll(pubSubProperties.subscriberId(), 1000L)
                .map(ackMessage -> {
                    String rawData = ackMessage.getPubsubMessage().getData().toStringUtf8();
                    ackMessage.ack();
                    return JsonUtils.readValue(rawData, Message.class, objectMapper);
                });
    }

    @PostConstruct
    void init() {
        Topic topic = pubSubAdmin.getTopic(pubSubProperties.topic());
        if (topic == null) {
            topic = pubSubAdmin.createTopic(pubSubProperties.topic());
        }

        Subscription subscription = pubSubAdmin.getSubscription(pubSubProperties.subscriberId());
        if (subscription == null) {
            subscription = pubSubAdmin.createSubscription(pubSubProperties.subscriberId(), topic.getName());
        }
        LOGGER.info("Topic {} and subscription {} is in place", topic.getName(), subscription.getName());
    }
}
