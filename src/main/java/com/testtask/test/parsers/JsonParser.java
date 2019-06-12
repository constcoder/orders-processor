package com.testtask.test.parsers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class JsonParser implements Iterator<Map<String, Object>> {

    private ObjectMapper objectMapper;
    private long lineNumber = 1;
    private ArrayNode arrayNode;
    private Iterator<JsonNode> node;
    private JsonFactory factory;
    private com.fasterxml.jackson.core.JsonParser parser;


    public JsonParser(String fileName) {
        try {
            objectMapper = new ObjectMapper();
            factory = objectMapper.getFactory();
            parser = factory.createParser(new File(fileName));
            arrayNode = objectMapper.readTree(parser);
            node = arrayNode.elements();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public boolean hasNext() {
        return (parser != null) && node.hasNext();
    }

    @Override
    public Map<String, Object> next() {
        if (parser == null) {
            return null;
        }
        JsonNode currentNode = node.next();
        Map<String, Object> result = new HashMap<>();
        result.put("line", lineNumber++);
        Iterator<Map.Entry<String, JsonNode>> fields = currentNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (field.getKey().equals("orderId")) {
                if (field.getValue().isInt()) {
                    result.put("orderId", field.getValue());
                }  else {
                    result.put("orderId", null);
                    result.putIfAbsent("result", "OrderId is not a valid number string: " + String.valueOf(field.getValue()));
                }

            } else if (field.getKey().equals("amount")) {
                if (field.getValue().isDouble() || field.getValue().isInt()) {
                    result.put("amount", field.getValue());
                } else {
                    result.put("amount", null);
                    result.putIfAbsent("result", "Amount is not a valid number string: " + String.valueOf(field.getValue()));
                }
            } else if (field.getKey().equals("currency")) {
                result.put("currency", field.getValue());
            } else if (field.getKey().equals("comment")) {
                result.put("comments", field.getValue());
            }
        }
        if (!node.hasNext()) {
            try {
                parser.close();
                parser = null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        result.putIfAbsent("result","OK");
        return result;
    }

}
