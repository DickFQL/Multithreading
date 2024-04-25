package org.example.handlerfile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class noThreadPool {
    /**
     * 分割方式
     * @param integer
     * @return
     */
    public static  String partition(Integer integer){
            HashMap<Integer, String> map = new HashMap<>();
            map.put(1,",");
            map.put(2,".");
            map.put(3,":");
            map.put(4,"\t");
            map.put(5,"\\|");
            map.put(11,",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            map.put(21,"(?<=\\\\D),(?=\\\\D)");
            return map.get(integer);
        }

    /**
     * 调整输入文件的格式
     * @param inputFilePath
     * @return
     */
    public static String inputFileFormat(String inputFilePath) {
        if (inputFilePath.equals(null)) return null;
        String[] split = inputFilePath.split("\\\\");
        String intputFilePathoutput = "";
        for (int i= 0;i< split.length-1;i++){
            intputFilePathoutput +=split[i]+"\\";
        }
        intputFilePathoutput =intputFilePathoutput+ "finish_" + split[split.length-1];
        return intputFilePathoutput;
    }
    /**
     * 调整输出文件的格式
     * @param inputFilePath
     * @return
     */
    public static String outputFileFormat(String inputFilePath ,String outputFilePath){

        String[] tmp = inputFilePath.split("\\\\");
        if (outputFilePath.charAt(outputFilePath.length()-1) != '\\' ) outputFilePath +='\\';
         outputFilePath += tmp[tmp.length-1]+".tsv";
        return outputFilePath;
    }

    /**
     * 修改输入文件名
     * @param inputFilePath
     */
    public static void changeFileName(String inputFilePath){
        //修改文件名
        String inputFileRename = inputFileFormat(inputFilePath);
        File oldFile = new File(inputFilePath);
        File newFile = null;
        if (inputFileRename != null) {
            newFile = new File(inputFileRename);
        }
        boolean b = oldFile.renameTo(newFile);
        if (b) System.out.println("文件修改成功");
        else System.out.println("文件修改失败");
    }

    public static Integer handlerLen(String line,String regex){
        String[] split = line.split(regex);
        return split.length;
    }
    /**
     * 测试函数：处理每行数据测试函数
     * @param line
     * @param regex
     * @return
     */
    public static String handlerTest(String line,String regex){
//        若字符从最后一位为分隔符，在最后添加空字符串
        if (line.charAt(0) == regex.charAt(0)) line = "''" + line;
        if (line.charAt(line.length()-1) == regex.charAt(0)) line += "''";
//        分割
        String[] data = line.split(regex);
        int len = data.length;
        for (int i=0;i<len;i++) {
            if (data[i].equals(""))data[i] = "''";
//            System.out.println("第"+(i+1)+"个字符串为："+data[i]);
        }
//        重新拼接
        String string = String.join(" ",data);

//        string +="\n";
        System.out.println(string);
        return string;
    }

    /**
     * 处理文件中每一行数据的格式
     * @param line
     * @param regex
     * @return
     */
    public static String handlerFormat(String line,String regex,Integer integer){
//        若字符从第一位、最后一位为分隔符，在最后添加空字符串
        if (line.charAt(0) == regex.charAt(0)) line = "''" + line;
        if (line.charAt(line.length()-1) == regex.charAt(0)) line += "''";
//        分割
        String[] data = line.split(regex);
        String string = "";
        int len = data.length;
        for (int i=0;i<integer;i++) {
            if (i==0) {
                string = data[i];
                continue;
            }
            if (i < len )  {
                if (data[i].equals(""))  string += "\t''";
                else string += "\t"+ data[i];
            }
            else string += "\t''";
//            System.out.println("第"+(i+1)+"个字符串为："+string);
        }
//        重新拼接

        string +="\n";
        return string;
    }

    /**
     * 常规分割字符串
     * @param inputFilePath
     * @param outputFilePathDouble
     * @param regex
     */
    public static void handlerformal(String inputFilePath,String outputFilePathDouble,String regex){
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePathDouble), "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath),"UTF-8"))) {
//            FileInputStream fis = new FileInputStream(inputFilePath);
//            FileOutputStream fos = new FileOutputStream(outputFilePathDouble);
//            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
//            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
//            BufferedReader br = new BufferedReader(isr);
//            BufferedWriter bw = new BufferedWriter(osw);

            System.out.println("文件"+inputFilePath+"读取成功，正在处理");
            //第一行数据处理
            String line = br.readLine();

            String[] split = line.split(regex);
            int len = split.length;
            bw.write(handlerFormat(line,regex,len));
//            line = br.readLine();
            //第二行至末尾数据处理
            while((line = br.readLine())!=null){
                bw.write(handlerFormat(line,regex,len));
            }
            System.out.println("文件处理完成");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 处理非常规的数据分割
     * @param inputFilePath
     * @param outputFilePathDouble
     * @param regex
     */
    public static void handlerInformal(String inputFilePath,String outputFilePathDouble,String regex){
       try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePathDouble))) {
            System.out.println("文件读取成功，正在处理");
            //第一行字段填写
            String line = br.readLine();
            regex = "\\[(.*?)\\]";
            String firstline = regexLine(line,regex);
            bw.write(firstline+"\n");
            //第二行至末行数据填写
            String regexValue1 = ">(.*?)[\\[\\)]";
            do {
            String value1 = regexLine(line,regexValue1);
            bw.write(value1+"\n");
            }while ((line = br.readLine())!=null);

            System.out.println("文件处理完成");
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 将匹配的结果转为专门的字符串
     * @param line
     * @param regex
     * @return
     */
    public static String regexLine(String line,String regex){
        Matcher matcher = Pattern.compile(regex).matcher(line);
        String firstline = "";
        while (matcher.find()) {
            if (matcher.group(1).equals(" ")) firstline += "''"+"\t";
            else firstline += matcher.group(1)+"\t"; // 使用 group(1) 获取括号内的内容
        }
        return firstline;
    }

    public static void handlerJson(String inputFilePath,String outputFilePathDouble){
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePathDouble))) {
            System.out.println("文件读取成功，正在处理");
            //第一行字段填写
            String line = br.readLine();
            String newLine = line.replace("null", "\"null\"");
            String regex = "\"(.*?)\"";
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static String[] test(String... values) {
        String[] input = new String[10];
        int i=1;
        // 遍历可变参数数组，打印每个值
        for (String value:values) {

            System.out.println();
            i++;
        }
        return input;
    }
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String input= "";
        String outputFilePath = "";
        String regex =  "";
        String mode = "";
        Map<Integer,String> outputMap = new HashMap<>();

        try{
            regex = args[0];
            mode = args[1];
            outputFilePath = args[2];
            for (int i = 0; i < args.length-3; i++) {
                outputMap.put(i, args[i+3]);
            }
        }catch (Exception e){
            System.out.println("命令格式：java -jar Multithreading.jar <输入数据分割方式> <输入数据处理模式> <输入文件的绝对路径> <输出文件的绝对路径> ");
            //分割方式
            //System.out.println("输入数据分割方式，例如:输入1：以逗号分割,输入2：以点号分割，输入3：冒号，输入4：制表符，输入11：非双引号内的逗号，输入21：非数字内的逗号");
            System.out.println("输入数据分割方式,例如：, : ;");
            //处理方式：常规和非常规
            System.out.println("输入数据处理模式,例如：0：命令说明；1：常规处理；2：json;3:非常规：[1]=>xxx;[2]=>xxx;");
            //输入文件地址
            System.out.println("输入文件的绝对路径，例如： D:\\IDA-workspace\\Multithreading\\data\\TSDM.txt");
            //输出文件地址
            System.out.println("输出文件的绝对路径，例如：D:\\IDA-workspace\\Multithreading\\data\\");
            return;
        }
        //————————————输入完成——————————————————————
        //展示输入内容
        //数据分割方式
        System.out.println("数据分割方式:"+regex);
        //数据处理模式
        System.out.println("数据处理模式"+mode);
        //输出文件的文件夹
        System.out.println("输出文件的文件夹"+outputFilePath);
        for (int i = 0; i < args.length-3; i++) {
            System.out.println("输入的文件名为："+outputMap.get(i));
        }

        for (int i = 0; i < args.length-3; i++) {
            System.out.println("输入的文件名为："+outputMap.get(i));
        }
        //——————————预处理完成————————————————————
        //outputFileFormat(String,String),输出路径调整路径调整
        System.out.println("开始处理");
        switch (mode){
            case "1":for (int i=0;i< args.length-3;i++) handlerformal(outputMap.get(i),outputFileFormat(outputMap.get(i),outputFilePath),regex);break;
            case "2":for (int i=0;i< args.length-3;i++) handlerJson(outputMap.get(i),outputFileFormat(outputMap.get(i),outputFilePath));break;
            case "3":for (int i=0;i< args.length-3;i++) handlerInformal(outputMap.get(i),outputFileFormat(outputMap.get(i),outputFilePath),regex);break;
            default:break;
        }
        //改变文件命名
//        changeFileName(inputFilePath);

    }
}
