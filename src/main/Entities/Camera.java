
package main.Entities;

import main.Entities.Helpers.InvisiblePlayerFollower;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private static Vector3f position = new Vector3f(0,0,0); //TODO find another way to receive data from here
    private static float pitch = 225;
    private static float yaw;
    private float roll;

    private InvisiblePlayerFollower invisibleObj;    //This one I think should not be static

    private float CAM_distanceFromPlayer;
    private float CAM_angleAroundPlayer;

    public Camera(InvisiblePlayerFollower invisibleObj){
        this.invisibleObj = invisibleObj;
        CAM_distanceFromPlayer = 50;
    }

    public void processKey(){

        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance() + 30f;
        calculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180 - (invisibleObj.getRotY() + CAM_angleAroundPlayer);

        //Don't let cam through the ground
            if (position.y < 0){
                position.y = 0;
            }
    }

    //Camera Position Calculations according to a player or invisible helper

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
            float angleChange = Mouse.getDX()* 0.3f;
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
        float theta = invisibleObj.getRotY() + CAM_angleAroundPlayer;
        float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = invisibleObj.getPosition().x - offsetX;
        position.z = invisibleObj.getPosition().z - offsetZ;
        position.y = invisibleObj.getPosition().y + verticalDistance;
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
