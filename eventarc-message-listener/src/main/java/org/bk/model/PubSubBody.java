package org.bk.model;

public record PubSubBody(String subscription, PubSubMessage message) {
}
