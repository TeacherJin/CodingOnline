package me.vk.CodingOnline.tools;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tools {
    //代码保存的路径
    private String codesPath;
    //代码保存的名称，不含后缀名
    private String className;

    //构造方法

    public Tools(String codesPath) {
        this.codesPath = codesPath;
    }

    public Tools(String codesPath, String className) {
        this.codesPath = codesPath;
        this.className = className;
    }

    //错误提示信息
    final private String ERROR = "服务器出现故障，请联系管理员";

    public String runCode() {
        //获取操作系统类型
        String osType = System.getProperty("os.name");
        //程序运行结果
        String result = ERROR;
        //根据操作系统执行合适的方法，这里仅仅支持linux和windows
        if (osType.toLowerCase().endsWith("linux")) {
            //编译和运行命令
            String complie = "javac " + codesPath + "/" + className + ".java";
            String run = "java -classpath " + codesPath + " " + className;
            result = runCodeWithLinux(complie);
            if (result.length() == 0) {
                return runCodeWithLinux(run);
            } else {
                return result;
            }
        }
        return result;
    }

    //运行Linux的方法，cmd为要执行的命令
    private String runCodeWithLinux(String cmd) {
        //程序运行结果
        String result = ERROR;
        //生成Runtime对象
        Runtime runtime = Runtime.getRuntime();
        try {
            //生成调用complie命令的进程
            Process processComplie = runtime.exec(cmd);
            //获取运行结果，需要获取运行结果和错误信息
            InputStream in = processComplie.getInputStream();
            InputStream error = processComplie.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(error));
            String s = "";
            StringBuilder sbResult = new StringBuilder();
            StringBuilder sbError = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sbResult.append(s).append("\n");
            }
            while ((s = errorReader.readLine()) != null) {
                sbError.append(s).append("\n");
            }
            //生成运行结果
            result = sbResult.toString() + sbError.toString();
            //关闭各种资源，结束进程
            reader.close();
            errorReader.close();
            error.close();
            in.close();
            processComplie.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void writeCode(String code){
        FileSystem fileSystem= FileSystems.getDefault();
        Path path=fileSystem.getPath(codesPath+"/"+className+".java");
        try (BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            bw.write(code, 0, code.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //列出指定目录下所有以java为后缀名的文件
    public List<String> listCodeFiles(){
        System.out.println(codesPath);
        File f=new File(codesPath);
        File[] files=f.listFiles();
        Arrays.sort(files);
        List<String> javaFiles=new ArrayList<String>();
        for(File file:files){
            if(file.getName().toLowerCase().endsWith("java")){
                javaFiles.add(file.getName());
            }
        }
        return javaFiles;
    }
}
