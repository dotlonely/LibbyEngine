package com.deadlist.test;

import com.deadlist.core.ILogic;
import com.deadlist.core.ObjectLoader;
import com.deadlist.core.RenderManager;
import com.deadlist.core.WindowManager;
import com.deadlist.core.entity.Model;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements ILogic {

    private int direction = 0;
    private float color = 0;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Model model;

    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        model = loader.loadModel(vertices);

    }

    @Override
    public void input() {
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)){
            direction = 1;
        }
        else if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN)){
            direction = -1;
        }
        else{
            direction = 0;
        }
    }

    @Override
    public void update() {
        color += direction * 0.01f;
        if(color > 1){
            color = 1.0f;
        }
        else if(color <= 0){
            color = 0.0f;
        }
    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.render(model);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
