package com.example;

// JsonToUrlEncodedConverter/src/JsonUrlEncoder.java

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class JsonUrlEncoder {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Encodes a JSON string into application/x-www-form-urlencoded format, preserving parameter order.
     *
     * @param jsonString The JSON string to encode.
     * @return The URL-encoded string.
     * @throws JsonProcessingException      If JSON parsing fails.
     * @throws UnsupportedEncodingException If UTF-8 encoding is not supported.
     */
    public static String encode(String jsonString) throws JsonProcessingException, UnsupportedEncodingException {
        JsonNode rootNode = mapper.readTree(jsonString);
        StringBuilder result = new StringBuilder();
        encodeNode(rootNode, null, result);
        return result.toString();
    }

    /**
     * Recursively encodes a JsonNode into a URL-encoded string.
     *
     * @param node      The current JsonNode to encode.
     * @param parentKey The parent key for nested objects. Pass null for top-level.
     * @param result    The StringBuilder accumulating the URL-encoded string.
     * @throws UnsupportedEncodingException If UTF-8 encoding is not supported.
     */
    private static void encodeNode(JsonNode node, String parentKey, StringBuilder result) throws UnsupportedEncodingException {
        if (node.isObject()) {
            Iterator<String> fieldNames = node.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode childNode = node.get(fieldName);
                String encodedKey = parentKey != null
                        ? parentKey + "[" + URLEncoder.encode(fieldName, "UTF-8") + "]"
                        : URLEncoder.encode(fieldName, "UTF-8");
                encodeNode(childNode, encodedKey, result);
            }
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode arrayElement : arrayNode) {
                String arrayKey = parentKey + "[]";
                encodeNode(arrayElement, arrayKey, result);
            }
        } else {
            // Primitive value
            String encodedValue = node.isNull() ? "" : URLEncoder.encode(node.asText(), "UTF-8");
            if (result.length() > 0) {
                result.append("&");
            }
            result.append(parentKey).append("=").append(encodedValue);
        }
    }
}