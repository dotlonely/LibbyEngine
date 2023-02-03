package com.deadlist.test;

import com.deadlist.core.*;
import com.deadlist.core.entity.Entity;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.Texture;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private DirectionalLight directionalLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;

    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0,0,0);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadObjModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/babyblue.png")), 1f);
        entity = new Entity(model, new Vector3f(-5, 0, -5), new Vector3f(0, 0, 0), 10);

        float lightIntensity = 1.0f;
        //point light
        Vector3f lightPosition = new Vector3f(-0.5f,-0.5f,-3.2f);
        Vector3f lightColor = new Vector3f(1,1,1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity);

        //spotlight
        Vector3f coneDir = new Vector3f(0,0,1);
        float cutoff = (float) Math.cos(Math.toRadians(180));
        SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), lightIntensity, 0,0,1), coneDir, cutoff);
        SpotLight spotLight1 = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), lightIntensity, 0,0,1), coneDir, cutoff);
        spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));


        //directional light
        lightPosition = new Vector3f(-1,-10,0);
        lightColor = new Vector3f(1,1,1);
        directionalLight = new DirectionalLight(lightColor,lightPosition, lightIntensity);

        pointLights = new PointLight[]{pointLight};
        spotLights = new SpotLight[]{spotLight, spotLight1};
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

        if(window.isKeyPressed(GLFW.GLFW_KEY_Z)){
            cameraInc.y = -1;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_X)){
            cameraInc.y = 1;
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_O)){
            pointLights[0].getPosition().x += 0.1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_P)){
            pointLights[0].getPosition().x -= 0.1f;
        }

        float lightPos = spotLights[0].getPointLight().getPosition().z;
        if(window.isKeyPressed(GLFW.GLFW_KEY_N)){
            spotLights[0].getPointLight().getPosition().z = lightPos + 0.1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_M)){
            spotLights[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }
    }

    @Override
    public void update(MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_STEP, cameraInc.y * Consts.CAMERA_STEP, cameraInc.z * Consts.CAMERA_STEP);

        if(mouseInput.isRightButtonPress()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENS, rotVec.y * Consts.MOUSE_SENS, 0);
        }

        //entity.incRotation(0.0f, 0.05f, 0.0f);
        entity.setRotation(0.0f, 180f, 0.0f);


        lightAngle += 0.05f;
        if(lightAngle > 90){
            directionalLight.setIntensity(0);
            if(lightAngle >= 360){
                lightAngle = -90;
            }
        }
        else if(lightAngle <= -80 || lightAngle >= 80){
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        }
        else{
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;

        }

        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0,0,window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        renderer.render(entity, camera, directionalLight, pointLights, spotLights);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
