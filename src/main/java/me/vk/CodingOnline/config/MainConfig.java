package me.vk.CodingOnline.config;

import com.jfinal.config.*;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import me.vk.CodingOnline.controller.StudentController;

public class MainConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setViewType(ViewType.FREE_MARKER);
    }

    @Override
    public void configRoute(Routes me) {
        me.add("/", StudentController.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {

    }

    @Override
    public void configInterceptor(Interceptors me) {

    }

    @Override
    public void configHandler(Handlers me) {

    }
}
