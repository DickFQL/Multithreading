package org.example.handlerfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Iterator;

public class HandlerJson {
        public static void main(String[] args) throws IOException, JSONException {
            char cbuf[] = new char[100000];
            InputStreamReader input =new InputStreamReader(new FileInputStream(new File("D:\\IDA-workspace\\data\\old\\json2.txt")),"UTF-8");
            int len =input.read(cbuf);
            String text =new String(cbuf,0,len);
            //1.构造一个json对象
            JSONObject obj = new JSONObject(text.substring(text.indexOf("{")));   //过滤读出的utf-8前三个标签字节,从{开始读取
//            System.out.println(obj.le);
            //2.通过getXXX(String key)方法获取对应的值
            System.out.println("id:"+obj.getString("id"));
            System.out.println("full_name:"+obj.getString("full_name"));
            System.out.println("first_name:"+obj.getString("first_name"));
            System.out.println("middle_initial:"+obj.getString("middle_initial"));
            //获取数组
//            System.out.println(obj.toString());
            Iterator keys = obj.keys();
            JSONArray names = obj.names();
            System.out.println(obj.toString());
            while(keys.hasNext()){
                System.out.print(keys.next()+" ");
                System.out.println(obj.getString((String) keys.next()));
            }

//            System.out.println("数组长度:"+arr.toString());

        }

}

