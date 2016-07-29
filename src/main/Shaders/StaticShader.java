package main.Shaders;

import main.Entities.Helpers.Camera;
import main.Entities.Light;
import main.Tools.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

/**
 * Created by rj on 5/28/2015.
 */
public class StaticShader extends ShaderProgram{

    private static final int MAX_LIGHTS = 3;

    private static String VERTEX_SHADER_FILE = "src/main/Shaders/shaders/SimpleVertexShader.txt";
    private static String FRAGMENT_SHADER_FILE = "src/main/Shaders/shaders/SimpleFragmentShader.txt";

    private int transformation_variable;
    private int projection_variable;
    private int view_variable;
    private int lightPosition_variable[];
    private int lightColor_variable[];
    private int lightAttenutation_variable[];
    private int shineDamper_variable;
    private int reflectivity_variable;
    private int skyColor_variable;
    private int clipPlane_variable;

    public StaticShader(){
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
        skyColor_variable = super.getUniformVariable("skyColor");
        clipPlane_variable = super.getUniformVariable("clipPlane");

        lightPosition_variable = new int[MAX_LIGHTS];
        lightColor_variable = new int[MAX_LIGHTS];
        lightAttenutation_variable = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightPosition_variable[i] = super.getUniformVariable("lightPosition[" + i + "]");
            lightColor_variable[i] = super.getUniformVariable("lightColor[" + i + "]");
            lightAttenutation_variable[i] = super.getUniformVariable("lightAttenuation[" + i + "]");
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

    public void loadLights(List<Light> light){
        for (int i = 0; i < MAX_LIGHTS; i++) {
            super.loadVector(lightPosition_variable[i], light.get(i).getPosition());
            super.loadVector(lightColor_variable[i], light.get(i).getColor());
            super.loadVector(lightAttenutation_variable[i], light.get(i).getAttenuation());
        }


    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(shineDamper_variable, damper);
        super.loadFloat(reflectivity_variable, reflectivity);
    }

    public void loadSkyColor(float r, float g, float b){
        super.loadVector(skyColor_variable, new Vector3f(r, g, b));
    }
}
