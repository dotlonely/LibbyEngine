package com.deadlist.core;

import com.deadlist.test.Launcher;
import com.deadlist.test.TestGame;
import imgui.ImGui;

public class ImGuiLayer {
    private boolean showText = false;

    private TestGame game;


    public void imgui() {


        game = Launcher.getGame();

        if(ImGui.begin("Debug Window")) {

            if(ImGui.beginTabBar("Settings#left_tab_bar")) {

                // Player Tab
                if (ImGui.beginTabItem("Player")) {

                    ImGui.text(game.getPlayerPositionToString());
                    ImGui.text(game.getPlayerRotationToString());
                    ImGui.newLine();
                    ImGui.text("Looking At: ");
                    ImGui.text(game.getMousePickerPointToString());
                    ImGui.newLine();
                    ImGui.text(game.getNormalizedDeviceCoordsToString());


                }
                else{
                    ImGui.endTabItem();
                    ImGui.setTabItemClosed("Player");
                }
                // End Player Tab
                if(ImGui.beginTabItem("Other")){
                    ImGui.text("Hello there");
                }
                else{
                    ImGui.endTabItem();
                    ImGui.setTabItemClosed("Other");
                }

            }
            ImGui.endTabBar();

            ImGui.dummy(2, 2);

            if(ImGui.beginTabBar("ExtendedFunctions#left_tab_bar")) {

                if (ImGui.beginTabItem("Camera")) {
                    ImGui.text(game.getCameraPositionToString());
                    ImGui.text(game.getCameraRotationToString());

                }

                ImGui.endTabItem();

            }

            ImGui.endTabBar();


        }
        ImGui.end();
    }
}
