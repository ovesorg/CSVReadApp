package com.sparkle.csvreadapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVReader1 {

    InputStream inputStream;

    public CSVReader1(InputStream is){
        this.inputStream = is;
    }

    public List<String[]> read(){

//        InputStream os = getContentResolver().openInputStream(uri);
//        BufferedReader writer = new BufferedReader(new InputStreamReader(os, StandardCharsets.UTF_8));

        List<String[]> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


            try {
                String csvLine;
                while ((csvLine = reader.readLine()) != null){
                    String[] row = csvLine.split(",");
                    resultList.add(row);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultList;


    }
}
