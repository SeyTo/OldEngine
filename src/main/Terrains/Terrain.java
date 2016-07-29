package main.Terrains;

import main.Models.RawModel;
import main.Loaders.Loader;
import main.Textures.TerrainTexture;
import main.Textures.TerrainTexturePack;
import main.Tools.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by rj on 6/9/2015.
 */
public class Terrain {

    private static final int SIZE = 800;    //size of each cell(distance between 2 vertices) NOT a grid
    private static final int MAX_HEIGHT = 50;
    private static final int MAX_PIXEL_COLOR = 256 * 256 * 256;

    private int x;
    private int z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;

    private float[][] heights;

    public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, TerrainTexture blendMap){
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateTerrain("testHeightMap");

    }

    private RawModel generateTerrain(String heightMapName){

        BufferedImage heightMap = null;

        try {
            heightMap = ImageIO.read(new File("res/random/" + heightMapName + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int VERTEX_COUNT = heightMap.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float height = getHeightsFromHeightMap(j, i, heightMap);
                heights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;

                Vector3f newNormalized = calculateNormals(j,i,heightMap);
                normals[vertexPointer*3] = newNormalized.x;
                normals[vertexPointer*3+1] = newNormalized.y;
                normals[vertexPointer*3+2] = newNormalized.z;

                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return Loader.loadToVAO(indices, vertices, textureCoords, normals);
    }

        private float getHeightsFromHeightMap(int x, int z, BufferedImage heightMap){
            if (x < 0 || x >= heightMap.getWidth() || z < 0 || z >= heightMap.getHeight()){
                return 0;
            }
            float height = heightMap.getRGB(x,z);
            height += MAX_PIXEL_COLOR/2f;
            height /= MAX_PIXEL_COLOR/2f;
            height *= MAX_HEIGHT;
            return height;
        }

        public float getHeightOfTerrain(float worldX, float worldZ){
            float terrainX = worldX - (float)this.x;   //location of someone in a particular terra
            float terrainZ = worldZ - (float)this.z;
            float gridSquareSize = SIZE / (float)(heights.length - 1);  //size of each SG
            int gridX = (int)Math.floor(terrainX / gridSquareSize);     //location in a particular grid normalizing between 0 and size
            int gridZ = (int)Math.floor(terrainZ / gridSquareSize);
            if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0 ){
                return 0;
            }
            float xCoord = (terrainX % gridSquareSize) / gridSquareSize ;
            float zCoord = (terrainZ % gridSquareSize) / gridSquareSize ; // (x distance within a SG)/SG .. and get it between 0 and 1
            float ans;

            if (xCoord <= (1-zCoord)){
                ans = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ],0),
                                        new Vector3f(1, heights[gridX + 1][gridZ], 0),
                                        new Vector3f(0, heights[gridX][gridZ + 1],1),
                                        new Vector2f(xCoord, zCoord));
            } else {
                ans = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ],0),
                                        new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                                        new Vector3f(0, heights[gridX][gridZ + 1],1),
                                        new Vector2f(xCoord, zCoord));
            }

            return ans;

        }

    private Vector3f calculateNormals(int x,int z,  BufferedImage image){
        float heightL = getHeightsFromHeightMap(x - 1, z, image);
        float heightR = getHeightsFromHeightMap(x + 1, z, image);
        float heightD = getHeightsFromHeightMap(x, z - 1, image);
        float heightU = getHeightsFromHeightMap(x, z + 1, image);
        Vector3f normal = new Vector3f(heightL-heightR, 2f/*??notaccurate*/, heightD-heightU);
        normal.normalise();
        return normal;
    }

    //BEANS

    public int getSize(){
        return SIZE;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getGridX() { return x/SIZE;}

    public int getGridZ() { return z/SIZE;}

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
}
