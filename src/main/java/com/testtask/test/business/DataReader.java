package com.testtask.test.business;

import com.testtask.test.parsers.CsvParser;
import com.testtask.test.parsers.JsonParser;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class DataReader implements Runnable {

    private String fileName;
    private Iterator<Map<String, Object>> fileParser;
    BlockingQueue<Map<String, Object>> queue;

    public DataReader(String fileName, BlockingQueue<Map<String, Object>> queue) {
        this.fileName = fileName;
        this.queue = queue;
        fileParser = createIterator(fileName);
    }


    protected static Iterator<Map<String , Object>> createIterator(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        String fullFIleName = System.getProperty("user.dir") + "\\" + fileName;
        switch (extension) {
            case "csv": {
                return new CsvParser(fullFIleName);
            }
            case "json": {
                return new JsonParser(fullFIleName);
            }
            default: {
                return null;
            }
        }
    }
    @Override
    public void run() {
        try {
            if (fileParser == null) {
                Map<String, Object> line = new HashMap<>();
                line.put("filename", fileName);
                line.put("result", "Could not read the file");
                queue.put(line);
            } else {
                while (fileParser.hasNext()) {
                    Map<String, Object> line = fileParser.next();
                    line.put("filename", fileName);
                    queue.put(line);
                }
            }
            Map<String, Object> eof = new HashMap<>();
            eof.put("eof", fileName);
            queue.put(eof);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}


