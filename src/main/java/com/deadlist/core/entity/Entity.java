package com.deadlist.core.entity;

import org.joml.Vector3f;

import java.util.Vector;

public class Entity {

    private Model model;
    private Vector3f pos;
    private Vector3f rotation;

    private float scale;

    private int textureIndex = 0;

    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(Model model, int textureIndex, Vector3f pos, Vector3f rotation, float scale) {
        this.model = model;
        this.textureIndex = textureIndex;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public float getTextureXOffset(){
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset(){
        int row = textureIndex % model.getTexture().getNumberOfRows();
        return (float) row / (float) model.getTexture().getNumberOfRows();
    }

    public void incRotation(float x, float y, float z){
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void incPos(float x, float y, float z){
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    public void setPos(float x, float y, float z){
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incScale(float scale){
        this.scale += scale;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

}
