package com.deadlist.core;

import com.deadlist.core.entity.Player;
import org.joml.Vector3f;

public class Camera {

    private float distanceFromPlayer = 10;
    private float angleAroundPlayer = 0;
    private float angleAbovePlayer = 0;
    private float pitch = 20;
    private float yaw = 0;
    private float roll = 0;

    private float rollThreshold = 2;

    private float maxRotationX = 90;
    private float minRotationX = -90;

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
        calculateAngleAroundPlayer(mouseInput);
        calculateAngleAbovePlayer(mouseInput);

        float horizontalDistance = 0;
        float verticalDistance = 5;

        calculateCameraPosition(horizontalDistance, verticalDistance);

    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta = angleAroundPlayer + player.getRotation().y;
        float theta2 = angleAbovePlayer + player.getRotation().x;

        yaw = 180 - theta;
        pitch = theta2;

        position.x = player.getPos().x;
        position.z = player.getPos().z;
        position.y = player.getPos().y + verticalDistance;


        //calculateRoll();
        rotation.y = -player.getRotation().y;
        rotation.x = pitch;
        rotation.z = player.getRotation().z;
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

    private void calculateRoll(){
//        if(player.getCurrentSpeedX() > 0){
//            if(roll < rollThreshold){
//                roll = player.getRollAngle() * 2f;
//            }
//        }
//        else if(player.getCurrentSpeedX() < 0) {
//            if(roll > -rollThreshold){
//                roll = -player.getRollAngle() * 2f;
//            }
//        }
//        else {
////            if(roll > 0){
////                roll--;
////            }
////            else if (roll < 0){
////                roll++;
////            }
//            roll = 0;
//        }
        rotation.z = player.getRotation().z;
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

    //Allows player to look left and right
    private void calculateAngleAroundPlayer(MouseInput mouseInput){
            float angleChange = mouseInput.getDisplVec().y * 0.3f;
            angleAroundPlayer -= angleChange;
    }

    //Allows player to look up and down
    private void calculateAngleAbovePlayer(MouseInput mouseInput){
        float angleChange = mouseInput.getDisplVec().x * 0.3f;

        //Locks player camera between 90 and -90 degrees
        if(angleAbovePlayer > maxRotationX){
            angleAbovePlayer = 90;
        }
        else if(angleAbovePlayer < minRotationX){
            angleAbovePlayer = -90;
        }
        else{
            angleAbovePlayer += angleChange;
        }
    }

    public float getDistanceFromPlayer(){
        return distanceFromPlayer;
    }
}
