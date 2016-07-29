package main.Tools;

import main.Entities.Helpers.Camera;
import main.Game;
import main.RenderEngine.DisplayManager;
import main.Terrains.Terrain;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;
import java.util.Vector;

/**
 * Created by RaJU on 6/24/2015.
 */
public class MousePicker {

    private final int RECURSION_COUNT = 20; //TODO recursions depend on RAY_RANGE
    private final int RAY_RANGE = 600; //in openGL pixel distance

    private Vector3f currentRay;

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    Vector3f currentTerrainPoint;

    public MousePicker(Camera camera, Matrix4f projectionMatrix) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = Maths.createViewMatrix(camera);
    }

    /**
     * Methods needed to create a ray from which the position is calculated. Needs to be updated before calculating the actual position by mouse.
     */
    public void update(){
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)){
            currentTerrainPoint = binarySearch(0,0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    public Vector3f getCurrentTerrainPoint(){
        return currentTerrainPoint;
    }

    private Vector3f calculateMouseRay(){
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoords = getNormalizedDeviceCoords(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY){
        float x = (2f * mouseX) / Display.getWidth() - 1;
        float y = (2f * mouseY) / Display.getHeight() - 1;
        return new Vector2f(x,y);
    }

    private Vector4f toEyeCoords(Vector4f clipCoords){
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords){
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    public Vector3f getCurrentRay(){
        return currentRay;
    }

    /**
     * This methods is dependent on <b>update()</b> method.
     * @return
     */
    public Vector2f getNormalizedDistanceFromMouse(){
        float Xcam = ( currentRay.getX() * Camera.getPosition().y ) / currentRay.getY();
        float Zcam = ( currentRay.getZ() * Camera.getPosition().y ) / currentRay.getY();
        return new Vector2f(-Xcam, -Zcam);
    }

    //****Get Height Of Terrain According To Screen Mouse Position****//

    /**
     * Recreates an object position according to mice's position. Using binary search.
     * This methods is dependent on <b>MousePicker.update(), Class Camera</b> method.
     * @return
     */
    public Vector3f getRedefinedPosition(){
        Vector2f normalizedMouseDistance = getNormalizedDistanceFromMouse();
        float halfX = normalizedMouseDistance.getX() / 2;
        float halfZ = normalizedMouseDistance.getY() / 2;
        float halfY = Camera.getPosition().getY() / 2;
        count = 0;
        return binaryHeightSearch(halfX, halfY, halfZ);
    }

    public int count = 0;
    private Vector3f binaryHeightSearch(float halfX, float halfY, float halfZ){
        count++;

        float globalX =  halfX + Camera.getPosition().x;
        float globalZ =  halfZ + Camera.getPosition().z;
        float actualHeight = getCurrentTerrain(Game.terrains, globalX, globalZ).getHeightOfTerrain(globalX, globalZ);   //TODO this is costly

        float heightDifference = halfY - actualHeight;

        if (-5 < heightDifference && heightDifference < 5){//Height found within range -5 to 5
            System.out.println("Height Difference:" + heightDifference);

            return new Vector3f(globalX, actualHeight, globalZ);
        } else if (heightDifference <= -5){
            System.out.println("Up");
            halfX = halfX - (halfX / 2);
            halfZ = halfZ - (halfZ / 2);
            halfY = halfY + (halfY / 2);
            return binaryHeightSearch(halfX, halfY, halfZ);//up search`
        } else if (heightDifference >= 5){
            System.out.println("Down");
            halfX = halfX + (halfX / 2);
            halfZ = halfZ + (halfZ / 2);
            halfY = halfY - (halfY / 2);
            return binaryHeightSearch(halfX, halfY, halfZ); //down search
        }
            System.out.println("Something wrong with \"binaryHeightSearch()\"");
            System.exit(-1);
            return null;
    }

    /**
     * Get the current Terrain according to the global mouse position. Dependent on <b>Game.Terrains</b>
     * @param terrain
     * @param globalX
     * @param globalZ
     * @return
     */
    private Terrain getCurrentTerrain(List<Terrain> terrain, float globalX, float globalZ){
        int gridSize = terrain.get(0).getSize();       //TODO ASSUMING all the terrains have the same size(const) and vertex No.(const)
        int gridX = (int)Math.floor(globalX / (float)gridSize);    //player is in gridX
        int gridZ = (int)Math.floor(globalZ / (float)gridSize);

        for (Terrain terra : terrain) {
            if (terra.getGridX() == gridX && terra.getGridZ() == gridZ){
                return terra;
            }
        }

        System.out.println("The terrain was selected 0 something wrong with method \"getCurrentTerrain()\"");
        Game.exit = true;        //TODO remove this, there must be a better way to do this
        return null;
    }

    //Picking algorithm by ThinMatrix

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) { //TODO no need for ray passing
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            Vector3f endPoint = getPointOnRay(ray, half);
            Terrain terrain = getCurrentTerrain(Game.terrains, endPoint.getX(), endPoint.getZ());
            if (terrain != null) {
                return endPoint;
            } else {
                return null;
            }
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        Terrain terrain = getCurrentTerrain(Game.terrains, testPoint.getX(), testPoint.getZ());
        float height = 0;
        if (terrain != null) {
            height = terrain.getHeightOfTerrain(testPoint.getX(), testPoint.getZ());
        }
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

}
