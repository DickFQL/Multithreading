package org.example.yupi;

import java.io.BufferedWriter;
import java.io.IOException;

import static org.example.yupi.ThreadPool.handlerFormat;


public class Concurrency implements Runnable{

    private  String line;
    private  String regex;
    private  Integer integer;
    private final BufferedWriter writer;

    public Concurrency(String line,String regex,Integer integer, BufferedWriter writer) {
        this.line = line;
        this.regex = regex;
        this.integer = integer;
        this.writer = writer;
    }

    @Override
    public void run() {
        String tmp = handlerFormat(this.line,this.regex,this.integer);
        // 处理数据，这里假设只是将原始数据写入文件
        try {
            synchronized (writer) {
                writer.write(tmp);

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入时被中断");
            System.out.println("当前时间:"+System.currentTimeMillis());
        }
    }
}
