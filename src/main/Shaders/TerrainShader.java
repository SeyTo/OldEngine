package main.Shaders;

import main.Entities.Helpers.Camera;
import main.Entities.Light;
import main.Tools.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

/**
 * Created by rj on 6/9/2015.
 */
public class TerrainShader extends ShaderProgram{

    private static final int MAX_LIGHTS = 3;

    private static String VERTEX_SHADER_FILE = "src/main/Shaders/shaders/TerrainVertexShader.txt";
    private static String FRAGMENT_SHADER_FILE = "src/main/Shaders/shaders/TerrainFragmentShader.txt";

    private int transformation_variable;
    private int projection_variable;
    private int view_variable;
    private int lightPosition_variable[];
    private int lightColor_variable[];
    private int lightAttenuation_variable[];
    private int shineDamper_variable;
    private int reflectivity_variable;
    private int backgroundTexture_variable;
    private int rTexture_variable;
    private int gTexture_variable;
    private int bTexture_variable;
    private int blendMap_variable;
    private int skyColor_variable;
    private int clipPlane_variable;


    public TerrainShader(){
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
        bindAttribute(2, "normal");
    }

    protected void getAllUniformVariables(){
        transformation_variable = super.getUniformVariable("transformationMatrix");
        projection_variable = super.getUniformVariable("projectionMatrix");
        view_variable = super.getUniformVariable("viewMatrix");
        shineDamper_variable = super.getUniformVariable("shineDamper");
        reflectivity_variable = super.getUniformVariable("reflectivity");
        backgroundTexture_variable = super.getUniformVariable("backgroundTexture");
        rTexture_variable = super.getUniformVariable("rTexture");
        gTexture_variable = super.getUniformVariable("gTexture");
        bTexture_variable = super.getUniformVariable("bTexture");
        blendMap_variable = super.getUniformVariable("blendMap");
        skyColor_variable = super.getUniformVariable("skyColor");
        clipPlane_variable = super.getUniformVariable("clipPlane");

        lightPosition_variable = new int[MAX_LIGHTS];
        lightColor_variable = new int[MAX_LIGHTS];
        lightAttenuation_variable = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightPosition_variable[i] = super.getUniformVariable("lightPosition[" + i + "]");
            lightColor_variable[i] = super.getUniformVariable("lightColor[" + i + "]");
            lightAttenuation_variable[i] = super.getUniformVariable("lightAttenuation[" + i + "]");
        }
    }

    public void loadClipPlane(Vector4f plane){
        super.loadVector(clipPlane_variable, plane);
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(transformation_variable, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(projection_variable, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = Maths.createViewMatrix(camera);
        super.loadMatrix(view_variable, matrix);
    }

    public void loadLight(List<Light> lights){
        for (int i = 0; i < MAX_LIGHTS; i++) {
            super.loadVector(lightPosition_variable[i], lights.get(i).getPosition());
            super.loadVector(lightColor_variable[i], lights.get(i).getColor());
            super.loadVector(lightAttenuation_variable[i], lights.get(i).getAttenuation());
        }
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(shineDamper_variable, damper);
        super.loadFloat(reflectivity_variable, reflectivity);
    }

    public void loadMultiTextureSamplers(){
        super.loadInt(backgroundTexture_variable, 0);
        super.loadInt(rTexture_variable, 1);
        super.loadInt(gTexture_variable, 2);
        super.loadInt(bTexture_variable, 3);
        super.loadInt(blendMap_variable, 4);
    }

    public void loadSkyColor(float r, float g, float b){
        super.loadVector(skyColor_variable, new Vector3f(r,g,b));
    }
}
