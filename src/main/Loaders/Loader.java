package main.Loaders;


import de.matthiasmann.twl.utils.PNGDecoder;
import main.Models.RawModel;
import main.Textures.TextureData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rj on 5/27/2015.
 */
public class Loader {

  //Basic VAO and VBO loading
    //I think all the methods should be static

    private static List<Integer> vaos = new ArrayList<Integer>();
    private static List<Integer> vbos = new ArrayList<Integer>();  //think this should be static
    private static List<Integer> texture = new ArrayList<Integer>();

    public static RawModel loadToVAO(int[] indices, float[] vertices, float[] textureCoord, float[] normals){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3, vertices);      //Store all the vertices in the first row ie 0
        storeDataInAttributeList(1,2, textureCoord);
        storeDataInAttributeList(2,3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length );
    }

    /**
     * Loader to VAO for 2D vertices
     * @param vertices
     * @return
     */
    public static RawModel loadToVAO(float[] vertices, int dimensions){
        int vaoID = createVAO();
        storeDataInAttributeList(0,dimensions, vertices);
        unbindVAO();
        return new RawModel(vaoID, vertices.length/dimensions);  //??
    }

    private static void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private static int createVAO(){
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private static void storeDataInAttributeList(int attributeNumber,int coordinateSize, float[] vertices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(vertices);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);

        /*unbindVAO*/GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

        private static FloatBuffer storeDataInFloatBuffer(float[] vertices){
            FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
            buffer.put(vertices);
            buffer.flip();
            return buffer;
        }

        private static IntBuffer storeDataInIntBuffer(int[] indices){
            IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
            buffer.put(indices);
            buffer.flip();
            return buffer;
        }

 //Texture Loading and Data

    public static int loadTexture(String fileName, String format){
        int textureID = 0;
        try {
            Texture texture = TextureLoader.getTexture(format, new FileInputStream("res/" + fileName + "." + format));
            textureID = texture.getTextureID();
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
        } catch (IOException e) {
            e.printStackTrace();
        }
            if (textureID == 0) System.out.println("The Texture maybe the false one.");
        texture.add(textureID);
        return textureID;
    }

    public static int loadCubeMap(String[] textureFiles){
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

        for (int i = 0; i < textureFiles.length; i++) {
            TextureData textureData = decodeTextureFile(textureFiles[i]);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        texture.add(texID);
        return texID;
    }

    private static TextureData decodeTextureFile(String fileName){
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;

        try {
            FileInputStream stream = new FileInputStream("res/sampleTexture/" + fileName + ".png");
            PNGDecoder decoder = new PNGDecoder(stream);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File : res/" + fileName + ", could not be read");
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return new TextureData(width, height, buffer);
    }

 //Cleanup

    private static void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    public static void cleanUp(){

        System.out.println("Deleting " + vaos.size() + "VAOs ");
        for (int vao : vaos){
            GL30.glDeleteVertexArrays(vao);

        }
        System.out.println("Deleting " + vbos.size() + "VBOs ");
        for (int vbo : vbos){
            GL15.glDeleteBuffers(vbo);
        }
        System.out.println("Deleting " + texture.size() + "Textures ");
        for (int textureId : texture){
            GL11.glDeleteTextures(textureId);
        }

    }

}
