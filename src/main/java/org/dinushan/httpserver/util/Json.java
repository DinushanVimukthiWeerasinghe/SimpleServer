package org.dinushan.httpserver.util;

import com.fasterxml.jackson.databind.*;

public class Json {
    //ObjectMapper is used to convert the object to json and vice versa
    private static ObjectMapper objectMapper=defaultObjectMapper();

    //Default ObjectMapper is created with the default configuration
    private static ObjectMapper defaultObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    //Converts the object to json string

    public static JsonNode parse(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Converts the json string to object

    public static <A> A fromJSON(JsonNode node, Class<A> type) {
        try {
            return objectMapper.treeToValue(node, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //Converts the object to json string and returns the json string
    public static JsonNode toJSON(Object object) {
        try {
            return objectMapper.valueToTree(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //Converts the object to json string and returns the json string
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
    // Method to convert the object to json string and Stringify the json string
    public static String Stringify(JsonNode node) {
        return generateJSON(node,false);
    }
    // Method to convert the object to json string and Stringify the json string
    public static String StringifyPretty(JsonNode node) {
        return generateJSON(node,true);
    }

}
