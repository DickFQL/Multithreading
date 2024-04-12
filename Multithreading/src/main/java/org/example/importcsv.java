package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class importcsv {

    public static void main(String[] args) throws ClassNotFoundException {
        String csvFile = "C:\\Users\\panyu_202101\\Desktop\\customers-100.csv"; // 文件路径
        String line = "";
        String csvSplitBy = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"; // CSV文件中的分隔符
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            boolean flag = true;
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/customer_test","root","root");
            while ((line = br.readLine()) != null) {
                // 使用逗号分割行中的字段
                if (flag) {
                    flag = false;
                    continue;
                }
                String[] data = line.split(csvSplitBy);
                String query = "INSERT INTO customer_100 (Indexx,Customer_Id,First_Name,Last_Name,Company,City,Country,Phone1,Phone2,Email,Subscription_Date,Website) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(query);
                // 输出每个字段
                for (int i=0;i<data.length;i++) {

                        statement.setString(i+1,data[i]);
                        System.out.print(data[i]+" ");

                }
                statement.executeUpdate();
                System.out.println("执行成功"); // 换行
            }
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
