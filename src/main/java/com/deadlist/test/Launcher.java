package com.deadlist.test;

import com.deadlist.core.EngineManager;
import com.deadlist.core.ImGuiManager;
import com.deadlist.core.WindowManager;
import com.deadlist.core.utils.Consts;
import imgui.app.Application;
import imgui.ImGui;


public class Launcher {

    private static WindowManager window;
    private static ImGuiManager imGui;
    private static TestGame game;

    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, 1024, 768, false);
        imGui = new ImGuiManager(window);
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
    public static ImGuiManager getImGui() { return imGui; }

    public static TestGame getGame(){
        return game;
    }

}
