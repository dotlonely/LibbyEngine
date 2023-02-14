package com.deadlist.core.utils;

import com.deadlist.core.Camera;
import com.deadlist.core.entity.Entity;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.core.guis.GuiTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;


public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(entity.getPos())
                .rotateX((float) Math.toRadians(entity.getRotation().x))
                .rotateY((float) Math.toRadians(entity.getRotation().y))
                .rotateZ((float) Math.toRadians(entity.getRotation().z))
                .scale(entity.getScale());

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Terrain terrain){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(terrain.getPosition()).scale(1);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(GuiTexture gui){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(gui.getPosition().x, gui.getPosition().y, 0f).scale(gui.getScale().x, gui.getScale().y, 1f);

        return matrix;
    }

    public static Matrix4f getViewMatrix(Camera camera){
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1,0,0))
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0,1,0))
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0,0,1));
        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }
}
