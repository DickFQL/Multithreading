package org.example;

public class WaitThread {
    public static void main(String[] args) throws InterruptedException{
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("hello");
        });
        System.out.println("start");
        thread.start();
        thread.join(0);
        System.out.println("end");
    }

}
