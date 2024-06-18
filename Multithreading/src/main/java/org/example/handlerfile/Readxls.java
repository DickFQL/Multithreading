package org.example.handlerfile;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Readxls {
    public static void main(String[] args) {
        String text = "( [UID] => 1 [username] => footfoot [email] => muenkel.sebastian@gmail.com [password] => $2a$12$h6nY4zZU3ceEotMBl1dQPeKLgmVkUb6K.4IjJUuv./KhybtIezL0K [salt] => $2a$12$h6nY4zZU3ceEotMBl1dQPr [ip] => ) ";

        // 定义正则表达式
        String regex = ">(.*?)[\\[\\)]"; // 匹配 [] 内的内容，使用非贪婪匹配

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);

        // 创建匹配器
        Matcher matcher = pattern.matcher(text);

        // 查找匹配
        while (matcher.find()) {
            // 输出匹配到的内容
            if (matcher.group(1).equals(" ")) System.out.println("Found: " +"这是空串");
            else System.out.println("Found: " + matcher.group(1)); // 使用 group(1) 获取括号内的内容
        }


    }
}
