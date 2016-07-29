package main.Textures;

/**
 * Created by RaJU on 5/31/2015.
 */
public class ModelTexture {

    private int textureID;

    private float shineDamper = 1;      //camera closer to shine normal means more bright shine
    private float reflectivity = 0;     //strength of the reflection

    public ModelTexture(int textureID){
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}
