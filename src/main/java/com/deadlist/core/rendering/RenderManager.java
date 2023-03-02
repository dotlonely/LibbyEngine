package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.ShaderManager;
import com.deadlist.core.WindowManager;
import com.deadlist.core.entity.Entity;
import com.deadlist.core.entity.SceneManager;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.guis.GuiTexture;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.utils.Consts;
import com.deadlist.test.Launcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private final WindowManager window;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private GuiRenderer guiRenderer;

    public RenderManager(){
        window = Launcher.getWindow();
    }

    public void init() throws Exception{
        enableCulling();
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        guiRenderer = new GuiRenderer();

        entityRenderer.init();
        terrainRenderer.init();
        guiRenderer.init();

    }

    public static void enableWireframe(){
        GL13.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void disableWireframe(){
        GL13.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public static void renderLights(ShaderManager shader, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight){
        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Consts.SPECULAR_POWER);

        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("spotLights", spotLights[i], i);
        }

        numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("pointLights", pointLights[i], i);
        }

        shader.setUniform("directionalLight", directionalLight);
    }

    public void render(Camera camera, SceneManager sceneManager){
        clear();

        entityRenderer.renderer(camera, sceneManager);
        terrainRenderer.renderer(camera, sceneManager);
        guiRenderer.renderer(camera, sceneManager);
    }

    public void processEntities(Entity entity){
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if(entityList != null){
            entityList.add(entity);
        }
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void processTerrain(Terrain terrain){
        terrainRenderer.getTerrains().add(terrain);
    }

    public void processGuis(GuiTexture gui){
        List<GuiTexture> guiList = guiRenderer.getGuis().get(gui.getModel());
        if(guiList != null){
            guiList.add(gui);
        }
        else {
            List<GuiTexture> newGuiList = new ArrayList<>();
            newGuiList.add(gui);
            guiRenderer.getGuis().put(gui.getModel(), newGuiList);
        }
    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }




}
