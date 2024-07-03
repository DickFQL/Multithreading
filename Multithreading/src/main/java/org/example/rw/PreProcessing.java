package org.example.rw;

import java.io.*;
import java.util.concurrent.*;

public class PreProcessing {
        private static final int QUEUE_CAPACITY = 1000;

        public static void seperateRW(String... args) throws InterruptedException {
            String inputFile;
            String outputFile;
            try{
                inputFile = args[0];
                outputFile = args[1];
            }catch (Exception e){
                System.out.println("命令格式：java -jar xxx.jar <输入数据处理模式> <输入文件的绝对路径> <输出文件的绝对路径> ");
//                //处理方式：常规和非常规
//                System.out.println("输入数据处理模式");
                //输入文件地址
                System.out.println("输入文件的绝对路径，例如： D:\\IDA-workspace\\Multithreading\\data\\TSDM.txt");
                //输出文件地址
                System.out.println("输出文件的绝对路径，例如：D:\\IDA-workspace\\Multithreading\\data\\TSDM.txt");
                return;
            }
            System.out.println("开始预处理");
            long startTime = System.currentTimeMillis();
            long total=0;
            try (BufferedReader brtemp = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
                while(true) {
                    if (brtemp.readLine() == null) break;
                    total++;
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BlockingQueue<String> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
            ExecutorService executorService = Executors.newFixedThreadPool(2); // 一个读线程，一个写线程
            // 启动读线程，处理为一行数据
            executorService.execute(new FileReaderTaskQuote(queue, inputFile,total));
            // 启动写线程
            executorService.execute(new FileWriterTask(queue, outputFile));
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println(new StringBuilder().append("预处理完毕，总耗时:").append((System.currentTimeMillis()-startTime)/1000).append("秒").append((System.currentTimeMillis()-startTime)%1000).toString());

        }
    }

