package org.example.practice;

import java.util.concurrent.CompletableFuture;

public class CompletableFuturePra {

    public static void Test01( ) {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("点了番茄炒蛋");
//        CompletableFuture.su
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务器打发票");
            SmallTool.sleepMillis(100);
            return "500发票";
        });
        SmallTool.printTimeAndThread("小白打完电话");
        SmallTool.printTimeAndThread( String.format("收到 %s,离开饭店",async.join()));
    }

    public static void Test02(){
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("点了番茄炒蛋");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师开草");
            return "番茄炒蛋";
        }).thenCompose( dish -> CompletableFuture.supplyAsync(()->{
            SmallTool.printTimeAndThread("服务员打饭");
            return dish + "服务员打好饭";
        }));
        System.out.println("test"+cf1.join());
    }
    public static void Test03(){
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("点了番茄炒蛋");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(300);
            return "番茄炒蛋好了";
        });
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("服务员蒸饭");
            SmallTool.sleepMillis(200);
            return "饭蒸好了";
        });
        SmallTool.printTimeAndThread(cf1.join()+cf2.join() + "都准备好了");
        SmallTool.printTimeAndThread("开始吃饭");

    }
    public static void Test03_opt(){
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("点了番茄炒蛋");

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            return "番茄炒蛋好了";
        }).thenCombine(CompletableFuture.supplyAsync(()->{
            SmallTool.printTimeAndThread("服务员蒸饭");
            SmallTool.sleepMillis(100);
            return "饭蒸好了";
        }),(dish , rice)->{
            SmallTool.printTimeAndThread("服务员打饭");
            SmallTool.sleepMillis(100);
            return String.format("%s + %s 好了",dish,rice);
        });

        SmallTool.printTimeAndThread(cf1.join() + "都准备好了");
        SmallTool.printTimeAndThread("开始吃饭");

    }
    public static void main(String[] args) {
//        Test01();
        Test02();
//        Test03();
//        Test03_opt();
    }

}
