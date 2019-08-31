package me.vk.CodingOnline.tools;

public class Tools {
    //代码保存的路径
    String codeDir;
    //代码保存的名称，不含后缀名
    String className;
    //构造方法
    public Tools(String codeDir, String className) {
        this.codeDir = codeDir;
        this.className = className;
    }
    //错误提示信息
    final String ERROR="服务器出现故障，请联系管理员";
    public String runCode(){
        //获取操作系统类型
        String osType=System.getProperty("os.name");
        //程序运行结果
        String result=ERROR;
        //根据操作系统执行合适的方法，这里仅仅支持linux和windows
        if(osType.toLowerCase().endsWith("linux")){
            result=runCodeWithLinux();
        }
        return result;
    }

    private String runCodeWithLinux() {
        //程序运行结果
        String result=ERROR;


        return result;
    }
}
