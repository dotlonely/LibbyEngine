package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.ShaderManager;
import com.deadlist.core.entity.Entity;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.SceneManager;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRenderer implements IRenderer {

    ShaderManager shader;
    private Map<Model, List<Entity>> entities;



    public EntityRenderer() throws Exception{
        entities = new HashMap<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource("/shaders/entity_vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("/shaders/entity_fragment.glsl"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createUniform("specularPower");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createMaterialUniform("material");
        shader.createPointLightListUniform("pointLights", Consts.MAX_POINT_LIGHTS);
        shader.createSpotLightListUniform("spotLights", Consts.MAX_SPOT_LIGHTS);
    }

//    @Override
//    public void renderer(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
//        shader.bind();
//        shader.setUniforms("projectionMatrix", Launcher.getWindow().updateProjectionMatrix());
//
//        RenderManager.renderLights(shader, pointLights, spotLights, directionalLight);
//        for(Model model : entities.keySet()){
//            bind(model);
//            List<Entity> entityList = entities.get(model);
//            for(Entity entity : entityList){
//                prepare(entity, camera);
//                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
//            }
//            unbind();
//        }
//
//        entities.clear();
//        shader.unbind();
//    }

    @Override
    public void renderer(Camera camera, SceneManager sceneManager) {
        shader.bind();
        shader.setUniforms("projectionMatrix", Launcher.getWindow().updateProjectionMatrix());

        RenderManager.renderLights(shader, sceneManager.getPointLights(), sceneManager.getSpotLights(), sceneManager.getDirectionalLight());
        for(Model model : entities.keySet()){
            bind(model);
            List<Entity> entityList = entities.get(model);
            for(Entity entity : entityList){
                prepare(entity, camera);
                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbind();
        }

        entities.clear();
        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        shader.setUniform("material", model.getMaterial());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object entity, Camera camera) {
        shader.setUniform("textureSampler", 0);
        shader.setUniforms("transformationMatrix", Transformation.createTransformationMatrix((Entity) entity));
        shader.setUniforms("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public Map<Model, List<Entity>> getEntities() {
        return entities;
    }
}
