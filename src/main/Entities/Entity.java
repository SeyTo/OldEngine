package main.Entities;

import main.Game;
import main.Models.TexturedModel;
import main.Terrains.Terrain;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;


public class Entity {

    private TexturedModel model;    //Entity doesnt need to neccessarily have a model
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    //Basic Movement

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

    //All about terrain

    public Terrain getCurrentTerrain(List<Terrain> terrain){
        int gridSize = terrain.get(0).getSize();       //TODO ASSUMING all the terrains have the same size(const) and vertex No.(const)
        int gridX = (int)Math.floor(getPosition().getX() / (float)gridSize);    //player is in gridX
        int gridZ = (int)Math.floor(getPosition().getZ() / (float)gridSize);

        for (Terrain terra : terrain) {
            if (terra.getGridX() == gridX && terra.getGridZ() == gridZ){
                return terra;
            }
        }

        System.out.println("The terrain was selected 0 something wrong with method \"getCurrentTerrain()\"");
        Game.exit = true;        //TODO remove this, there must be a better way to do this
        return null;
    }

    //Beans

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Entity setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Entity setPositionY(float Y) {
        this.position.setY(Y);
        return this;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
