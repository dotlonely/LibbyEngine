package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.ShaderManager;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.SceneManager;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.entity.terrain.TerrainTexturePack;
import com.deadlist.core.utils.Consts;
import com.deadlist.core.utils.Transformation;
import com.deadlist.core.utils.Utils;
import com.deadlist.test.Launcher;
import org.lwjgl.opengl.*;

import java.util.ArrayList;
import java.util.List;

public class TerrainRenderer implements IRenderer {

    ShaderManager shader;

    private List<Terrain> terrains;


    public TerrainRenderer() throws Exception{
        terrains = new ArrayList<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource("/shaders/terrain_vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("/shaders/terrain_fragment.glsl"));
        shader.link();
        shader.createUniform("backgroundTexture");
        shader.createUniform("redTexture");
        shader.createUniform("greenTexture");
        shader.createUniform("blueTexture");
        shader.createUniform("blendMap");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createUniform("specularPower");
        shader.createUniform("skyColor");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createMaterialUniform("material");
        shader.createPointLightListUniform("pointLights", Consts.MAX_POINT_LIGHTS);
        shader.createSpotLightListUniform("spotLights", Consts.MAX_SPOT_LIGHTS);
    }

    @Override
    public void renderer(Camera camera, SceneManager sceneManager) {
        shader.bind();
        shader.setUniforms("projectionMatrix", Launcher.getWindow().updateProjectionMatrix());

        RenderManager.renderLights(shader, sceneManager.getPointLights(), sceneManager.getSpotLights(), sceneManager.getDirectionalLight());
        for(Terrain terrain : terrains){
            bind(terrain.getModel());
            prepare(terrain, camera);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbind();
        }

        terrains.clear();
        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        shader.setUniform("backgroundTexture", 0);
        shader.setUniform("redTexture", 1);
        shader.setUniform("greenTexture",2);
        shader.setUniform("blueTexture", 3);
        shader.setUniform("blendMap", 4);

        shader.setUniform("skyColor", Consts.SKY_COLOR);
        shader.setUniform("material", model.getMaterial());
    }

    private void bindTextures(Terrain terrain){
        TerrainTexturePack texturePack = terrain.getTerrainTexturePack();
        GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRedTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGreenTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBlueTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object terrain, Camera camera) {

        bindTextures((Terrain) terrain);

        shader.setUniforms("transformationMatrix", Transformation.createTransformationMatrix((Terrain) terrain));
        shader.setUniforms("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }
}
