package me.vk.CodingOnline.config;

import com.jfinal.config.*;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import me.vk.CodingOnline.controller.StudentController;
import me.vk.CodingOnline.model.Student;
import me.vk.CodingOnline.model.Verse;

public class MainConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        //开启调试模式
        me.setDevMode(true);
        //使用FreeMarker来渲染页面
        me.setViewType(ViewType.FREE_MARKER);
    }

    @Override
    public void configRoute(Routes me) {
        //添加路由，凡是对网站根路径的访问都转到StudentController类处理
        me.add("/", StudentController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        DruidPlugin dp=new DruidPlugin("jdbc:mariadb://localhost/codedb","root","1981218");
        me.add(dp);
        ActiveRecordPlugin arp=new ActiveRecordPlugin(dp);
        me.add(arp);
        arp.addMapping("student", Student.class);
        arp.addMapping("verse", Verse.class);

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
