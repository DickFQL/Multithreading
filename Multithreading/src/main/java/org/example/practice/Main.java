package org.example.practice;


import org.example.controller.noThreadPool;

// 按两次 Shift 打开“随处搜索”对话框并输入 `show whitespaces`，
// 然后按 Enter 键。现在，您可以在代码中看到空格字符。
public class Main {
    public static void main(String[] args) {
        noThreadPool noThreadPool = new noThreadPool();
        String input = "\"078dac38513996889aa1bd7c5ea522b5\",\"[\\\"leticia.hanyszNarvaez@netcomlearning.com\\\"]\",\"[]\",\"\",\"\",\"ACwAAARkzYgBaH4BYcOEHaCGvbsL3HuEUZHvYq8\",\"73715080\",\"[]\",\"[]\",\"[]\",\"[]\",false,true,false,false,false,false,false,false,\"2021-02-08 20:33:57.000 +0100\",\"\",\"2021-02-08 20:33:57.000\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"[]\",\"[]\",\"[]\",\"Leticia\",\"Hanysz-Narvaez\",\"\",\"\",\"\",\"\",\"\",\"\",\"Education Management\",\"\",\"[]\",\"2011-10-01 00:00:00.000\",\"Senior Global Marketing Manager\",\"NYC\",\"We manage learning. We promote the values of lifelong learning.null\"";

        // 正则表达式：匹配不在双引号中的逗号
        String regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
               regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] datas = input.split(regex);

        for (String data : datas) {
            System.out.println(data);
        }
    }
}

