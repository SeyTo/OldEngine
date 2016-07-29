package main.Skybox;

import main.Entities.Helpers.Camera;
import main.Shaders.ShaderProgram;
import main.Tools.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by RaJU on 6/23/2015.
 */
public class SkyBoxShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/main/Skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/Skybox/skyboxFragmentShader.txt";

    private int projectionMatrix_variable;
    private int viewMatrix_variable;
    private int fogColor_variable;

    public SkyBoxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f){
        super.loadMatrix(projectionMatrix_variable, matrix4f);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix4f = Maths.createViewMatrix(camera);
        matrix4f.m30 = matrix4f.m31 = matrix4f.m32 = 0;
        super.loadMatrix(viewMatrix_variable, matrix4f);
    }

    public void loadFog(float r, float g, float b){
        super.loadVector(fogColor_variable, new Vector3f(r,g,b));
    }

    protected void bindAttributes(){
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformVariables() {
        projectionMatrix_variable = super.getUniformVariable("projectionMatrix");
        viewMatrix_variable = super.getUniformVariable("viewMatrix");
        fogColor_variable = super.getUniformVariable("fogColor");
    }
}
