package com.deadlist.test;

import com.deadlist.core.*;
import com.deadlist.core.entity.*;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.entity.terrain.TerrainTexture;
import com.deadlist.core.entity.terrain.TerrainTexturePack;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.rendering.RenderManager;
import com.deadlist.core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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
    private Player player;
    Vector3f cameraInc;


    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        //camera = new Camera(player);
        cameraInc = new Vector3f(0,0,0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();


        //camera.setPosition(0, 10, 5);

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grass.png"));
        TerrainTexture redTexture = new TerrainTexture(loader.loadTexture("textures/grassFlowers.png"));
        TerrainTexture greenTexture = new TerrainTexture(loader.loadTexture("textures/mossy.png"));
        TerrainTexture blueTexture = new TerrainTexture(loader.loadTexture("textures/path.png"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, redTexture, greenTexture, blueTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/blendMap.png"));

        Terrain terrain = new Terrain(new Vector3f(0f, -1f, -800f), loader,
                new Material(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 0.1f), texturePack, blendMap);
        Terrain terrain2 = new Terrain(new Vector3f(-800f, -1f, -800f), loader,
                new Material(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 0.1f), texturePack, blendMap);
        sceneManager.addTerrain(terrain);
        sceneManager.addTerrain(terrain2);

        Random rnd = new Random();

        Model playerModel = loader.loadObjModel("/models/girl.obj");
        playerModel.setTexture(new Texture(loader.loadTexture("textures/babyblue.png")), 1f);

        player = new Player(playerModel, new Vector3f(0, 0, -10), new Vector3f(0f, 180f, 0f), 0.05f);
        sceneManager.addEntity(player);

        camera = new Camera(player);

        Model grass = loader.loadObjModel("/models/grassModel.obj");
        grass.setTexture(new Texture(loader.loadTexture("textures/grassTexture.png")), 1f);
        grass.getTexture().setHasTransparency(true);
        //grass.getTexture().setUseFakeLighting(true);

        Model model1 = loader.loadObjModel("/models/lowPolyTree.obj");
        model1.setTexture(new Texture(loader.loadTexture("textures/lowPolyTree.png")), 1f);

        Model fern = loader.loadObjModel("/models/fern.obj");
        fern.setTexture(new Texture(loader.loadTexture("textures/fern.png")), 1f);
        fern.getTexture().setHasTransparency(true);
        //fern.getTexture().setUseFakeLighting(true);

        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 200;
            float y = rnd.nextFloat() * 150;
            float z =rnd.nextFloat() * -400;

            if(y < 50){
                sceneManager.addEntity(new Entity(grass, new Vector3f(x, -1f, z), new Vector3f(0f, 0f, 0f), 1f));
            }
            else if(y >= 50 && y < 100){
                sceneManager.addEntity(new Entity(model1, new Vector3f(x, -1f, z), new Vector3f(0f, 0f, 0f), .5f));
            }
            else{
                sceneManager.addEntity(new Entity(fern, new Vector3f(x, -1f, z), new Vector3f(0f, 0f, 0f), 1f));
            }

        }


        float lightIntensity = 1.0f;

        //point light
        float pointLightIntensity = 0f;
        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);
        Vector3f lightColor = new Vector3f(1,1,1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, pointLightIntensity);

        //spotlight
        float spotLightIntensity = 0f;
        Vector3f coneDir = new Vector3f(50,50,1);
        float cutoff = (float) Math.cos(Math.toRadians(90));
        SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), spotLightIntensity, 0,0,1), coneDir, cutoff);
        lightColor = new Vector3f(.7f, .5f, .1f);
        SpotLight spotLight1 = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), spotLightIntensity, 0,0,1), coneDir, cutoff);
        spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));


        //directional light
        lightPosition = new Vector3f(10,10,10);
        sceneManager.setDirectionalLight(new DirectionalLight(lightColor, lightPosition, lightIntensity));

        sceneManager.setPointLights(new PointLight[]{pointLight});
        //pointLights = new PointLight[]{pointLight};
        sceneManager.setSpotLights(new SpotLight[]{spotLight});
        //spotLights = new SpotLight[]{spotLight, spotLight1};

        player.init();
    }

    @Override
    public void input(WindowManager window, MouseInput mouseInput) {

//        cameraInc.set(0,0,0);
//        if(window.isKeyPressed(GLFW.GLFW_KEY_W)){
//            cameraInc.z = -1;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_S)){
//            cameraInc.z = 1;
//        }

//        if(window.isKeyPressed(GLFW.GLFW_KEY_A)){
//            cameraInc.x = -1;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_D)){
//            cameraInc.x = 1;
//        }

        player.move(window);
        camera.movePlayerCamera(mouseInput);

//        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)){
//            cameraInc.y = -1;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
//            cameraInc.y = 1;
//        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_O)){
            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z - 1));
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_P)){
            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z + 1));
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_N)){
            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x - 1,
                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z));
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_M)){
            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x + 1,
                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z));
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_I)){
            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
                    sceneManager.getDirectionalLight().getDirection().y + 1, sceneManager.getDirectionalLight().getDirection().z));
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_U)){
            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
                    sceneManager.getDirectionalLight().getDirection().y - 1, sceneManager.getDirectionalLight().getDirection().z));
        }

//
//        if(mouseInput.isRightButtonPress()){
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * Consts.MOUSE_SENS, rotVec.y * Consts.MOUSE_SENS, 0);
//        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_Y)){
            RenderManager.enableWireframe();
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_T)){
            RenderManager.disableWireframe();
        }

//        if(mouseInput.getIsInWindow()){
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * Consts.MOUSE_SENS, rotVec.y * Consts.MOUSE_SENS, 0);
//        }


    }

    @Override
    public void update(MouseInput mouseInput) {

        float cameraStep = Consts.CAMERA_STEP;
        System.out.println(camera.getDistanceFromPlayer());

        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
            cameraStep = Consts.CAMERA_SPRINT_STEP;
        }

        camera.movePosition(cameraInc.x * cameraStep, cameraInc.y * cameraStep, cameraInc.z * cameraStep);


        for(Entity entity : sceneManager.getEntities()){
            renderer.processEntities(entity);
        }

        for(Terrain terrain : sceneManager.getTerrains()){
            renderer.processTerrain(terrain);
        }

        mouseInput.endFrame();
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
