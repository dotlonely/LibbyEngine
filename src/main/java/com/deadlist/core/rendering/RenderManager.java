package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.ShaderManager;
import com.deadlist.core.WindowManager;
import com.deadlist.core.entity.Entity;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.utils.Consts;
import com.deadlist.core.utils.Transformation;
import com.deadlist.core.utils.Utils;
import com.deadlist.test.Launcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {

    private final WindowManager window;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;

    public RenderManager(){
        window = Launcher.getWindow();
    }

    public void init() throws Exception{
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();

        entityRenderer.init();
        terrainRenderer.init();

    }

    public void bind(Model model){
        entityRenderer.bind(model);
    }

    public void unbind(){
        entityRenderer.unbind();
    }

    public void prepare(Entity entity, Camera camera){
        entityRenderer.prepare(entity, camera);
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

    public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights){
        clear();

        entityRenderer.renderer(camera, pointLights, spotLights, directionalLight);
        terrainRenderer.renderer(camera, pointLights, spotLights, directionalLight);
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

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }




}