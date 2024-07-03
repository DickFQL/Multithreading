package org.example.rw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 读文件线程操作
 */
class FileReaderTask implements Runnable {
    private final BlockingQueue<String> queue;
    private final String inputFile;

    public FileReaderTask(BlockingQueue<String> queue, String inputFile) {
        this.queue = queue;
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
            String line;
            int i = 0;
            long l = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                System.out.println("这是第"+i+"条数据" + line);
//                new RunLog().countSecond(i,10000,3603385,l);
                i++;
//                queue.put(line);
                if (i==15 ) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 结束标记
                queue.put("EOF");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Integer countComma(String currentLine){
        String regex =",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(currentLine);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }
}
