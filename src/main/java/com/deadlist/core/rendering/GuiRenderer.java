package com.deadlist.core.rendering;

import com.deadlist.core.Camera;
import com.deadlist.core.ShaderManager;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.SceneManager;
import com.deadlist.core.guis.GuiTexture;
import com.deadlist.core.utils.Transformation;
import com.deadlist.core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiRenderer implements IRenderer{

    ShaderManager shader;
    private Map<Model, List<GuiTexture>> guis;

    public GuiRenderer() throws Exception {
        guis = new HashMap<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource("/shaders/gui_vertex.glsl"));
        shader.createFragmentShader(Utils.loadResource("/shaders/gui_fragment.glsl"));
        shader.link();
        shader.createUniform("guiTexture");
        shader.createUniform("transformationMatrix");
    }

    @Override
    public void renderer(Camera camera, SceneManager sceneManager) {
        shader.bind();

        for(Model model : guis.keySet()){
            bind(model);
            List<GuiTexture> guiList = guis.get(model);
            for(GuiTexture gui : guiList){
                prepare(gui, camera);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, model.getVertexCount());
            }
            unbind();
        }

        guis.clear();
        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getId());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object gui, Camera camera) {
        shader.setUniform("guiTexture", 0);
        shader.setUniforms("transformationMatrix", Transformation.createTransformationMatrix((GuiTexture) gui));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }

    public Map<Model, List<GuiTexture>> getGuis() {
        return guis;
    }
}
