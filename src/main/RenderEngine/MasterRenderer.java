package main.RenderEngine;

import main.Entities.Helpers.Camera;
import main.Entities.Entity;
import main.Entities.Light;
import main.Game;
import main.Loaders.*;
import main.Models.TexturedModel;
import main.Shaders.GUIShader;
import main.Shaders.StaticShader;
import main.Shaders.TerrainShader;
import main.Shaders.TextShader;
import main.Skybox.SkyBoxRenderer;
import main.Water.WaterFrameBuffers;
import main.Water.WaterRenderer;
import main.Water.WaterShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rj on 6/8/2015.
 */
public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 2000;

    public static float RED = 0.8f;
    public static float BLUE = 1f;
    public static float GREEN = 0.8f;

    private boolean renderGUIs = true;

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;
    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();

    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRenderer terrainRenderer;

    private GUIShader guiShader = new GUIShader();
    private GUIRenderer guiRenderer;

    private TextShader textShader = new TextShader();
    private TextRenderer textRenderer;

    private SkyBoxRenderer skyBoxRenderer;

    public MasterRenderer(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();


        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        guiRenderer = new GUIRenderer(guiShader);
        textRenderer = new TextRenderer(textShader, Loader.loadTexture("sampleTexture/textSprites/Text_berlin", "png"));
        skyBoxRenderer = new SkyBoxRenderer(projectionMatrix);
    }

    public void prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
    }

    /**
     * Process entities once in one render loop.
     * @param entity
     */
    public void processEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null){
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    /**
     * Process entities once in one render loop.
     * @param variety_entities
     */
    public void processEntity(List<Entity> variety_entities){
        for (Entity entity : variety_entities){
            TexturedModel entityModel = entity.getModel();  //creating new List according to Texture uniqueness
            List<Entity> batch = entities.get(entityModel);
            if (batch != null){
                batch.add(entity);
            } else {
                List<Entity> newBatch = new ArrayList<Entity>();
                newBatch.add(entity);
                entities.put(entityModel, newBatch);
            }
        }
    }

    /**
     * 1. The List of terrains have been moved to Game.terrains <br>
     * 2. This method will be used to check which terrain to render according to the player positionX, <br>
     * 3. Also this method need not to be checked every frame. Maybe after every (playerMovementDistance) > widthOfTerrain/2 or something like that <br>
     */
    public void processTerrain() {}

    public void processGUIs(){}

    //TODO this render method needs to be free of light and camera method

    /**
     * <b>WARNING</b>!!After this method call clearVolatileObjects() else the volatile arrays(entities) will keep building up and cause a crash.
     * @param lights
     * @param camera
     * @param clipPlane
     */
    public void render(List<Light> lights, Camera camera, Vector4f clipPlane){
        prepare();

        skyBoxRenderer.render(camera, RED, GREEN, BLUE);

        //Terrain
        terrainShader.start();
        terrainShader.loadLight(lights);
        terrainShader.loadViewMatrix(camera);
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadClipPlane(clipPlane);
        terrainRenderer.render(Game.terrains);  //TODO there should be a separate class where all these statics for stuffs like terrains are kept
        terrainShader.stop();

        //Entity
        shader.start();
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadClipPlane(clipPlane);
        entityRenderer.render(entities);
        shader.stop();

        //Guis
        if (renderGUIs) {
            guiShader.start();
            guiRenderer.render(Game.guis);
            guiShader.stop();
        }

        textShader.start();
        textRenderer.render(Game.texts);
        textShader.stop();


    }

    private void createProjectionMatrix(){//Camera Projection Matrix
        float aspectRatio       = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale           = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f)))) * aspectRatio;
        float x_scale           = y_scale / aspectRatio;
        float frustrum_length   = FAR_PLANE - NEAR_PLANE;

        //This matrix is in the net
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = - ((FAR_PLANE + NEAR_PLANE) / frustrum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = - ((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
        projectionMatrix.m33 = 0;
    }

    /**
     * <b>WARNING</b> This method need to be called after render() method after all the
     */
    public void clearVolatileObjects(){
        entities.clear();
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
        guiShader.cleanUp();
        textShader.cleanUp();
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public void setRenderGUIs(boolean renderGUI){
        renderGUIs = renderGUI;
    }

}
