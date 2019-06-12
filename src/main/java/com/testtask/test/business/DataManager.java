package com.testtask.test.business;

import org.apache.commons.collections.set.SynchronizedSet;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataManager {
    public static void execute(String[] args) {
        List<String> fileNames = Arrays.asList(args);
        Set<String> usedFileNames = Collections.synchronizedSet(new HashSet<>(fileNames));
        BlockingQueue<Map<String, Object>> queue = new LinkedBlockingQueue<>();
        for (String f:
             fileNames) {
            (new Thread(new DataReader(f,queue))).start();
        }
        Thread processThread =  new Thread(new DataProcessor(queue, usedFileNames));
        processThread.start();
        try {
            processThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }


    }
}
