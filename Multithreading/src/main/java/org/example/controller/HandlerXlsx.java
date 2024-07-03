package org.example.controller;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class HandlerXlsx {


    public  void xls2tsv(String inputFilePath,String outputFilePath) {
//        inputFilePath = "C:\\Users\\panyu_202101\\Desktop\\data.xlsx";
//        outputFilePath = "C:\\Users\\panyu_202101\\Desktop\\data2.csv";
        try {
            convertExcelToCsv(inputFilePath, outputFilePath);
            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
        }
    }

    public static void convertExcelToCsv(String excelFilePath, String tsvFilePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(fis), 100);
             FileWriter writer = new FileWriter(tsvFilePath);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            // 选择第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            for (Row row : sheet) {
                StringBuilder rowString = new StringBuilder();
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    rowString.append(cellValue).append("\t");
                }
                // 移除最后一个制表符
                if (rowString.length() > 0) {
                    rowString.setLength(rowString.length() - 1);
                }
                bufferedWriter.write(rowString.toString());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
