package org.example.handlerfile;

import java.io.*;
import java.util.concurrent.*;

public class SeperateRW {
        private static final int QUEUE_CAPACITY = 1000;
        private static final String INPUT_FILE = "D:\\IDA-workspace\\DataHandler\\dataHandler\\src\\main\\resources\\test.csv";
        private static final String OUTPUT_FILE = "D:\\IDA-workspace\\DataHandler\\dataHandler\\src\\main\\resources\\output_file.csv";

        public static void main(String[] args) throws InterruptedException {
            BlockingQueue<String> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
            ExecutorService executorService = Executors.newFixedThreadPool(2); // 一个读线程，一个写线程

            // 启动读线程
            executorService.execute(new FileReaderTask(queue, INPUT_FILE));

            // 启动写线程
            executorService.execute(new FileWriterTask(queue, OUTPUT_FILE));

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
    }

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
                StringBuilder stringBuilder = new StringBuilder();

                while (stringBuilder.append(br.readLine()).toString()  != null) {
                    if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == '\"'){
                        queue.put(stringBuilder.toString());
                        stringBuilder.setLength(0);
                    }

                }
            } catch (IOException | InterruptedException e) {
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
    }

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
                e.printStackTrace();
            }
        }
    }
