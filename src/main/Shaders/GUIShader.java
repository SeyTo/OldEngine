package main.Shaders;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by rj on 6/12/2015.
 */
public class GUIShader extends ShaderProgram{

    private static String VERTEX_SHADER_FILE = "src/main/Shaders/shaders/GUIVertexShader.txt";
    private static String FRAGMENT_SHADER_FILE = "src/main/Shaders/shaders/GUIFragmentShader.txt";

    private int transformation_variable;

    public GUIShader(){
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    protected void getAllUniformVariables(){
        transformation_variable = super.getUniformVariable("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(transformation_variable, matrix);
    }

}
