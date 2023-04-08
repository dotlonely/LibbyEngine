package com.deadlist.core.entity;

import com.deadlist.core.EngineManager;
import com.deadlist.core.ILogic;
import com.deadlist.core.MouseInput;
import com.deadlist.core.entity.terrain.Terrain;
import com.deadlist.test.Launcher;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity implements ILogic{

    private static final float MOVE_SPEED = 30;
    private static final float GRAVITY = -75;
    private static final float JUMP_FORCE = 30;

    private float currentSpeedX = 0;
    private float currentSpeedZ = 0;
    private float verticalSpeed = 0;

    private float rollAngleX;
    private float rollAngleZ;
    private int maxJumps = 1;
    private int numJumps;

    private Terrain terrain;

    public Player(Model model, Vector3f pos, Vector3f rotation, float scale) {
        super(model, pos, rotation, scale);
    }

    public void move(MouseInput mouseInput){
        checkInputs();

        //Sets rotation of player based on mouse movement
        calculateAngleAroundPlayer(mouseInput);

        //calculates roll angle based on rotation for camera class
        calculateRollAngleX();
        calculateRollAngleZ();


        float sideDistance = currentSpeedX * EngineManager.getDeltaTime();
        float forwardDistance = currentSpeedZ * EngineManager.getDeltaTime();

        //Calculates change in forward distance
        float dxForward = (float) (forwardDistance * Math.sin(Math.toRadians(super.getRotation().y)));
        float dzForward = (float) (forwardDistance * Math.cos(Math.toRadians(super.getRotation().y)));

        //Calculate change in side distance (strafing)
        float dxSide = (float) (sideDistance * Math.sin(Math.toRadians(90 + super.getRotation().y)));
        float dzSide = (float) (sideDistance * Math.cos(Math.toRadians(90 + super.getRotation().y)));

        verticalSpeed += GRAVITY * EngineManager.getDeltaTime();

        super.incPos(dxForward, verticalSpeed * EngineManager.getDeltaTime(), dzForward);
        super.incPos(dxSide, 0f, dzSide);

        //TODO: Revisit camera roll later on
//        if(getCurrentSpeedX() > 0 ){
//            if(getRollAngleX() > .5 || getRollAngleX() < -.5){
//                    //setRotation(0f, getRotation().y, getRollAngleX());
//                if(getRotation().z < 5)
//                    getRotation().z++;
//            }
//            else if(getRollAngleZ() > .5 || getRollAngleZ() < -.5){
//                    //setRotation(0f, getRotation().y, getRollAngleZ());
//                if(getRotation().z < 5)
//                    getRotation().z++;
//            }
//        }
//        else if(getCurrentSpeedX() < 0) {
//            if(getRollAngleX() > .5 || getRollAngleX() < -.5){
//                //setRotation(0f, getRotation().y, -getRollAngleX());
//                if(getRotation().z > -5)
//                    getRotation().z--;
//            }
//            else if(getRollAngleZ() > .5 || getRollAngleZ() < -.5){
//                //setRotation(0f, getRotation().y, -getRollAngleZ());
//                if(getRotation().z > -5)
//                    getRotation().z--;
//            }
//
//        }
//        else {
//            setRotation(0f, getRotation().y, 0f);
//        }


        float terrainHeight = terrain.getHeightOfTerrain(super.getPos().x, super.getPos().z);

        if(super.getPos().y < terrainHeight){
            verticalSpeed = 0;
            super.getPos().y = terrainHeight;
            resetJumps();
        }
    }

    public void setTerrain(Terrain terrain){
       this.terrain = terrain;
    }

    private void jump(){
        this.verticalSpeed = JUMP_FORCE;
        useJump();
    }

    private void checkInputs(){

        if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_W)){
            this.currentSpeedZ = -MOVE_SPEED;
        } else if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_S)){
            this.currentSpeedZ = MOVE_SPEED;
        } else this.currentSpeedZ = 0;

        if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_D)){
            this.currentSpeedX = MOVE_SPEED;
        }
        else if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_A)){
            this.currentSpeedX = -MOVE_SPEED;
        } else this.currentSpeedX = 0;

        if(Launcher.getWindow().isKeyPressed((GLFW.GLFW_KEY_SPACE)) && isCanJump()){
            jump();
        }

    }

    private void calculateAngleAroundPlayer(MouseInput mouseInput){
        getRotation().y -= mouseInput.getDisplVec().y * 0.3;
        getRotation().y %= 360;
    }


    private void useJump(){
        numJumps--;
    }

    private void resetJumps(){
        numJumps = maxJumps;
    }

    private boolean isCanJump(){
        return numJumps > 0;
    }

    @Override
    public void init() throws Exception {
        numJumps = maxJumps;
    }


    @Override
    public void input(MouseInput mouseInput){
        move(mouseInput);
    }

    @Override
    public void update(MouseInput mouseInput) {
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }

    private void calculateRollAngleX(){
        rollAngleX = (float) Math.sin(Math.toRadians(90 + super.getRotation().y));
    }

    private void calculateRollAngleZ(){
        rollAngleZ = (float) Math.cos(Math.toRadians(90 + super.getRotation().y));
    }


    public int getNumJumps(){
        return numJumps;
    }

    public String positionToString(){
        float xPos = Math.round(getPos().x);
        float zPos = Math.round(getPos().z);

        return ("(" + xPos + ", " + zPos + ")");
    }

    public float getCurrentSpeedX(){
        return currentSpeedX;
    }
    public float getCurrentSpeedZ() { return currentSpeedZ; }

    public float getRollAngleX(){
        return rollAngleX;
    }

    public float getRollAngleZ(){
        return rollAngleZ;
    }

}
