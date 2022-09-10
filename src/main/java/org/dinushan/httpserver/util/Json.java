package org.dinushan.httpserver.util;

import com.fasterxml.jackson.databind.*;

public class Json {
    private static ObjectMapper objectMapper=defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static JsonNode parse(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <A> A fromJSON(JsonNode node, Class<A> type) {
        try {
            return objectMapper.treeToValue(node, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static JsonNode toJSON(Object object) {
        try {
            return objectMapper.valueToTree(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String generateJSON(Object object, boolean pretty) {
        ObjectWriter objectWriter = objectMapper.writer();
        if (pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        try {

            return objectWriter.writeValueAsString(object);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Stringify(JsonNode node) {
        return generateJSON(node,false);
    }
    public static String StringifyPretty(JsonNode node) {
        return generateJSON(node,true);
    }

}
