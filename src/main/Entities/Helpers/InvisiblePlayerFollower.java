package main.Entities.Helpers;

import main.Entities.Entity;
import main.Entities.Player;
import main.Models.TexturedModel;
import main.RenderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by RaJU on 6/11/2015.
 * Created this so that camera can move in player mode or free mode.
 */
public class InvisiblePlayerFollower extends Helper{

    Entity player;

    public static boolean playerCam = false;

    public final float RUN_SPEED = 100;
    private final float TURN_SPEED = 200;
    private final float FLY_SPEED = 60;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float currentUpSpeed = 0;

    public InvisiblePlayerFollower(Entity player){
        super(new Vector3f(0,0,0),0,0,0);
        this.player = player;

        position = new Vector3f();
        this.position.x = 48.19f;
        this.position.y = 26.17f;
        this.position.z = 70.76f;   System.out.printf("Player Position: \n%f , %f , %f\n",player.getPosition().x, player.getPosition().y, player.getPosition().z);

        this.rotX = player.getRotX();
        this.rotY = -135.0f;
        this.rotZ = player.getRotZ();
    }

    public void tick(){
        if (playerCam) {
            this.position = player.getPosition();
            this.rotX = player.getRotX();
            this.rotY = player.getRotY();
            this.rotZ = player.getRotZ();
        } else {
            checkInputs();

            increaseRotation(0,currentTurnSpeed * DisplayManager.getFrameTimeSeconds(),0);
            float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
            float dx = (float)(distance * Math.sin(Math.toRadians(getRotY())));
            float dz = (float)(distance * Math.cos(Math.toRadians(getRotY())));
            increasePosition(dx, 0, dz);

            float dy = currentUpSpeed * DisplayManager.getFrameTimeSeconds();
            increasePosition(0,dy,0);
        }
    }

    /**
     * Dependent on Static boolean playerCam = false
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
            this.currentUpSpeed = FLY_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_C)){
            this.currentUpSpeed = -FLY_SPEED;
        } else {
            this.currentUpSpeed = 0;
        }
    }

    //Beans

    public Vector3f getPosition() {
        return position;
    }

    public void increaseRotation(float rotX, float rotY, float rotZ){
        this.rotX += rotX;
        this.rotY += rotY;
        this.rotZ += rotZ;
    }

    public void increasePosition(float x, float y, float z){
            this.position.x += x;
            this.position.y += y;
            this.position.z += z;
    }


    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }
}
