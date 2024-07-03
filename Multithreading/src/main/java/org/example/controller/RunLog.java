package org.example.controller;

import java.time.LocalDateTime;

public class RunLog {

    private final LocalDateTime startTime = LocalDateTime.now();
    public void countSecond(long curret, int step, long total,long startTime){

        if (curret%step==0)  {
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("当前进度：已处理了" + curret + "行数据,总计："+total+"行数据"+ ", 已处理时间(毫秒): " + elapsedTime);
        }
    }

}
