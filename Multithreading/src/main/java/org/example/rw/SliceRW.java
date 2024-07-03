package org.example.rw;

import org.example.controller.SliceTest;

import java.io.*;
import java.util.concurrent.*;


/**
 * 读写分离，同等文件大小
 */
public class SliceRW {
        private static final int QUEUE_CAPACITY = 1000;
        private static final String inputFile = "D:\\IDA-workspace\\Multithreading\\Multithreading\\test.csv";
        private static final String outputFile = "D:\\IDA-workspace\\Multithreading\\Multithreading\\output_file.csv";

        public static void main(String[] args) throws Exception {
//            String inputFile;
//            String outputFile;
            int threadNum = 2;
            //参数调用
//            try{
//                threadNum = Integer.parseInt(args[0]);
//                inputFile = args[1];
//                outputFile = args[2];
//
//            }catch (Exception e){
//                System.out.println("命令格式：java -jar xxx.jar <输入数据处理模式> <输入文件的绝对路径> <输出文件的绝对路径> ");
//                //处理方式：常规和非常规
//                System.out.println("输入数据处理模式");
//                //输出文件地址
//                System.out.println("输出文件的绝对路径，例如：D:\\IDA-workspace\\Multithreading\\data\\");
//                //输入文件地址
//                System.out.println("输入文件的绝对路径，例如： D:\\IDA-workspace\\Multithreading\\data\\TSDM.txt");
//                return;
//            }
            //分割文件
            File file = new File(inputFile);
            System.out.println(file.length());

            SliceTest sliceTest = new SliceTest();
            SliceTest.seperate(inputFile);
            long fileLine;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
                String line;
                fileLine = 0;
                while ((line = br.readLine()) != null) fileLine++;
            }

            //每个文件500MB
            int MB500 = 500 * 1024 * 1024;
            double DB500 = 500.0 * 1024 * 1024;
            int lineNum = (int) (file.length()>MB500 ?  (DB500 / file.length()) * fileLine: file.length());
//            System.out.println(lineNum);

            //对每个文件进行分布式读
            BlockingQueue<String> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
            ExecutorService executorService = Executors.newFixedThreadPool(threadNum); // 一个读线程，一个写线程
            // 启动读线程，处理为一行数据
//            executorService.execute(new FileReaderTaskQuote(queue, inputFile));

            //启动读线程
            for (int i = 0; i < threadNum-1; i++) {
                executorService.execute(new FileReaderTask(queue, inputFile));
            }

            // 启动写线程
//            executorService.execute(new FileWriterTask(queue, outputFile));

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
    }

