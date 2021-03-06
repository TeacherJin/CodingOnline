package me.vk.CodingOnline.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import me.vk.CodingOnline.model.Student;
import me.vk.CodingOnline.model.Verse;
import me.vk.CodingOnline.tools.Tools;

import java.io.*;
import java.util.List;
import java.util.Random;

//为了可以使用session传递数据，添加拦截器
@Before(SessionInViewInterceptor.class)
public class StudentController extends Controller {
    //访问首页
    public void index() {
        //从数据库中获取诗词警句，显示在首页上
        List<Verse> verses = Verse.dao.findAll();
        //获取一个随机数，不大于诗词警句的总数量
        Random random = new Random();
        int i = random.nextInt(verses.size());
        //将该随机数对应的诗词警句放到session当中;
        setSessionAttr("verse", verses.get(i).getStr("verse"));
        //渲染页面，首页
        render("/index.html");
    }

    //处理登录过程
    public void login() {
        //获取登录页面index.html传递过来的学号和密码
        String studentId = getPara("studentId");
        String studentPassword = getPara("studentPassword");
        //从数据库学生信息表中获取相关的数据
        Student student = Student.dao.findById(studentId);
        //判断是否为合法用户
        if (student != null && student.getStr("password").equals(studentPassword)) {
            //将学号和姓名写入session
            setSessionAttr("studentId", studentId);
            setSessionAttr("studentName", student.getStr("name"));
            //跳转到editor.html页面
            redirect("/editor");
        } else {
            //给出错误提示信息并提供跳转回首页的链接
            renderHtml("输入的用户名或密码错误，请返回<a href=\"./index\">首页</a>并重新输入正确信息");
        }

    }

    //渲染在线编程页面editor.html
    public void editor() {
        //获取学号，因为不是从前台传过来的，而是通过session传递的，所以使用getSessionAttr方法
        String studentId=getSessionAttr("studentId");
        //获取代码保存路径
        String codesPath = getRequest().getServletContext().getRealPath("/codes") + "/" + studentId;
        //获取Tools对象
        Tools tool=new Tools(codesPath);
        //获取用户代码目录下的源代码文件名称
        List<String> javaFiles=tool.listCodeFiles();
        setSessionAttr("javaFiles",javaFiles);
        render("/editor.html");
    }

    //渲染注册用户的页面
    public void newUser() {
        //渲染创建新用户的页面
        render("/newUser.html");
    }

    //处理注册事务
    public void register() {
        //获取用户填写的信息
        String studentId = getPara("studentId");
        String studentName = getPara("studentName");
        String studentPassword = getPara("studentPassword");
        //创建用户模型对象
        Student student = Student.dao.findById(studentId);
        //判断用户提交的学号是否已经被注册
        if (student != null) {
            renderHtml("您输入的学号已被注册，请返回<a href=\"/index\">首页</a>并重新输入正确信息");
        } else {
            //将信息添加到数据库中，注意，必须重新new一个用户对象
            new Student().set("id", studentId).set("name", studentName).set("password", studentPassword).save();
            //为该用户添加存放代码的目录
            String codesPath = getRequest().getServletContext().getRealPath("/codes") + "/" + studentId;
            File file = new File(codesPath);
            if (file.mkdir()) {
                renderHtml("您已经注册成功，请返回<a href=\"./index\">首页</a>并登录");
            } else {
                renderHtml("<font color='red'>服务器出现问题，请联系管理员</font>");
            }
        }
    }

    //编译并运行程序
    public void runCode(){
        //获取前台页面的参数值
        String className=getPara("className");
        String code=getPara("code");
        String studentId=getPara("studentId");
        //获取用户名
        Student student = Student.dao.findById(studentId);
        String studentName=student.getStr("name");
        //获取代码保存路径
        String codesPath = getRequest().getServletContext().getRealPath("/codes") + "/" + studentId;
        //获取Tools对象
        Tools tool=new Tools(codesPath,className);
        //保存代码文件
        tool.writeCode(code);
        //编译并运行程序，将结果保存起来
        String result=tool.runCode();
        //将结果存入session，为了方便显示，将之前获取的参数也存入session
        setSessionAttr("result",result);
        setSessionAttr("className",className);
        setSessionAttr("code",code);
        setSessionAttr("studentId",studentId);
        setSessionAttr("studentName",studentName);
        //跳转到editor页面
        redirect("/editor");
    }

    //打开链接中指定的文件
    public void openFile() throws FileNotFoundException {
        //获取各类参数
        String file=getPara("file");
        String className=file.substring(0,file.lastIndexOf(".java"));
        String studentId=getSessionAttr("studentId");
        String studentName=new Student().dao.findById(studentId).getStr("name");
        //获取代码保存路径
        String codesPath = getRequest().getServletContext().getRealPath("/codes") + "/" + studentId;
        String filePath=codesPath+"/"+file;
        //读取文件中的内容
        File f=new File(filePath);
        String code="";
        try  {
            FileReader reader = new FileReader(f);
            BufferedReader br=new BufferedReader(reader);
            StringBuilder sb=new StringBuilder();
            String s="";
            while ((s=br.readLine())!=null){
                sb.append(s+"\n");
            }
            reader.close();
            br.close();
            code=sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把相关内容加入到session中
        setSessionAttr("className",className);
        setSessionAttr("code",code);
        setSessionAttr("studentId",studentId);
        setSessionAttr("studentName",studentName);
        //跳转到editor页面
        redirect("/editor");
    }
    //退出登录状态
    public void logout(){
        //清除所有的session
        getSession().invalidate();
        redirect("/index");
    }
}
