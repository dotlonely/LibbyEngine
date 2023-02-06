package com.deadlist.core.entity;

import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.lighting.DirectionalLight;
import com.deadlist.core.lighting.PointLight;
import com.deadlist.core.lighting.SpotLight;
import com.deadlist.core.utils.Consts;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    private List<Entity> entities;
    private List<Terrain> terrains;

    private Vector3f ambientLight;
    private SpotLight[] spotLights;
    private PointLight[] pointLights;
    private DirectionalLight directionalLight;

    private float lightAngle;
    private float spotAngle = 0;
    private float spotInc = 1;

    public SceneManager(float lightAngle){
        entities = new ArrayList<>();
        terrains = new ArrayList<>();
        ambientLight = Consts.AMBIENT_LIGHT;

        this.lightAngle = lightAngle;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity){
        this.entities.add(entity);
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }

    public void addTerrain(Terrain terrain){
        this.terrains.add(terrain);
    }

    public void setTerrains(List<Terrain> terrains) {
        this.terrains = terrains;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public void setAmbientLight(float x, float y, float z){
        this.ambientLight = new Vector3f(x,y,z);
    }

    public SpotLight[] getSpotLights() {
        return spotLights;
    }

    public SpotLight getSpotLight(int index){
        return spotLights[index];
    }

    public void setSpotLights(SpotLight[] spotLights) {
        this.spotLights = spotLights;
    }

    public PointLight[] getPointLights() {
        return pointLights;
    }

    public PointLight getPointLight(int index){
        return pointLights[index];
    }

    public void setPointLights(PointLight[] pointLights) {
        this.pointLights = pointLights;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public float getLightAngle() {
        return lightAngle;
    }

    public void setLightAngle(float lightAngle) {
        this.lightAngle = lightAngle;
    }
    public void incLightAngle(float lightAngle) {
        this.lightAngle += lightAngle;
    }

    public float getSpotAngle() {
        return spotAngle;
    }

    public void setSpotAngle(float spotAngle) {
        this.spotAngle = spotAngle;
    }

    public float getSpotInc() {
        return spotInc;
    }

    public void setSpotInc(float spotInc) {
        this.spotInc = spotInc;
    }


}
