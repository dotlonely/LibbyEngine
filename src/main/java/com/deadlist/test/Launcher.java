package com.deadlist.test;

import com.deadlist.core.EngineManager;
import com.deadlist.core.WindowManager;
import com.deadlist.core.utils.Consts;
import org.lwjgl.Version;
public class Launcher {

    private static WindowManager window;
    private static EngineManager engine;

    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, 1024, 768, false);
        engine = new EngineManager();

       try {
            engine.start();
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    public static WindowManager getWindow() {
        return window;
    }
}
