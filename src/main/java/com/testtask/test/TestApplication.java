package com.testtask.test;

import com.testtask.test.business.DataManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.PrintStream;

@SpringBootApplication
public class TestApplication implements CommandLineRunner {


    @Override
    public void run(String[] args) throws Exception {
        System.setErr(new PrintStream("errors.log"));
        DataManager.execute(args);

    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
