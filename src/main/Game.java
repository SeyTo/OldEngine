package main;

import main.Controls.BasicStaticControls;
import main.Entities.*;
import main.Entities.Helpers.*;
import main.Loaders.Loader;
import main.Loaders.OBJLoader;
import main.Models.RawModel;
import main.Models.TexturedModel;
import main.RenderEngine.DisplayManager;
import main.RenderEngine.MasterRenderer;
import main.Terrains.Terrain;
import main.Textures.GUITexture;
import main.Textures.ModelTexture;
import main.Textures.TerrainTexture;
import main.Textures.TerrainTexturePack;
import main.Tools.MousePicker;
import main.Water.WaterFrameBuffers;
import main.Water.WaterRenderer;
import main.Water.WaterShader;
import main.Water.WaterTile;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import main.Entities.Helpers.Camera;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO need to create a separate entity like class for the environmental entities like trees and grass
//TODO create grid image just for testing
//TODO create in game console(PainInTheAss)
//TODO terrains needs to render more than just 4 textures (use uniform arrays)
//TODO terrains are passed where the player goes.
//TODO use the standard camera controls
//TODO test first person camera
//TODO shadows and shadow mapping
//TODO also create a tree, grass and other environmental props map
//TODO recreate keys as keypressed or keyhold or keyrelease
public class Game {

    public static boolean exit = false;

    public static List<Terrain> terrains;
    private static List<Entity> trees;
    public static List<Player> players;
    private static List<Entity> modeledHelpers;
    private static List<Helper> nonModeledHelpers;
    private static List<Light> lights;
    public static List<GUITexture> guis;
    public static List<Text> texts;
    public static List<WaterTile> waters;
    private static Camera main_Camera;  //Considering only 1 cam for now

    private int NO_OF_TREES = 20;



    public Game(){


        constructImmersion();
        constructTerrains();
        constructTrees();   //dependent on constructTerrains
        constructPlayers();
        //constructNPC();
        constructGUIs();
        constructHelpers(); //dependent on constructImmersion and constructPlayers

    }

    private static void Modes(){
        DisplayManager.fullScreenMode(false);
    }

    private static void mainLoop(){
        MasterRenderer renderer = new MasterRenderer();

        /*MousePicker picker = new MousePicker(main_Camera, renderer.getProjectionMatrix());*/

        WaterFrameBuffers wfbo = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(waterShader, renderer.getProjectionMatrix(), wfbo);


        /*GUITexture wReflect = new GUITexture(wbos.getReflectionTexture(), new Vector2f(-0.5f,0.5f), new Vector2f(0.25f,0.25f), 0);
        GUITexture wRefract = new GUITexture(wbos.getRefractionTexture(), new Vector2f(0.5f,0.5f), new Vector2f(0.25f,0.25f), 0);*/



        //main_Camera.setMaster(players.get(0));
        main_Camera.setMaster(null);
        while(!Display.isCloseRequested()){
            //Process
            {
                GL11.glEnable(GL11.GL_CLIP_PLANE0);

                renderer.processEntity(trees);
                renderer.processEntity(modeledHelpers);
                renderer.processEntity(players.get(0));    //TODO there should be a separate class where all these statics for stuffs like terrains are kept

                renderer.setRenderGUIs(false);

                wfbo.bindReflectionFrameBuffer();
                renderer.render(lights, main_Camera, new Vector4f(0,1f,0,-waters.get(0).getHeight()));
                wfbo.unbindCurrentFrameBuffer();

                wfbo.bindRefractionFrameBuffer();
                renderer.render(lights, main_Camera, new Vector4f(0,-1f,0,waters.get(0).getHeight()));
                wfbo.unbindCurrentFrameBuffer();
                //renderer.processTerrain();    //moving all the hard pieces to static and directly render at masterRenderer



            }

            //Ticks
            {
                if (main_Camera.getMaster() != null)
                    players.get(0).move(terrains);
                /*if (Mouse.isButtonDown(0)){
                    picker.update();
                    players.get(0).setPosition(picker.getCurrentTerrainPoint());
                }*/
                //basic controls
                BasicStaticControls.controls();
                //camera controls
                main_Camera.processKey();
            }

            {
                GL11.glDisable(GL11.GL_CLIP_PLANE0);
                renderer.setRenderGUIs(false);
                renderer.render(lights, main_Camera, new Vector4f(0,1f,0,waters.get(0).getHeight()));

                waterShader.start();
                waterRenderer.render(waters,main_Camera);
                waterShader.stop();

                renderer.clearVolatileObjects();

                DisplayManager.updateDisplay();
                if (exit) break;

            }
        }

        waterShader.cleanUp();
        wfbo.cleanUp();
        renderer.cleanUp();
        cleanUp();
    }

    public static void main(String[] args){
        Modes();
        DisplayManager.createDisplay();
        new Game();
        mainLoop();

        System.exit(0);
    }

    //CONTRUCTION

    private void constructTerrains(){   //terrains
        System.out.println("1.Creating Terrains");
        TerrainTexture background = new TerrainTexture(Loader.loadTexture("sampleTexture/grassDarkB", "png"));
        TerrainTexture red = new TerrainTexture(Loader.loadTexture("sampleTexture/Grass1","png"));
        TerrainTexture green = new TerrainTexture(Loader.loadTexture("sampleTexture/Sand_texture2","png"));
        TerrainTexture blue = new TerrainTexture(Loader.loadTexture("sampleTexture/tile3","png"));
        TerrainTexture blendMap = new TerrainTexture(Loader.loadTexture("sampleTexture/blendmap","png"));
        TerrainTexturePack texturePack = new TerrainTexturePack(background, red, green, blue);

        terrains = new ArrayList<Terrain>(9);//TODO ASSUMING forever using only 9 terrains then swap in or out

        Terrain terrainMM = new Terrain( 0, 0, texturePack, blendMap);

        terrains.add(terrainMM);

        waters = new ArrayList<WaterTile>(1);
        WaterTile waterTile = new WaterTile(110,380, 25);
        waters.add(waterTile);
    }

    private void constructTrees(){  //Tree01_tex[NO_OF_TREES]
        TexturedModel Tree01_tex = new TexturedModel(OBJLoader.load("Tree2"),
                                                        new ModelTexture(Loader.loadTexture("sampleTexture/grey","png")));
            Tree01_tex.getTexture().setReflectivity(0);
            Tree01_tex.getTexture().setShineDamper(50);

        trees = new ArrayList<Entity>(NO_OF_TREES);

        Random random = new Random();
        for (int i = 0; i < NO_OF_TREES; i++) {
            int x = random.nextInt(800) - 0; //TODO need dynamic variable(1600,800) that changes according to movements
            int z = random.nextInt(800) - 0;
            Entity entity = new Entity(Tree01_tex, new Vector3f(x,0,z), 0,0,0,0.5f );

            trees.add(entity.setPositionY(entity.getCurrentTerrain(Game.terrains).getHeightOfTerrain(entity.getPosition().x, entity.getPosition().z)));
        }
    }

    private void constructPlayers(){    //PL_charizard
        players = new ArrayList<Player>(1); //TODO ASSUMING only one player

        RawModel charizard = OBJLoader.load("Charizard");
        ModelTexture charizardTextures = new ModelTexture(Loader.loadTexture("raw/LizardonDh", "tga"));
            charizardTextures.setReflectivity(0.001f);
            charizardTextures.setShineDamper(0.001f);
        TexturedModel texturedCharizard = new TexturedModel(charizard, charizardTextures);

        Player PL_charizard = new Player(texturedCharizard, new Vector3f(30.0f,50.0f,50.0f),0.0f,0.0f,0.0f,1.0f);

        players.add(PL_charizard);      //index 0 reserved for main player DO NOT CHANGE
    }

    private void constructImmersion(){  //sunMain, sunReflect, sunAmbient
        lights = new ArrayList<Light>(3);        //TODO ASSUMING only 3 lights
        //only 3 main lights reserved ..change at terrain shader and entity shader
        Light sunMain = new Light(new Vector3f(1000,1000,80), new Vector3f(0.8f,1f,0.8f));
        Light sunReflect = new Light(new Vector3f(-80, 90, 80), new Vector3f(1f,0f,0f), new Vector3f(0.35f, 0.0001f, 0.00002f));
        Light sunAmbient = new Light(new Vector3f(-80, 90, -80), new Vector3f(0f,0f,1f), new Vector3f(0.35f, 0.0001f, 0.00002f));
        lights.add(sunMain);    //spot 0 is reserved for sunMain NEVER CHANGE IT
        lights.add(sunReflect); //spot 1 is reserved for sunReflect NEVER CHANGE IT
        lights.add(sunAmbient); //spot 2 is reserved for sunAmbient NEVER CHANGE IT

    }

    private void constructHelpers(){    //PlayerFollower, lightBoxEntity, main_camera
        nonModeledHelpers = new ArrayList<Helper>();
        modeledHelpers = new ArrayList<Entity>();

        //Player follower
        InvisiblePlayerFollower playerFollower = new InvisiblePlayerFollower(players.get(0));
        nonModeledHelpers.add(playerFollower);

        //Light box ent.
        RawModel sunRawModel = lights.get(0).generateLightBox();
        TexturedModel lightSimpleTexturedModel = new TexturedModel(sunRawModel, new ModelTexture(Loader.loadTexture("sampleTexture/tile3","png")));
        Entity lightBoxEntity_sunMain = new Entity(lightSimpleTexturedModel, lights.get(0).getPosition(), 0, 0, 0, 5f);
        Entity lightBoxEntity_sunReflect = new Entity(lightSimpleTexturedModel, lights.get(1).getPosition(),0,0,0, 5f);
        Entity lightBoxEntity_sunAmbient = new Entity(lightSimpleTexturedModel, lights.get(2).getPosition(),0,0,0, 5f);
        modeledHelpers.add(lightBoxEntity_sunMain);
        modeledHelpers.add(lightBoxEntity_sunReflect);
        modeledHelpers.add(lightBoxEntity_sunAmbient);

        //Main Camera
        main_Camera = new Camera();
    }

    private void constructGUIs(){
        guis = new ArrayList<GUITexture>();
        texts = new ArrayList<Text>();
        Text.initiateMapping();


        //GUITexture gui = new GUITexture(Loader.loadTexture("sampleTexture/somelogo","png"), new Vector2f(-0.5f,-0.5f), new Vector2f(0.25f, 0.25f), 0.2f);
        //guis.add(gui);
    }
    //CLEAN UPs

    public static void cleanUp(){
        terrains.clear();
        trees.clear();
        players.clear();
        modeledHelpers.clear();
        nonModeledHelpers.clear();
        lights.clear();
        Text.cleanLettersMap();
        texts.clear();
        waters.clear();

        Loader.cleanUp();
        DisplayManager.destroy();
        System.exit(0);
    }
}
