package org.example.main;

import org.example.controller.HandlerCsv;
import org.example.controller.HandlerXlsx;
import org.example.rw.PreProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.example.controller.noThreadPool.*;

public class Main {
    public static final Integer numArgs = 4;

    //处理模式：常规和非常规
    public static void directExe(String isPrePro,String regex,String afterRegex,String outputFilePath,String inputFilePathName) throws IOException, InterruptedException {
        String outputFilePathName = outputFileFormat(inputFilePathName,outputFilePath);

        if(inputFilePathName.endsWith("csv")){
            //处理csv文件
            HandlerCsv.handlerCSV(inputFilePathName,outputFilePathName,isPrePro);
//            System.out.println("csv处理完成");
        }
        else if(inputFilePathName.endsWith("xlsx")){
            HandlerXlsx.convertExcelToCsv(inputFilePathName,outputFilePathName);
            System.out.println("xlsx处理完成");
        }
        else if(inputFilePathName.endsWith("xls")){
            HandlerXlsx.convertExcelToCsv(inputFilePathName,outputFilePathName);
            System.out.println("xls处理完成");
        }
        else if (inputFilePathName.endsWith("json")) {
            //用python实现
        }
        else if (inputFilePathName.endsWith("sql")) {
            //导入数据库中，执行sql语句即可
            //后续用dag流图处理
        }

    }

    static void traverseDir(File directory,String outputFilePath,String regex,String mode,String isPrePro) throws IOException, InterruptedException {
        // 获取目录中的所有文件和子目录
        File[] files = directory.listFiles();

        // 遍历文件和子目录
        for (File file : files) {
            // 如果是文件，则打印文件路径
            if (file.isFile()) {
                directExe(mode,regex,isPrePro,outputFilePath,file.getAbsolutePath());
            }
            // 如果是目录，则递归遍历子目录
            else if (file.isDirectory()) {
                System.out.println("Directory: " + file.getAbsolutePath());
                traverseDirectory(file,outputFilePath,regex,mode,isPrePro); // 递归调用遍历子目录
            }
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        String afterRegex = "1";
        String regex = ",";
        String isPrePro = "\"";
        String outputFilePath ="D:\\IDA-workspace\\Multithreading\\Multithreading\\target\\";
        String testInputFilePath = "D:\\IDA-workspace\\Multithreading\\Multithreading\\singleline.csv";
        Map<Integer,String> inputMap =  new HashMap<>();

        try{
            isPrePro = args[0];
            regex = args[1];
            afterRegex = args[2];
            outputFilePath = args[3];
            for (int i = 0; i < args.length-numArgs; i++) {
                inputMap.put(i,args[numArgs+i]);
            }
        }
        catch (Exception e) {
            System.out.println("命令格式：java -jar Multithreading.jar <输入数据分割方式> <输入数据处理模式> <输入文件的绝对路径> <输出文件的绝对路径> ");
            //处理模式：常规和非常规
            System.out.println("输入数据处理模式,例如：0：字符串替换；1：常规处理；2：json;3:非常规：[1]=>xxx;[2]=>xxx;");
            //分割方式
            //System.out.println("输入数据分割方式，例如:输入1：以逗号分割,输入2：以点号分割，输入3：冒号，输入4：制表符，输入11：非双引号内的逗号，输入21：非数字内的逗号");
            System.out.println("输入数据分割方式或替换前字符,例如：, : ;");
            //字段包围符或替换后字符
            //System.out.println("输入数据分割方式，例如:输入1：以逗号分割,输入2：以点号分割，输入3：冒号，输入4：制表符，输入11：非双引号内的逗号，输入21：非数字内的逗号");
            System.out.println("输入字段包围符或替换后字符,例如：, : ;");

            //输出文件地址
            System.out.println("输出文件的绝对路径，例如：D:\\IDA-workspace\\Multithreading\\data\\");
            //输入文件地址
            System.out.println("输入文件的绝对路径，例如： D:\\IDA-workspace\\Multithreading\\data\\TSDM.txt");
            System.out.println(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        }

        //判断输入文件是否是文件夹
        boolean isDirtory = new File(inputMap.get(0)).isDirectory();
        long l = System.currentTimeMillis();
        //输入文件夹、输出文件夹、分割符、模式、替换前字符、替换后字符
        if (isDirtory) traverseDir(new File(inputMap.get(0)),outputFilePath,regex,afterRegex,isPrePro);
        else {
            for (int i = 0; i < args.length- numArgs; i++) {
                directExe(isPrePro,regex,afterRegex,outputFilePath,inputMap.get(i));
            }
        }
        System.out.println("文件总体处理完成，耗时："+(System.currentTimeMillis()-l)/1000+"秒");
    }
}
