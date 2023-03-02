package com.deadlist.core.utils;

import com.deadlist.core.ILogic;
import com.deadlist.core.MouseInput;
import com.deadlist.test.Launcher;
import org.joml.*;
import com.deadlist.core.Camera;

public class MousePicker implements ILogic {

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    public MousePicker(Camera camera, Matrix4f projectionMatrix){
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        viewMatrix = Transformation.getViewMatrix(camera);
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }

    private Vector3f calculateMouseRay(MouseInput mouseInput){
        float mouseX = mouseInput.getPosX();
        float mouseY = mouseInput.getPosY();
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeSpace(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords){
        Matrix4f invertedView = viewMatrix.invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalize();
        return mouseRay;
    }

    private Vector4f toEyeSpace(Vector4f clipCoords){
        Matrix4f invertedProjection = projectionMatrix.invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f,0f);
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY){
        float x = (2.0f * mouseX) / Launcher.getWindow().getWidth() - 1f;
        float y = (2.0f * mouseY) / Launcher.getWindow().getHeight() - 1f;
        return new Vector2f(x, y);
    }


    @Override
    public void init() throws Exception {

    }

    @Override
    public void input(MouseInput mouseInput) {

    }

    @Override
    public void update(MouseInput mouseInput) {
        viewMatrix = Transformation.getViewMatrix(camera);
        currentRay = calculateMouseRay(mouseInput);
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
