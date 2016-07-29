package main.Entities.Helpers;

import main.Entities.Player;
import main.Entities.Text;
import main.Game;
import main.RenderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.text.PlainDocument;

//TODO restructure to move freely
public class Camera {
    private static Vector3f position = new Vector3f(0,60,0); //TODO find another way to receive data from here
    private static float pitch = 0;
    private static float yaw = 0;
    private float roll;

    private Player master;

    private float CAM_distanceFromPlayer;
    private float CAM_angleAroundPlayer;

    private float RUN_SPEED = 80f;
    private float STRAFE_SPEED = 80f;
    private float UP_SPEED = 80f;
    private float currentSpeed = 0;
    private float currentStrafeSpeed = 0;
    private float currentVerticalSpeed = 0;

    public boolean checkInput=false;

    Text graphInfo;

    public Camera(){
        CAM_distanceFromPlayer = 50;
        setMaster(null);
        graphInfo = new Text(new Vector2f(-0.9f, -0.9f), new Vector2f(0.04f,0.04f));
        Game.texts.add(graphInfo);
    }

    //TODO setMaster for camera
    public void setMaster(Player object){
        master = object;
    }

    public Player getMaster() { return master; }

    public void processKey(){
        if (master == null){
            processKeyNoMaster();
        } else {
            processKeyWMaster();
        }
    }

    private void processKeyNoMaster(){
        calculatePitch();
        calculateAngleAroundPlayer();
        yaw = 180 - CAM_angleAroundPlayer;
        checkInputs();
        if (checkInput) {
            Vector3f offset = calculateCameraMovementForce();
            position.x += offset.x;
            position.y += offset.y;
            position.z += offset.z;
        }
        checkInput = false;

    }

    private Vector3f calculateCameraMovementForce(){
        float theta = -CAM_angleAroundPlayer - 90;

        float forwardDistance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float strafeDistance = currentStrafeSpeed * DisplayManager.getFrameTimeSeconds();
        float verticalDistance = currentVerticalSpeed * DisplayManager.getFrameTimeSeconds();

        float offsetX = (float)(forwardDistance * Math.cos(Math.toRadians(theta))) +
                        (float)(strafeDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(forwardDistance * Math.sin(Math.toRadians(theta))) +
                        (float)(strafeDistance * Math.cos(Math.toRadians(theta)));
        float offsetY = /*(float)(forwardDistance * Math.sin(pitch)/*) +*/
                        (verticalDistance);

        graphInfo.setText("Theta: " + theta + "OffsetX : " + offsetX + "OffsetY : " + offsetY + " OffsetZ : " + offsetZ);

        while (Keyboard.isKeyDown(Keyboard.KEY_P)){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new Vector3f(offsetX, offsetY, offsetZ); //increase val
    }

    private void checkInputs(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = -RUN_SPEED;
            checkInput = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = RUN_SPEED;
            checkInput = true;
        } else {
            this.currentSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentStrafeSpeed = STRAFE_SPEED;
            checkInput = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentStrafeSpeed = -STRAFE_SPEED;
            checkInput = true;
        } else {
            this.currentStrafeSpeed = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            this.currentVerticalSpeed = UP_SPEED;
            checkInput = true;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_C)){
            this.currentVerticalSpeed = -UP_SPEED;
            checkInput = true;
        } else {
            this.currentVerticalSpeed = 0;
        }
    }

    //Camera Position Calculations according to a player or invisible helper

    private void processKeyWMaster(){
        calculateZoom();

        calculatePitch();
        calculateAngleAroundPlayer();

        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance() + 30f;
        calculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180 - (master.getRotY() + CAM_angleAroundPlayer);

        //TODO Don't let cam through the ground
    }

    private void calculateZoom(){
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        CAM_distanceFromPlayer -= zoomLevel;
    }



    private void calculatePitch(){
        if (Mouse.isButtonDown(1)){
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
        }
    }

    private void calculateAngleAroundPlayer(){
        if (Mouse.isButtonDown(1)){
            float angleChange = Mouse.getDX()* 0.6f;
            CAM_angleAroundPlayer -= angleChange;
        }
    }

    private float calculateHorizontalDistance(){
        return (float) (CAM_distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (CAM_distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }



    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float theta =master.getRotY() + CAM_angleAroundPlayer;
        float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = master.getPosition().x - offsetX;
        position.z = master.getPosition().z - offsetZ;
        position.y = master.getPosition().y + verticalDistance;
    }

    //Beans

    public static Vector3f getPosition() {
        return position;
    }

    public static float getPitch() {
        return pitch;
    }

    public static float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setYaw(float yaw){
        this.yaw = yaw;
    }
}
