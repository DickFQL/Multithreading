package org.example;


import org.example.entity.MyThread0;
import org.example.entity.SecondTestThread;

// 按两次 Shift 打开“随处搜索”对话框并输入 `show whitespaces`，
// 然后按 Enter 键。现在，您可以在代码中看到空格字符。
public class Main {
    public static void main(String[] args) {
        System.out.println("main start");
        Thread t = new MyThread0();
        Thread t2 = new SecondTestThread();
        t.setPriority(1);
        t2.setPriority(10);
        t.start(); // 启动新线程
        t2.start();
//        System.out.println("main end");
    }
}

