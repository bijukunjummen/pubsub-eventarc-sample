package org.bk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bk.model.Message;

public class JsonUtils {
    public static <T> String writeValueAsString(T obj, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Message readValue(String rawData, Class<Message> clazz, ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(rawData, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
