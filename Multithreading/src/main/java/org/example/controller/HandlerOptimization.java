package org.example.controller;


//import cn.hutool.json.JSONUtil;
//import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
//import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

//@RestController
//@Slf4j
public class HandlerOptimization {

    @Resource
    ThreadPoolExecutor threadPoolExecutor;


    /**
     * 一行一行读入内存处理
     */
//    @GetMapping("/handlerNolimit")
    public void handNoQueueLimit(){
//        String inputFilePath = "G:\\已上传文件\\linkedin2023\\linkedin.csv";
//        String outputFilePath = "G:\\已上传文件\\linkedin2023\\linkedin.csv.tsv";
        String inputFilePath = "D:\\IDA-workspace\\DataHandler\\dataHandler\\src\\main\\resources\\test.csv";
        String outputFilePath = "D:\\IDA-workspace\\DataHandler\\dataHandler\\src\\main\\resources\\test.csv.tsv";
//java -jar dataHandler-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath),"UTF-8"))) {

            System.out.println("文件"+inputFilePath+"读取成功，正在处理");
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder processTime = new StringBuilder();
            long startTime = System.currentTimeMillis();
            System.out.println("Current Time: " + LocalDateTime.now());

            stringBuilder = stringBuilder.append(br.readLine()).append("\n");
            String s = StringUtils.join(stringBuilder.toString().replace("\"", "").split(","), "\t");
            bw.write(s);
            stringBuilder.setLength(0);
            //35945361
            for (int i = 1; i < 35945361; i++) {
                if (i%100000==0)  {

                    processTime.append("当前进度：").append(i).append("/35945361,已处理时间(毫秒):").append(System.currentTimeMillis()-startTime);
                    System.out.println(processTime.toString());
                    processTime.setLength(0);
                }
                stringBuilder = stringBuilder.append(br.readLine());
//                stringBuilder.charAt(stringBuilder.length() - 2) == '\"' && stringBuilder.charAt(stringBuilder.length() - 3) == ',';
//                stringBuilder.charAt(stringBuilder.length() - 2) == '\"' && stringBuilder.charAt(stringBuilder.length() - 3) == ','
                if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '\"'){
                    String strBuilderString = stringBuilder.toString();

                    CompletableFuture.runAsync(() ->{
                        try {
                            String replaced ;
                            String[] split ;
                            String joined;
                            String formatted;
                            replaced = strBuilderString.replace("\n", " ").replace("\r", " ");
//                    System.out.println("replaced结束"+"Current Time: " + LocalDateTime.now());
                            split = replaced.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                    System.out.println("split结束"+"Current Time: " + LocalDateTime.now());
                            joined = StringUtils.join(split, "\t")+"\n";
//                    System.out.println("joined结束"+"Current Time: " + LocalDateTime.now());
                            formatted = joined.replace("\"\"", "''").replace("\"","").replace("''", "\"").replace("\t\"", "\t''");
//                    System.out.println("formatted结束"+"Current Time: " + LocalDateTime.now());
                            synchronized (bw){
                                bw.write(formatted);
                            }

//                    System.out.println("bw写入结束"+"Current Time: " + LocalDateTime.now());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },threadPoolExecutor);

                    stringBuilder.setLength(0);
                }
            }
            System.out.println("Current Time: " + LocalDateTime.now());
            System.out.println("处理完成,总用时："+(System.currentTimeMillis()-startTime)/1000+"秒");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

//    @GetMapping("/handlerSlice")
    public void handSlice(){
//        String inputFilePath = "G:\\已上传文件\\linkedin2023\\linkedin.csv";
//        String outputFilePath = "G:\\已上传文件\\linkedin2023\\linkedin.csv.tsv";
        String inputFilePath = "D:\\IDA-workspace\\DataHandler\\dataHandler\\src\\main\\resources\\test.csv";
        String outputFilePath = "D:\\IDA-workspace\\DataHandler\\dataHandler\\src\\main\\resources\\test.csv.tsv";
//java -jar dataHandler-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath),"UTF-8"))) {

            System.out.println("文件"+inputFilePath+"读取成功，正在处理");
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder processTime = new StringBuilder();
            long startTime = System.currentTimeMillis();
            System.out.println("Current Time: " + LocalDateTime.now());

            stringBuilder = stringBuilder.append(br.readLine()).append("\n");
            String s = StringUtils.join(stringBuilder.toString().replace("\"", "").split(","), "\t");
            bw.write(s);
            stringBuilder.setLength(0);
            //35945361
            for (int i = 1; i < 35945361; i++) {
                if (i%100000==0)  {
                    processTime.append("当前进度：").append(i).append("/35945361,已处理时间(毫秒):").append(System.currentTimeMillis()-startTime);
                    System.out.println(processTime.toString());
                    processTime.setLength(0);
                }
                stringBuilder = stringBuilder.append(br.readLine());
//                stringBuilder.charAt(stringBuilder.length() - 2) == '\"' && stringBuilder.charAt(stringBuilder.length() - 3) == ',';
//                stringBuilder.charAt(stringBuilder.length() - 2) == '\"' && stringBuilder.charAt(stringBuilder.length() - 3) == ','
                if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '\"'){
                    String strBuilderString = stringBuilder.toString();

                            String replaced ;
                            String[] split ;
                            String joined;
                            String formatted;
                            replaced = strBuilderString.replace("\n", " ").replace("\r", " ");
//                    System.out.println("replaced结束"+"Current Time: " + LocalDateTime.now());
                            split = replaced.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
//                    System.out.println("split结束"+"Current Time: " + LocalDateTime.now());
                            joined = StringUtils.join(split, "\t")+"\n";
//                    System.out.println("joined结束"+"Current Time: " + LocalDateTime.now());
                            formatted = joined.replace("\"\"", "''").replace("\"","").replace("''", "\"").replace("\t\"", "\t''");
//                    System.out.println("formatted结束"+"Current Time: " + LocalDateTime.now());
                    CompletableFuture.runAsync(() ->{
                        try {
                            bw.write(formatted);
//                    System.out.println("bw写入结束"+"Current Time: " + LocalDateTime.now());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },threadPoolExecutor);

                    stringBuilder.setLength(0);
                }
            }
            System.out.println("Current Time: " + LocalDateTime.now());
            System.out.println("处理完成,总用时："+(System.currentTimeMillis()-startTime)/1000+"秒");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }



    /**
     * 队列测试
     *
     * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
     * @from <a href="https://yupi.icu">编程导航知识星球</a>
     */

//    @GetMapping("/get")
    public String getCurrentThread() {
        Map<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度", size);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数", taskCount);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数", completedTaskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在工作的线程数", activeCount);
//        return JSONUtil.toJsonStr(map);
        return "";
    }


}
