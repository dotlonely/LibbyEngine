package com.deadlist.test;

import com.deadlist.core.*;
import com.deadlist.core.entity.*;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.rendering.RenderManager;
import com.deadlist.core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestGame implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private  SceneManager sceneManager;
    private Camera camera;
    Vector3f cameraInc;


    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();


        Terrain terrain = new Terrain(new Vector3f(0f, -1f, -800f), loader, new Material(new Texture(loader.loadTexture("textures/terrain.png")), 0.1f));
        Terrain terrain2 = new Terrain(new Vector3f(-800f, -1f, -800f), loader, new Material(new Texture(loader.loadTexture("textures/terrain.png")), 0.1f));
        sceneManager.addTerrain(terrain);
        sceneManager.addTerrain(terrain2);

        Random rnd = new Random();

        Model model = loader.loadObjModel("/models/stall.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/babyblue.png")), 1f);

        Model model1 = loader.loadObjModel("/models/bunny.obj");
        model1.setTexture(new Texture(loader.loadTexture("textures/babyblue.png")));

        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 100 - 50;
            float y = rnd.nextFloat() * 100 - 50;
            float z =rnd.nextFloat() * -200;

            if(rnd.nextInt() * 100 > 50){
                sceneManager.addEntity(new Entity(model, new Vector3f(x, 2, z), new Vector3f(rnd.nextFloat() * 180, rnd.nextFloat() * 180, 0f), 1));
            }
            else{
                sceneManager.addEntity(new Entity(model1, new Vector3f(x,2,z), new Vector3f(rnd.nextFloat() * 180, rnd.nextFloat() * 180, 0f), 10));
            }


        }

        //entities.add(new Entity(model, new Vector3f(0, 0, -2f), new Vector3f(0, 0,0), 1));

        //entity = new Entity(model, new Vector3f(-5, 0, -5), new Vector3f(0, 0, 0), 10);

        float lightIntensity = 1.0f;
        //point light
        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);
        Vector3f lightColor = new Vector3f(1,1,1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity);

        //spotlight
        lightIntensity = 5f;
        Vector3f coneDir = new Vector3f(50,-50,1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), lightIntensity, 0,0,1), coneDir, cutoff);
        lightColor = new Vector3f(200f, 0f, 0f);
        SpotLight spotLight1 = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), lightIntensity, 0,0,1), coneDir, cutoff);
        spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));


        //directional light
        lightPosition = new Vector3f(-1,-10,0);
        lightColor = new Vector3f(1,1,1);
        sceneManager.setDirectionalLight(new DirectionalLight(lightColor,lightPosition, lightIntensity));

        sceneManager.setPointLights(new PointLight[]{pointLight});
        //pointLights = new PointLight[]{pointLight};
        sceneManager.setSpotLights(new SpotLight[]{spotLight, spotLight1});
        //spotLights = new SpotLight[]{spotLight, spotLight1};
    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W)){
            cameraInc.z = -1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_S)){
            cameraInc.z = 1;
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_A)){
            cameraInc.x = -1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_D)){
            cameraInc.x = 1;
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)){
            cameraInc.y = -1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            cameraInc.y = 1;
        }


        if(window.isKeyPressed(GLFW.GLFW_KEY_O)){
            sceneManager.getPointLight(0).getPosition().x += 0.1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_P)){
            sceneManager.getPointLight(0).getPosition().x -= 0.1f;
        }

        float lightPos = sceneManager.getSpotLights()[0].getPointLight().getPosition().z;
        if(window.isKeyPressed(GLFW.GLFW_KEY_N)){
            sceneManager.getSpotLight(0).getPointLight().getPosition().z = lightPos + 0.1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_M)){
            sceneManager.getSpotLight(0).getPointLight().getPosition().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(MouseInput mouseInput) {

        float cameraStep = Consts.CAMERA_STEP;


        if(mouseInput.isRightButtonPress()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENS, rotVec.y * Consts.MOUSE_SENS, 0);
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
            cameraStep = Consts.CAMERA_SPRINT_STEP;
        }

        camera.movePosition(cameraInc.x * cameraStep, cameraInc.y * cameraStep, cameraInc.z * cameraStep);

        for(Entity entity : sceneManager.getEntities()){
            entity.incRotation(.05f, 0.025f, 0f);
        }

        sceneManager.incLightAngle(0.05f);
        if(sceneManager.getLightAngle() > 90){
            sceneManager.getDirectionalLight().setIntensity(0);
            if(sceneManager.getLightAngle() >= 360){
                sceneManager.setLightAngle(-90);
            }
        }
        else if(sceneManager.getLightAngle() <= -80 || sceneManager.getLightAngle() >= 80){
            float factor = 1 - (Math.abs(sceneManager.getLightAngle()) - 80) / 10.0f;
            sceneManager.getDirectionalLight().setIntensity(factor);
            sceneManager.getDirectionalLight().getColor().y = Math.max(factor, 0.9f);
            sceneManager.getDirectionalLight().getColor().z = Math.max(factor, 0.5f);
        }
        else{
            sceneManager.getDirectionalLight().setIntensity(1);
            sceneManager.getDirectionalLight().getColor().x = 1;
            sceneManager.getDirectionalLight().getColor().y = 1;
            sceneManager.getDirectionalLight().getColor().z = 1;

        }

        double angRad = Math.toRadians(sceneManager.getLightAngle());
        sceneManager.getDirectionalLight().getDirection().x = (float) Math.sin(angRad);
        sceneManager.getDirectionalLight().getDirection().y = (float) Math.cos(angRad);

        for(Entity entity : sceneManager.getEntities()){
            renderer.processEntities(entity);
        }

        for(Terrain terrain : sceneManager.getTerrains()){
            renderer.processTerrain(terrain);
        }
    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        renderer.render(camera, sceneManager);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
