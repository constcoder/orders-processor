package com.testtask.test.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class DataProcessor implements Runnable {

    private BlockingQueue<Map<String, Object>> queue;
    private Set<String> usedFileNames;


    public DataProcessor(BlockingQueue<Map<String, Object>> queue, Set<String> usedFileNames) {
        this.queue = queue;
        this.usedFileNames = usedFileNames;
    }

    @Override
    public void run() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            while (!usedFileNames.isEmpty()) {
                Map<String, Object> line = queue.take();
                if (line.containsKey("eof")) {
                    usedFileNames.remove(line.get("eof"));
                } else {
                    line.put("id", line.get("orderId"));
                    line.remove("orderId");
                    String result = objectMapper.writeValueAsString(line);
                    System.out.println(result);
                }

            }
        } catch (InterruptedException | JsonProcessingException ex) {
            ex.printStackTrace();
        }

    }
}
