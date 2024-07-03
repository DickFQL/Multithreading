package org.example.controller;






import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * 调整输出文件的格式，改变文件名称加后缀
     * @param inputFilePath
     * @return
     */
    public static String outputFileFormat(String inputFilePath ,String outputFilePath){
        //将文件名保存，合并到输出文件路径
        String[] tmp = inputFilePath.split("\\\\");
        if (outputFilePath.charAt(outputFilePath.length()-1) != '\\' ) outputFilePath +='\\';
         outputFilePath += tmp[tmp.length-1]+".tsv";
        return outputFilePath;
    }
    /**
     * 调整输出文件的格式,不改变文件名称
     * @param inputFilePath
     * @return
     */
    public static String outputFileFormatNo(String inputFilePath ,String outputFilePath){
        //将文件名保存，合并到输出文件路径
        String[] tmp = inputFilePath.split("\\\\");
        if (outputFilePath.charAt(outputFilePath.length()-1) != '\\' ) outputFilePath +='\\';
        outputFilePath += tmp[tmp.length-1];
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
    public static String replaceFormat(String line, String regex, String afterRegex){

        StringBuffer result = new StringBuffer();
        result.append(line.replaceAll(regex, afterRegex));

//        System.out.println(result);
        result.append("\n");
        return result.toString();
    }

    public static boolean isBlank(String cs) {
        int strLen = cs.length();
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static String[] splitByNonQuotedComma(String input,String regex, String afterRegex) {
        List<String> parts = new ArrayList<>();
        boolean insideQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == afterRegex.charAt(0)) {
                insideQuotes = !insideQuotes;
            }
            if (c == regex.charAt(0) && !insideQuotes) {
                parts.add(sb.toString());
                sb.setLength(0); // 清空 StringBuilder
            } else {
                sb.append(c);
            }
        }

        // 添加最后一个部分
        parts.add(sb.toString());

        return parts.toArray(new String[0]);
    }

    /**
     * 处理文件中每一行数据的格式
     * @param line 文件中的每一行数据
     * @param regex 需要匹配的字符
     * @return
     */
    public static String handlerFormat(String line,String regex,Integer integer,String afterRegex){
//        若字符从第一位、最后一位为分隔符，在最后添加空字符串
//        regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        if (line.charAt(0) == regex.charAt(0)) line = "''" + line;
        if (line.charAt(line.length()-1) == regex.charAt(0)) line += "''";
//        分割
//        String[] datas = line.split(regex);
        String[] datas = splitByNonQuotedComma(line,regex,afterRegex);
        StringBuilder string = new StringBuilder();

        int len = datas.length;

        for (int i=0;i<integer;i++) {
            if (i==0) {
                string = new StringBuilder(datas[i]);
                continue;
            }
            if (i < len )  {
                //StringUtils不能使用？？？？？？？？

                if (isBlank(datas[i]))  string.append("\t").append("''");
                else string.append("\t").append(datas[i]);
                            }
            else {
                string.append("\t").append("''");
            }
        }
//        重新拼接

        string.append("\n");
        return string.toString();
    }
    public static void replaceChar(String inputFilePath,String outputFilePathDouble,String regex,String afterRegex){
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePathDouble), "UTF-8"));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath),"UTF-8"))) {

            System.out.println("文件"+inputFilePath+"读取成功，正在处理");
            //第一行数据处理
            String line = br.readLine();

            String[] split = line.split(regex);
//            int len = split.length;
            bw.write(replaceFormat(line,regex,afterRegex));
//            line = br.readLine();
            //第二行至末尾数据处理
            while((line = br.readLine())!=null){
                bw.write(replaceFormat(line,regex,afterRegex));
            }
            System.out.println("文件处理完成");
            System.out.println("已保存至"+outputFilePathDouble);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * 常规分割字符串
     * @param inputFilePath
     * @param outputFilePathDouble
     * @param regex
     */
    public static void handlerformal(String inputFilePath,String outputFilePathDouble,String regex,String afterRegex){
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

            bw.write(handlerFormat(line,regex,len,afterRegex));
//            line = br.readLine();
            //第二行至末尾数据处理
            while((line = br.readLine())!=null){
                if (!StringUtils.isBlank(line)) bw.write(handlerFormat(line,regex,len,afterRegex));
            }
            System.out.println("文件处理完成");
            System.out.println("已保存至"+outputFilePathDouble);
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

    /**
     *处理Json格式的数据
     * @param inputFilePath
     * @param outputFilePathDouble
     */

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
    public static void traverseDirectory(File directory,String outputFilePath,String regex,String mode,String afterRegex) {
        // 获取目录中的所有文件和子目录
        File[] files = directory.listFiles();

        // 遍历文件和子目录
        for (File file : files) {
            // 如果是文件，则打印文件路径
            if (file.isFile()) {
                switch (mode){
                    case "0":
                        replaceChar(file.getAbsolutePath(),outputFileFormatNo(file.getAbsolutePath(),outputFilePath),regex,afterRegex);
//                        System.out.println(file.getAbsolutePath());
                        break;
                    case "1":
                        handlerformal(file.getAbsolutePath(),outputFileFormat(file.getAbsolutePath(),outputFilePath),regex,afterRegex);
//                        System.out.println(file.getAbsolutePath());
                        break;
                    case "2":
                        handlerJson(file.getAbsolutePath(),outputFileFormat(file.getAbsolutePath(),outputFilePath));
                        break;
                    case "3":
                        handlerInformal(file.getAbsolutePath(),outputFileFormat(file.getAbsolutePath(),outputFilePath),regex);
                        break;
                    default:
                        break;
                }
            }
            // 如果是目录，则递归遍历子目录
            else if (file.isDirectory()) {
                System.out.println("Directory: " + file.getAbsolutePath());
                traverseDirectory(file,outputFilePath,regex,mode,afterRegex); // 递归调用遍历子目录
            }
        }
    }

    public static void initParam(String... args){
        String mode = "";
        String input= "";
        String outputFilePath = "";
        String regex =  "";
        String afterRegex = "";

        Map<Integer,String> inputMap = new HashMap<>();
        //测试
        String[] split = input.split(",");
        try{
            //1
            mode= args[0];
            regex  = args[1];
            afterRegex = args[2];
            outputFilePath = args[3];
            for (int i = 0; i < args.length-4; i++) {
                inputMap.put(i, args[i+4]);
            }
            //!1

        }catch (Exception e){
            System.out.println("命令格式：java -jar Multithreading.jar <输入数据分割方式> <输入数据处理模式> <输入文件的绝对路径> <输出文件的绝对路径> ");
            //处理模式：常规和非常规
            System.out.println("输入数据处理模式,例如：0：字符串替换；1：常规处理；2：json;3:非常规：[1]=>xxx;[2]=>xxx;");
            //分割方式
            //System.out.println("输入数据分割方式，例如:输入1：以逗号分割,输入2：以点号分割，输入3：冒号，输入4：制表符，输入11：非双引号内的逗号，输入21：非数字内的逗号");
            System.out.println("输入数据分割方式或替换前字符,例如：, : ;");
            //字段包围符或替换后字符
            //System.out.println("输入数据分割方式，例如:输入1：以逗号分割,输入2：以点号分割，输入3：冒号，输入4：制表符，输入11：非双引号内的逗号，输入21：非数字内的逗号");
            System.out.println("输入字段包围符或替换后字符,例如：, : ;");

            //输出文件地址
            System.out.println("输出文件的绝对路径，例如：D:\\IDA-workspace\\Multithreading\\data\\");
            //输入文件地址
            System.out.println("输入文件的绝对路径，例如： D:\\IDA-workspace\\Multithreading\\data\\TSDM.txt");
            System.out.println(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            return;
        }
        //————————————输入完成——————————————————————
        //展示输入内容
        //数据处理模式
        System.out.println("数据处理模式:"+mode);
        //数据分割方式
        if (mode.equals("0")) System.out.println("需要替换的字符串为："+regex);
        if (mode.equals("1")) System.out.println("数据分割方式:"+regex);
        //
        if (mode.equals("0")) System.out.println("替换的目标字符串为："+afterRegex);
        if (mode.equals("1")) System.out.println("字段包围符为:"+afterRegex);
        //输出文件的文件夹
        System.out.println("输出文件的文件夹"+outputFilePath);
        //2
        for (int i = 0; i < args.length-4; i++) {
            System.out.println("输入的文件名为："+inputMap.get(i));
        }
        //!2

        if (mode.equals("1") && !afterRegex.equals("null")) {
            regex = regex + "(?=(?:[^"+afterRegex+"]*"+afterRegex+"[^"+afterRegex+"]*"+afterRegex+")*[^"+afterRegex+"]*$)";
//            regex = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            System.out.println("regex为"+regex);
        }

        //——————————预处理完成————————————————————
        //outputFileFormat(String,String),输出路径调整路径调整
        System.out.println("开始处理");
        Boolean isDirtory = new File(inputMap.get(0)).isDirectory();
        if (isDirtory) traverseDirectory(new File(inputMap.get(0)),outputFilePath,regex,mode,afterRegex);
        else {
            switch (mode){
                case "0":
                    //3 i换成0,0换成i
                    for (int i=0;i< args.length-4;i++)
                        replaceChar(inputMap.get(i),outputFileFormat(inputMap.get(i),outputFilePath),regex,afterRegex);
                    break;
                case "1":
                    for(int i=0;i< args.length-4;i++)
                    handlerformal(inputMap.get(i),outputFileFormat(inputMap.get(i),outputFilePath),regex,afterRegex);
                    break;
                case "2":
                    for
                    (int i=0;i< args.length-4;i++) handlerJson(inputMap.get(i),outputFileFormat(inputMap.get(i),outputFilePath));
                    break;
                case "3":
                    for
                    (int i=0;i< args.length-4;i++) handlerInformal(inputMap.get(i),outputFileFormat(inputMap.get(i),outputFilePath),regex);
                    break;
                default:
                    break;
            }
        }
        //改变文件命名,test
//        changeFileName(inputFilePath);
    }
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        String mode = "1";
        String regex = ",";
        String afterRegex = "\"";
        String outputFilePath ="D:\\IDA-workspace\\Multithreading\\Multithreading\\target\\";
        String inputMap = "D:\\IDA-workspace\\Multithreading\\Multithreading\\target\\testOne.csv";
        initParam(mode,regex,afterRegex,outputFilePath,inputMap);
    }
}
