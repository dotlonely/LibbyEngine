package com.deadlist.test;

import com.deadlist.core.WindowManager;
import org.lwjgl.Version;
public class Launcher {

    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        WindowManager window = new WindowManager("Fruiting Bodies", 1024, 768, false);
        window.init();

        while(!window.windowShouldClose()){
            window.update();
        }

        window.cleanup();
    }
}
