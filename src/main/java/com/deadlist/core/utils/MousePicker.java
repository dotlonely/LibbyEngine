package com.deadlist.core.utils;

import com.deadlist.core.ILogic;
import com.deadlist.core.MouseInput;
import com.deadlist.test.Launcher;
import org.joml.*;
import com.deadlist.core.Camera;

public class MousePicker implements ILogic {

    private Vector3f currentRay;

    private Vector2f normDevCoords;

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


    // Calculate mouse ray based on mouseInput, center lock determines if device coords should be 0,0. aka mouse is locked to center screen
    private Vector3f calculateMouseRay(MouseInput mouseInput, boolean centerLock){
        float mouseX = mouseInput.getPosX();
        float mouseY = mouseInput.getPosY();
        Vector2f normalizedCoords;

        if(centerLock){
            normalizedCoords = new Vector2f(0f,0f);
        }
        else{
            normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        }

        normDevCoords = normalizedCoords;

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

    // Returns string of the normalized device coords for use in GUI interface
    public String getNormDevCoordsToString(){
        if(normDevCoords != null)
            return "Normalized Device Coords: \nX:" + normDevCoords.x + "\nY: " + normDevCoords.y;
        else return "NULL";
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

        //TODO: Expand this so some factor determined earlier in the process, aka choosing to make a FPS vs RTS
        currentRay = calculateMouseRay(mouseInput, false);
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
