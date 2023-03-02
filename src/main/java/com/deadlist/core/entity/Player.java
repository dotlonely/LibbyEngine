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
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -75;
    private static final float JUMP_FORCE = 30;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float verticalSpeed = 0;

    private int maxJumps = 1;
    private int numJumps;

    private Terrain terrain;

    public Player(Model model, Vector3f pos, Vector3f rotation, float scale) {
        super(model, pos, rotation, scale);
    }

    public void move(){
        checkInputs();
        super.incRotation(0f, currentTurnSpeed * EngineManager.getDeltaTime(), 0f);
        float distance = currentSpeed * EngineManager.getDeltaTime();
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().y)));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().y)));
        verticalSpeed += GRAVITY * EngineManager.getDeltaTime();
        super.incPos(dx, verticalSpeed * EngineManager.getDeltaTime(), dz);
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
            this.currentSpeed = MOVE_SPEED;
        } else if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_S)){
            this.currentSpeed = -MOVE_SPEED;
        } else this.currentSpeed = 0;

        if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        }
        else if(Launcher.getWindow().isKeyPressed(GLFW.GLFW_KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        }
        else this.currentTurnSpeed = 0;

        if(Launcher.getWindow().isKeyPressed((GLFW.GLFW_KEY_SPACE)) && isCanJump()){
            jump();
        }

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
        move();
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

    public int getNumJumps(){
        return numJumps;
    }

    public String positionToString(){
        float xPos = Math.round(getPos().x);
        float zPos = Math.round(getPos().z);

        return ("(" + xPos + ", " + zPos + ")");
    }

    public float getCurrentSpeed(){
        return currentSpeed;
    }

}
