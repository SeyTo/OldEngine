package main.Shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by RaJU on 6/13/2015.
 */
public class TextShader extends ShaderProgram {

    private static String VERTEX_SHADER_FILE = "src/main/Shaders/shaders/TextGUIVertexShader.txt";
    private static String FRAGMENT_SHADER_FILE = "src/main/Shaders/shaders/TextGUIFragmentShader.txt";

    private int transformation_variable;
    private int sheetCoord_variable;

    public TextShader(){
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    protected void getAllUniformVariables(){
        transformation_variable = super.getUniformVariable("transformationMatrix");
        sheetCoord_variable = super.getUniformVariable("sheetCoord");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(transformation_variable, matrix);
    }

    public void loadSheetCoordVector(Vector2f vector){ super.loadVector(sheetCoord_variable, vector);}
}
