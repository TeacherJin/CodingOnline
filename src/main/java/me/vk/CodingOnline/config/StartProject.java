package me.vk.CodingOnline.config;

import com.jfinal.core.JFinal;

public class StartProject {
    public static void main(String[] args) {
        JFinal.start("src/main/webapp",8000,"/CodingOnline",5);
    }
}
