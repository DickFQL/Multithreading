package org.example.practice;

public class DaemonThread2 {
    public static void main(String[] args) {
        Thread t = new DaemonThread1();
        // 如果注释下一行，观察Thread1的执行情况:
        t.setDaemon(true);
        t.start();
        System.out.println("main: wait 3 sec...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        System.out.println("main: end.");
    }
}

class DaemonThread1 extends Thread {
    @Override
    public void run() {
        System.out.println("Thread: start");
        for (;;) {
            System.out.println("Thread-1: running...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}
