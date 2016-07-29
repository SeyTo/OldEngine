package main.Water;

import main.Entities.Helpers.Camera;
import main.Shaders.ShaderProgram;
import main.Tools.Maths;
import org.lwjgl.util.vector.Matrix4f;


public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/main/Water/Shaders/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/main/water/Shaders/waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformVariables() {
		location_projectionMatrix = getUniformVariable("projectionMatrix");
		location_viewMatrix = getUniformVariable("viewMatrix");
		location_modelMatrix = getUniformVariable("modelMatrix");
		location_reflectionTexture = getUniformVariable("reflectionTexture");
		location_refractionTexture = getUniformVariable("refractionTexture");
	}

	public void connectTextures(){
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
