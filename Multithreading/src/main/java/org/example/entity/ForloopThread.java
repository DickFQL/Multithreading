package org.example.entity;


// 按两次 Shift 打开“随处搜索”对话框并输入 `show whitespaces`，
// 然后按 Enter 键。现在，您可以在代码中看到空格字符。
public class ForloopThread {

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
        for (int i = 0; i < 100; i++) {
            System.out.println("main: running...");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

}

class Thread1 extends Thread {

    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("Thread-1: running...");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }
}

class Thread2 extends Thread {

    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("Thread-2: running...");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }
}

