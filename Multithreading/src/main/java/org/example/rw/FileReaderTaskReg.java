package org.example.rw;

import org.example.controller.RunLog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FileReaderTaskReg implements Runnable {
    private final BlockingQueue<String> queue;
    private final String inputFile;

    public FileReaderTaskReg(BlockingQueue<String> queue, String inputFile) {
        this.queue = queue;
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        String regex =",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        Pattern pattern = Pattern.compile(regex);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine = br.readLine();
            int count = countComma(pattern,currentLine);
            queue.put(currentLine);
            //临时字符串
            String temp;
            //临时计数
            int tempCount=0;
            int i = 0;
            long startTime = System.currentTimeMillis();
            while ( (temp = br.readLine()) !=null ) {
                new RunLog().countSecond(i,10000,3603385,startTime);
                i++;
                stringBuilder.append(temp);
                tempCount = countComma(pattern, stringBuilder.toString());
                if (tempCount == count){
                    queue.put(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
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
                System.out.println("读进程结束了");
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
