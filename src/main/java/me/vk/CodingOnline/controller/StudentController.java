package me.vk.CodingOnline.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import me.vk.CodingOnline.model.Verse;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

        render("/index.html");
    }

    //处理登录过程
    public void login(){

    }
}
