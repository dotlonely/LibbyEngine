package com.deadlist.core;

import com.deadlist.core.utils.Consts;
import com.deadlist.test.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {

    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 144;

    private static long deltaTime;
    public static int fps;
    private static float frameTime = 1.0f/FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private ImGuiManager imGui;
    private MouseInput mouseInput;
    private ILogic gameLogic;
    private GLFWErrorCallback errorCallback;


    private void init() throws Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        imGui = Launcher.getImGui();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        window.init();
        imGui.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void start() throws Exception{
        init();
        if(isRunning){
            return;
        }
            run();
    }

    public void run(){
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while(isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            deltaTime = startTime - lastTime;

            lastTime = startTime;

            unprocessedTime += deltaTime / (double) NANOSECOND;
            frameCounter += deltaTime;

            input();

            while(unprocessedTime > frameTime){
                render = true;
                unprocessedTime -= frameTime;

                if(window.windowShouldClose()){
                    stop();
                }

                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    window.setTitle(Consts.TITLE + " FPS: " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update();
                render();
                frames++;
            }
        }

        cleanup();
    }

    private void stop(){
        if(!isRunning){
            return;
        }
        isRunning = false;
    }

    private void input(){
        mouseInput.input();
        gameLogic.input(mouseInput);
    }

    private void render(){
        gameLogic.render();

        // Handling swapping buffers in the imGui update method.
        // So we disable the window's update method.
        //window.update();

        imGui.update();
    }

    private void update(){
        gameLogic.update(mouseInput);
    }

    private void cleanup(){
        gameLogic.cleanup();
        window.cleanup();
        imGui.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

    public static float getDeltaTime(){
        return (float)deltaTime * 0.000000001f;
    }
}
