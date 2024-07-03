package org.example.controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.StringUtils;
import org.example.rw.PreProcessing;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerCsv {

    public static void handlerCSV(String fileInputPathName,String fileOutputPathName,String isPreProcessing)  {
        //是否预处理
        if (isPreProcessing.equals("true")) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPathName))){
                String s = bufferedReader.readLine();
                //字段中是否含有双引号
                boolean contains = s.contains("\"");
                String tempFilePathName = "";
                if (contains) {
                    tempFilePathName = fileInputPathName+".csv";
                    PreProcessing.seperateRW(fileInputPathName,tempFilePathName);
                    fileInputPathName = tempFilePathName;
                }
            }catch (IOException e){
                throw new RuntimeException("文件不存在" + fileInputPathName);
            } catch (InterruptedException e) {
                throw new RuntimeException("文件读取异常" + fileInputPathName);
            }
            //预处理
        }

        int i = 0;
        String[] lineInArray = new String[0];
        try (
                CSVReader reader = new CSVReader(new FileReader(fileInputPathName));
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutputPathName));
        ) {
            long l = System.currentTimeMillis();
            while ((lineInArray = reader.readNext()) != null) {
                if (i!=0 && i%100000 == 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("当前进度：已处理了").append(i / 100000).append("0w行数据").append(",已用时").append((System.currentTimeMillis() - l) / 1000).append("秒");
                    System.out.println(stringBuilder.toString());
                }
                i++;
                List<String> collect = Arrays.stream(lineInArray).map(string -> {
                    if (StringUtils.isBlank(string))
                        return "''";
                    else return string;
                }).collect(Collectors.toList());
                String join = StringUtils.join(collect, "\t")+"\r\n";
                writer.write(join);
            }
            System.out.println("文件处理完毕！");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException | CsvException e) {
            System.out.println("运行时出现异常，第"+(i+1)+"行中的数据存在异常，请手动查验，上一行数据为："+Arrays.stream(lineInArray).collect(Collectors.toList()));
            throw new RuntimeException(e);
        }
    }
    public static String outputFileFormat(String inputFilePath ,String outputFilePath){
        //将文件名保存，合并到输出文件路径
        String[] tmp = inputFilePath.split("\\\\");
        if (outputFilePath.charAt(outputFilePath.length()-1) != '\\' ) outputFilePath +='\\';
        outputFilePath += tmp[tmp.length-1]+".tsv";
        return outputFilePath;
    }


    public static void main(String[] args) {
        //非输入文件的参数个数
        int numArgs = 2;
        String isPreProcessing= "false";
        HandlerCsv openCsvHandler = new HandlerCsv();
        Map<Integer,String> inputMap = new HashMap<>();
        String outputMap ="";
        try{
            isPreProcessing = args[0];
            outputMap = args[1];
            for (int i = 0; i < args.length-numArgs; i++) {
                 inputMap.put(i,args[numArgs+i]);
            }
        }
        catch (Exception e) {
            outputMap = "D:\\IDA-workspace\\Multithreading\\Multithreading\\target\\test.csv.tsv";
            String input = "D:\\IDA-workspace\\Multithreading\\Multithreading\\target\\test.csv";
            e.printStackTrace();
        }
        for (int i = 0; i < args.length-1; i++) {
            openCsvHandler.handlerCSV(inputMap.get(i),outputFileFormat(inputMap.get(i),outputMap),isPreProcessing);
        }

    }
}
