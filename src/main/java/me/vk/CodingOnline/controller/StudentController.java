package me.vk.CodingOnline.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import me.vk.CodingOnline.model.Student;
import me.vk.CodingOnline.model.Verse;

import java.util.List;
import java.util.Random;

//为了可以使用session传递数据，添加拦截器
@Before(SessionInViewInterceptor.class)
public class StudentController extends Controller {
    //访问首页
    public void index(){
        //从数据库中获取诗词警句，显示在首页上
        List<Verse> verses= Verse.dao.findAll();
        //获取一个随机数，不大于诗词警句的总数量
        Random random=new Random();
        int i=random.nextInt(verses.size());
        //将该随机数对应的诗词警句放到session当中;
        setSessionAttr("verse",verses.get(i).getStr("verse"));
        //渲染页面，首页
        render("/index.html");
    }

    //处理登录过程
    public void login(){
        //获取登录页面index.html传递过来的学号和密码
        String studentId=getPara("StudentId");
        String studentPassword=getPara("StudentPassword");
        //从数据库学生信息表中获取相关的数据
        Student student=Student.dao.findById(studentId);
        //判断是否为合法用户
        if(student !=null && student.getStr("password").equals(studentPassword) ){
            //将学号和姓名写入session
            setSessionAttr("StudentId",studentId);
            setSessionAttr("StudentName",student.getStr("name"));
            //跳转到editor.html页面
            redirect("/editor");
        }else{
            //给出错误提示信息并提供跳转回首页的链接
            renderHtml("输入的用户名或密码错误，请返回<a href=\"/index\">首页</a>并重新输入正确信息");
        }

    }

    //渲染在线编程页面editor.html
    public void editor(){
        render("/editor.html");
    }
}
