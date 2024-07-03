package org.example.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;

public class ExcelToCsvConverter {

    public static void main(String[] args) {


        String inputFilePath = "C:\\Users\\panyu_202101\\Desktop\\data.xlsx";
        String outputFilePath = "C:\\Users\\panyu_202101\\Desktop\\data2.csv";

        try {
            convertExcelToCsv(inputFilePath, outputFilePath);
            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
        }
    }

    public static void convertExcelToCsv(String excelFilePath, String csvFilePath) throws IOException {
        Workbook workbook = null;
        try (FileInputStream fis = new FileInputStream(excelFilePath)) {
            if (excelFilePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (excelFilePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("The specified file is not Excel file");
            }

            DataFormatter dataFormatter = new DataFormatter();
            try (PrintWriter writer = new PrintWriter(new File(csvFilePath))) {
                Sheet sheet = workbook.getSheetAt(0); // Convert only the first sheet
                for (Row row : sheet) {
                    boolean firstCell = true;
                    for (Cell cell : row) {
                        if (!firstCell) writer.print(",");
                        String cellValue = dataFormatter.formatCellValue(cell);
                        StringBuilder cellValueBuilder = new StringBuilder();
                        cellValueBuilder.append("\"").append(cellValue).append("\"");
                        writer.print(cellValueBuilder.toString());
                        firstCell = false;
                    }
                    writer.println();
                }
            }
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }
}
