package com.testtask.test.parsers;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CsvParser implements Iterator<Map<String, Object>> {

    private CSVReader csvReader;
    private CSVIterator csvIterator;
    private long lineNumber = 1;

    public CsvParser(String fileName) {
        try {
            csvReader = new CSVReader(new FileReader(fileName));
            csvIterator = (CSVIterator)csvReader.iterator();
        } catch (IOException ex) {
            csvReader = null;
        }

    }

    @Override
    public boolean hasNext() {
        return (csvReader != null) && csvIterator.hasNext();
    }

    @Override
    public Map<String, Object> next() {
        if (csvReader == null)
            return null;
        String[] line = csvIterator.next();
        try {
            if (!csvIterator.hasNext()) {
                csvReader.close();
                csvReader = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("line", lineNumber++);
        try {
            result.put("orderId", Integer.valueOf(line[0].replaceAll("\\uFEFF", "").trim()));
        } catch (NumberFormatException ex) {
            result.put("orderId", null);
            result.putIfAbsent("result", "OrderId is not a valid number string: " + line[0]);
        }

        try {
            result.put("amount", new BigDecimal(line[1].replaceAll("\\uFEFF", "").trim()));
        } catch (NumberFormatException ex) {
            result.put("amount", null);
            result.putIfAbsent("result", "Amount is not a valid number string: " + line[1]);
        }

        result.put("currency", line[2]);
        result.put("comment", line[3]);
        result.putIfAbsent("result","OK");
        return result;
    }
}
