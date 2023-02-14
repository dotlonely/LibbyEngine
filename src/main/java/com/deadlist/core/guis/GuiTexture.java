package com.deadlist.core.guis;

import com.deadlist.core.ObjectLoader;
import com.deadlist.core.entity.Model;
import com.deadlist.core.entity.Texture;
import org.joml.Vector2f;

public class GuiTexture {

    private Model model;
    private int texture;
    private Vector2f position;
    private Vector2f scale;

    public GuiTexture(int texture, ObjectLoader loader, Vector2f position, Vector2f scale) throws Exception {
        this.model = generateGui(loader);
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    private Model generateGui(ObjectLoader loader){
          float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
          return loader.loadModel(positions);
    }

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public Model getModel(){
        return model;
    }

}
