package org.bk.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pubsub")
public record PubSubProperties(String topic, String subscriberId) {
}
