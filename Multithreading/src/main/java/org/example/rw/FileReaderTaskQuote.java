package org.example.rw;

import org.example.controller.RunLog;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 读文件多线程操作，将多行数据整合为一行
 *
 */
class FileReaderTaskQuote implements Runnable {
    private final BlockingQueue<String> queue;
    private final String inputFile;

    private long total = 0;

    public FileReaderTaskQuote(BlockingQueue<String> queue, String inputFile, Long total) {
        this.queue = queue;
        this.inputFile = inputFile;
        this.total = total;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine = br.readLine();
            queue.put(currentLine);
            //临时字符串
            String temp = null;

            long i = 0;

            boolean flag = true;
            RunLog runLog = new RunLog();
            while ( true ) {
                if (flag) {
                    temp = br.readLine();
                    stringBuilder.append(temp);
                }
                if (temp == null) {
                    queue.put(stringBuilder.toString());
                    break;
                }
                runLog.countSecond(i++,100000,this.total,startTime);

                if (temp.endsWith("\"")){
                    temp = br.readLine();
                    //完整的数据
                    if (temp ==null || temp.startsWith("\"")){
                        queue.put(stringBuilder.toString().replace("\\\"\"","").replace("\\",""));
                        stringBuilder.setLength(0);
                    }
                    flag =false;
                    stringBuilder.append(temp);
                }//数据不完整
                else flag =true;
            }
        } catch (IOException e) {
            System.out.println("读线程故障，可能文件路径有问题");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("读线程故障，可能中断了");
            throw new RuntimeException(e);
        } finally {
            try {
                // 结束标记
                queue.put("EOF");

            } catch (InterruptedException e) {
                System.out.println("读线程故障，可能内存溢出了");
                Thread.currentThread().interrupt();
            }
        }
    }

    public Integer countComma(Pattern pattern, String currentLine){

        Matcher matcher = pattern.matcher(currentLine);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }
}
