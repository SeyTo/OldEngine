package main.Entities;

import main.Models.TexturedModel;
import main.RenderEngine.DisplayManager;
import main.Terrains.Terrain;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Created by RaJU on 6/11/2015.
 */
//TODO improve gravity constant
public class Player extends Entity {

    public static final float RUN_SPEED = 60;
    private static final float TURN_SPEED = 200;

    private static final float GRAVITY = -98f;
    private static final float JUMP_POWER = 90f;
    private boolean isinAir = false;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float currentUpSpeed = 0;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    /**
     * Dependent on static boolean playerCam = true
     */
    public void move(List<Terrain> terrains){
        checkInputs();
        super.increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSeconds(),0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);

        currentUpSpeed += (GRAVITY * DisplayManager.getFrameTimeSeconds());
        float dy = currentUpSpeed * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0,dy,0);

        //Terrain Collision

        Terrain terrain = getCurrentTerrain(terrains);  //seek the current terrain where the player is standing
        float TERRAIN_HEIGHT = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (super.getPosition().y < TERRAIN_HEIGHT) {
            currentUpSpeed = 0;
            isinAir = false;
            super.getPosition().y = TERRAIN_HEIGHT;
        }
    }

    /**
     * Dependent on static boolean playerCam = true
     */
    private void checkInputs(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            if (!isinAir) {
                this.currentUpSpeed = JUMP_POWER;
                isinAir = true;
            }
        }
    }
}
