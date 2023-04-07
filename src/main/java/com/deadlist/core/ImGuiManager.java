package com.deadlist.core;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;


public class ImGuiManager {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private String glslVersion = "#version 150";

    private WindowManager window;
    private ImGuiLayer layer;

    public ImGuiManager(WindowManager window){
        this.window = window;
        layer = new ImGuiLayer();
    }

    public void init(){

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(window.getWindowHandle(), false);
        imGuiGl3.init(glslVersion);

    }

    public void update(){
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        layer.imgui();

        ImGui.render();

        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }

        GLFW.glfwSwapBuffers(window.getWindowHandle());
        GLFW.glfwPollEvents();
    }

    public void cleanup(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

}
