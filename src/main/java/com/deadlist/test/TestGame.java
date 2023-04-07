package com.deadlist.test;

import com.deadlist.core.*;
import com.deadlist.core.entity.*;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.entity.terrain.TerrainTexture;
import com.deadlist.core.entity.terrain.TerrainTexturePack;
import com.deadlist.core.guis.GuiTexture;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.rendering.RenderManager;
import com.deadlist.core.utils.MousePicker;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class TestGame implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private  SceneManager sceneManager;
    private Camera camera;
    private Player player;
    private Terrain terrain;
    Vector3f cameraInc;
    private Entity lightGizmo;
    private PointLight playerLight;

    private MousePicker picker;

    private boolean lockMouse = false;


    //TODO: Figure out why player is colliding with something at position x,z -255 and x,z 255
    // Now its not just at 255 sometimes you can go further and get stuck at different points


    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera(player);
        cameraInc = new Vector3f(0,0,0);
        sceneManager = new SceneManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

//        //Locks mouse to center of window
//        window.lockMouseToWindow(true);

        picker = new MousePicker(camera, Launcher.getWindow().getProjectionMatrix());

        //camera.setPosition(0, 10, 5);
        GuiTexture catGuiTwo = new GuiTexture(loader.loadTexture("textures/nasa.png"), loader, new Vector2f(0.75f, 0.75f), new Vector2f(0.1f, 0.1f));
        sceneManager.addGui(catGuiTwo);

        GuiTexture catGui = new GuiTexture(loader.loadTexture("textures/cat.png"), loader, new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        sceneManager.addGui(catGui);


        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/medGreen.png"));
        TerrainTexture redTexture = new TerrainTexture(loader.loadTexture("textures/lightGreen.png"));
        TerrainTexture greenTexture = new TerrainTexture(loader.loadTexture("textures/darkGreen.png"));
        TerrainTexture blueTexture = new TerrainTexture(loader.loadTexture("textures/lightBrown.png"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, redTexture, greenTexture, blueTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/blendMap.png"));

        terrain = new Terrain(new Vector3f(0f, -1f, -800f), loader,
                new Material(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f), 0.1f), texturePack, blendMap, "perlin_noise");

        sceneManager.addTerrain(terrain);

        Random rnd = new Random();

        Model playerModel = loader.loadObjModel("/models/cube.obj");
        playerModel.setTexture(new Texture(loader.loadTexture("textures/babyblue.png")), 1f);

        player = new Player(playerModel, new Vector3f(0, 0, -10), new Vector3f(0f, 180f, 0f), 1.5f);
        sceneManager.addEntity(player);

        camera = new Camera(player);

        Model grass = loader.loadObjModel("/models/grassModel.obj");
        grass.setTexture(new Texture(loader.loadTexture("textures/darkTan.png")), 1f);
        grass.getTexture().setHasTransparency(true);

        Model tree = loader.loadObjModel("/models/lowPolyTree.obj");
        tree.setTexture(new Texture(loader.loadTexture("textures/lowPolyTree.png")), 1f);

        Model fern = loader.loadObjModel("/models/fern.obj");
        fern.setTexture(new Texture(loader.loadTexture("textures/maroonish.png")), 1f);
        fern.getTexture().setNumberOfRows(2);
        fern.getTexture().setHasTransparency(true);

        Model brazier = loader.loadObjModel("/models/brazier.obj");
        brazier.setTexture(new Texture(loader.loadTexture("textures/medPurple.png")), 1f);

        Model gizmo = loader.loadObjModel("/models/cube.obj");
        gizmo.setTexture(new Texture(loader.loadTexture("textures/white.png")), 1f);



        for (int i = 0; i < 200; i++) {
            float x = rnd.nextFloat() * 200;
            float z = rnd.nextFloat() * -400;
            float y = terrain.getHeightOfTerrain(x, z);
            float objectOffset = -1;

//            if(i < 50){
//                sceneManager.addEntity(new Entity(grass, new Vector3f(x, y + objectOffset, z), new Vector3f(0f, 0f, 0f), 1f));
//            }
//            else if(i < 100 && i > 50){
//                sceneManager.addEntity(new Entity(brazier, new Vector3f(x, y + objectOffset, z), new Vector3f(0f, 0f, 0f), 1f));
//            }
//            else{
//                sceneManager.addEntity(new Entity(fern, rnd.nextInt(4), new Vector3f(x, y + objectOffset, z), new Vector3f(0f, 0f, 0f), 1f));
//            }

            if(i < 100){
               sceneManager.addEntity(new Entity(grass, new Vector3f(x, y + objectOffset, z), new Vector3f(0f, 0f, 0f), 1f));
            }
            else if(i > 100){
                sceneManager.addEntity(new Entity(fern, rnd.nextInt(4), new Vector3f(x, y + objectOffset, z), new Vector3f(0f, 0f, 0f), 1f));
            }

        }
        Entity entity1 = new Entity(brazier, new Vector3f(0,terrain.getHeightOfTerrain(0,0),0), new Vector3f(0, 0, 0), 1);
        Entity entity2 = new Entity(brazier, new Vector3f(5,terrain.getHeightOfTerrain(5,-5),-5), new Vector3f(0, 0, 0), 1);
        sceneManager.addEntity(entity1);
        sceneManager.addEntity(entity2);

        //sceneManager.addEntity(new Entity(brazier, new Vector3f(0,terrain.getHeightOfTerrain(0,0),0), new Vector3f(0, 0, 0), 1));
        //sceneManager.addEntity(new Entity(brazier, new Vector3f(5,terrain.getHeightOfTerrain(5,-5),-5), new Vector3f(0, 0, 0), 1));
        sceneManager.addEntity(new Entity(brazier, new Vector3f(10,terrain.getHeightOfTerrain(10,-10),-10), new Vector3f(0, 0, 0), 1));
        sceneManager.addEntity(new Entity(brazier, new Vector3f(15,terrain.getHeightOfTerrain(15,-15),-15), new Vector3f(0, 0, 0), 1));
        sceneManager.addEntity(new Entity(brazier, new Vector3f(20,terrain.getHeightOfTerrain(20,-20),-20), new Vector3f(0, 0, 0), 1));

        //Point lights on braziers
        float pointLightIntensity = 0.25f;
        Vector3f pointLightPosition = new Vector3f();
        Vector3f pointLightColor = new Vector3f(1,0.55f,0);
        //Vector3f pointLightColor = new Vector3f(1,1f,1);

       // PointLight pointLight = new PointLight(pointLightColor, new Vector3f(0,(terrain.getHeightOfTerrain(0,0) + 10),0), pointLightIntensity);
        PointLight pointLight = new PointLight(pointLightColor, new Vector3f(entity1.getPos().x, entity1.getPos().y + 18, entity1.getPos().z), pointLightIntensity);
        playerLight = new PointLight(pointLightColor, new Vector3f(player.getPos().x, entity1.getPos().y + 2, entity1.getPos().z), pointLightIntensity);
        playerLight.setIntensity(0f);


        lightGizmo = new Entity(gizmo, pointLight.getPosition(), new Vector3f().zero(), .5f);
        sceneManager.addEntity(lightGizmo);



        float lightIntensity = 0.0f;

//        //point light
//        float pointLightIntensity = 0f;
//        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);
//        Vector3f lightColor = new Vector3f(1,1,1);
//        PointLight pointLight = new PointLight(lightColor, lightPosition, pointLightIntensity);

        Vector3f lightColor = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);

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

        sceneManager.setPointLights(new PointLight[]{pointLight, playerLight});
        //pointLights = new PointLight[]{pointLight};
        sceneManager.setSpotLights(new SpotLight[]{spotLight});
        //spotLights = new SpotLight[]{spotLight, spotLight1};

        player.setTerrain(terrain);
        player.init();


    }

    @Override
    public void input(MouseInput mouseInput) {

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

        if(window.isKeyReleased(GLFW.GLFW_KEY_TAB)){
            if(!lockMouse) {
                window.lockMouseToWindow(true);
                lockMouse = true;
            }
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_TAB)){
            if(lockMouse){
                window.lockMouseToWindow(false);
                lockMouse = false;
            }
        }


        player.move(mouseInput);
        camera.movePlayerCamera(mouseInput);

//        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)){
//            cameraInc.y = -1;
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
//            cameraInc.y = 1;
//        }

//        if(window.isKeyPressed(GLFW.GLFW_KEY_O)){
//            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
//                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z - 1));
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_P)){
//            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
//                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z + 1));
//        }
//
//        if(window.isKeyPressed(GLFW.GLFW_KEY_N)){
//            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x - 1,
//                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z));
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_M)){
//            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x + 1,
//                    sceneManager.getDirectionalLight().getDirection().y, sceneManager.getDirectionalLight().getDirection().z));
//        }
//
//        if(window.isKeyPressed(GLFW.GLFW_KEY_I)){
//            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
//                    sceneManager.getDirectionalLight().getDirection().y + 1, sceneManager.getDirectionalLight().getDirection().z));
//        }
//        if(window.isKeyPressed(GLFW.GLFW_KEY_U)){
//            sceneManager.getDirectionalLight().setDirection(new Vector3f(sceneManager.getDirectionalLight().getDirection().x,
//                    sceneManager.getDirectionalLight().getDirection().y - 1, sceneManager.getDirectionalLight().getDirection().z));
//        }

//
//        if(mouseInput.isRightButtonPress()){
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * Consts.MOUSE_SENS, rotVec.y * Consts.MOUSE_SENS, 0);
//        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_O)){
            sceneManager.getPointLight(0).incIntensity(-1 * EngineManager.getDeltaTime());
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_P)){
            sceneManager.getPointLight(0).incIntensity(1 * EngineManager.getDeltaTime());
        }


        if(window.isKeyPressed(GLFW.GLFW_KEY_I)){
            sceneManager.getPointLight(0).setPosition(new Vector3f(sceneManager.getPointLight(0).getPosition().x,
                    sceneManager.getPointLight(0).getPosition().y + (5 * EngineManager.getDeltaTime()),
                    sceneManager.getPointLight(0).getPosition().z));
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_U)){
            sceneManager.getPointLight(0).setPosition(new Vector3f(sceneManager.getPointLight(0).getPosition().x,
                    sceneManager.getPointLight(0).getPosition().y - (5 * EngineManager.getDeltaTime()),
                    sceneManager.getPointLight(0).getPosition().z));
        }


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

        picker.update(mouseInput);
        player.update(mouseInput);

        //System.out.println(player.positionToString());

        //System.out.println(picker.getCurrentRay().x+ " " + picker.getCurrentRay().y + " " + picker.getCurrentRay().z);
        //System.out.println("X: " + player.getRollAngleX() + "  ,  Z: " + player.getRollAngleZ());
        System.out.println(player.getRotation().y);


        //System.out.println(mouseInput.getPosX() + " , " + mouseInput.getPosY());

        lightGizmo.setPos(sceneManager.getPointLight(0).getPosition());

        lightGizmo.setPos(sceneManager.getPointLight(0).getPosition().x,
                sceneManager.getPointLight(0).getPosition().y,
                sceneManager.getPointLight(0).getPosition().z);

        lightGizmo.incRotation(0, 0.25f, 0.1f );
        playerLight.setPosition(player.getPos());


        for(Entity entity : sceneManager.getEntities()){
            renderer.processEntities(entity);
        }

        for(Terrain terrain : sceneManager.getTerrains()){
            renderer.processTerrain(terrain);
        }

        for(GuiTexture gui : sceneManager.getGuis()){
            renderer.processGuis(gui);
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
