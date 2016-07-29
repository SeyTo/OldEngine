package main.Textures;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by rj on 6/12/2015.
 */
public class GUITexture {

    private int textureID;
    private Vector2f position;
    private Vector2f scale;
    private float rotZ;

    public GUITexture(int textureID, Vector2f position, Vector2f scale, float rotZ) {
        this.textureID = textureID;
        this.position = position;
        this.scale = scale;
        this.rotZ = rotZ;
    }

    public int getTextureID() {
        return textureID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public float getRotZ() {
        return rotZ;
    }
}
