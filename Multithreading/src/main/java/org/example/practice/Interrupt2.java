package org.example.practice;

public class Interrupt2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread2();
        t.start();
        Thread.sleep(1000);
        System.out.println("开始中断MyThread");//3
        t.interrupt(); // 中断t线程
        System.out.println("MyThread中断完成，等待MyThread执行完");//4
        t.join(); // 等待t线程结束
        System.out.println("end"); //7
    }
}

class MyThread2 extends Thread {
    public void run() {
        Thread hello = new HelloThread();
        hello.start(); // 启动hello线程
        try {
            System.out.println("MyThread等待被中断1"); //1
            hello.join(); // 等待hello线程结束
            System.out.println("MyThread等待被中断2");
        } catch (InterruptedException e) {
            System.out.println("interrupted!"); //5
        }
        hello.interrupt();
    }
}

class HelloThread extends Thread {
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");//2
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("helloThread被中断"); //6
                break;
            }
        }
    }
}