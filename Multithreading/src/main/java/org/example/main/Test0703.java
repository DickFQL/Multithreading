package org.example.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test0703 {

    public static void main(String[] args) {
        String pathName = "D:\\IDA-workspace\\Multithreading\\Multithreading\\target\\test0703.csv";
        try {
            String s = new BufferedReader(new FileReader(pathName)).readLine();
            System.out.println(s);
            String[] split = s.split("\"");
            for (String s1 : split) {
                System.out.println(s1);
            }
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        System.out.println("Hello World!");
    }


}
