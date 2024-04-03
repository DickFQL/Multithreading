package org.example;

import java.time.LocalTime;

public class DaemonThread extends Thread {
    @Override
    public void run() {
//        System.out.println("你好");
        while (true) {
            System.out.println(LocalTime.now());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
        System.out.println(LocalTime.now()+"这是第二个点");
    }

}