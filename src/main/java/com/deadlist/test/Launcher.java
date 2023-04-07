package com.deadlist.test;

import com.deadlist.core.EngineManager;
import com.deadlist.core.WindowManager;
import com.deadlist.core.utils.Consts;
public class Launcher {

    private static WindowManager window;
    private static TestGame game;

    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, 1024, 768, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();

       try {
            engine.start();
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame(){
        return game;
    }
}
