package org.example.rw;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;

class FileWriterTask implements Runnable {
    private final BlockingQueue<String> queue;
    private final String outputFile;

    public FileWriterTask(BlockingQueue<String> queue, String outputFile) {
        this.queue = queue;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
            while (true) {
                String line = queue.take();
                if ("EOF".equals(line)) {
                    // 如果读线程已经结束，并且队列中已经没有剩余数据
                    break;
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("写线程故障");
            e.printStackTrace();
        }
    }
}
