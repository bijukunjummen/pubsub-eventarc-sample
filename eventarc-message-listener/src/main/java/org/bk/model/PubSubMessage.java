package org.bk.model;

import java.util.Map;

public record PubSubMessage(String data, Map<String, String> attributes, String messageId, String publishTime) {
}
