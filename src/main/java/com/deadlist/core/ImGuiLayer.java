package com.deadlist.core;

import com.deadlist.test.Launcher;
import com.deadlist.test.TestGame;
import imgui.ImFont;
import imgui.ImGui;
import imgui.type.ImFloat;

public class ImGuiLayer {
    private boolean showText = false;

    private TestGame game;


    public void imgui() {


        game = Launcher.getGame();


        ImGui.begin("Debug Window");


        ImGui.beginTabBar("1");

        ImGui.beginTabItem("Player");


        ImGui.text(game.getPlayerPositionToString());
        ImGui.text(game.getPlayerRotationToString());
            //ImGui.sameLine();

        ImGui.endTabItem();

        ImGui.endTabBar();

        ImGui.end();
    }
}
