package com.deadlist.core;

import com.deadlist.core.entity.Player;
import org.joml.Vector3f;

public class Camera {

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;
    private float pitch = 20;
    private float yaw;

    private Vector3f position;
    private Vector3f rotation;

    private Player player;

    public Camera(Player player){
        this.player = player;
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    public Camera(Player player, Vector3f position, Vector3f rotation){
        this.player = player;
        this.position = position;
        this.rotation = rotation;
    }


    private float calculateHorizonalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }
    public void movePlayerCamera(MouseInput mouseInput){
        calculateZoom(mouseInput);
        calculatePitch(mouseInput);
        calculateAngleAroundPlayer(mouseInput);

        float horizontalDistance = calculateHorizonalDistance();
        float verticalDistance = calculateVerticalDistance();

        calculateCameraPosition(horizontalDistance, verticalDistance);

    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = angleAroundPlayer + player.getRotation().y;
        float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));

        yaw = 180 - theta;

        position.x = player.getPos().x - xOffset;
        position.z = player.getPos().z - zOffset;
        position.y = player.getPos().y + verticalDistance;

        rotation.y = yaw;

    }

    public void movePosition(float x, float y, float z){
        if (z != 0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }

        if (x != 0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }

        position.y += y;
    }

    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void moveRotation(float x, float y, float z){
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    private void calculateZoom(MouseInput mouseInput){
        float zoomLevel = mouseInput.getYScroll() * 0.0005f;
        distanceFromPlayer -= zoomLevel;
        if(distanceFromPlayer < 5){
            distanceFromPlayer = 5;
        }
        else if(distanceFromPlayer > 50){
            distanceFromPlayer = 50;
        }
    }

    private void calculatePitch(MouseInput mouseInput){
        if(mouseInput.isLeftButtonPress()){
            float changePitch = mouseInput.getDisplVec().x * 0.1f;
            pitch -= changePitch;
            if(pitch < 1){
                pitch = 1;
            }
            else if (pitch > 90){
                pitch = 90;
            }
        }
    }

    private void calculateAngleAroundPlayer(MouseInput mouseInput){
        if(mouseInput.isRightButtonPress()){
            float angleChange = mouseInput.getDisplVec().y * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

    public float getDistanceFromPlayer(){
        return distanceFromPlayer;
    }
}
