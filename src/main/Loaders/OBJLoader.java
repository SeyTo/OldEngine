package main.Loaders;

import main.Models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaJU on 6/2/2015.
 */
public class OBJLoader {

    public static RawModel load(String fileName){

        FileReader fReader = null;

        try {
            fReader = new FileReader("res/raw/" + fileName + ".obj");
            System.out.println("Receiving object " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fReader);

        List<Integer> indices = new ArrayList<Integer>();
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textureCoord = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<String> faces = new ArrayList<String>();
        int[] indicesArray;
        float[] verticesArray;
        float[] texturesArray = null;
        float[] normalsArray = null;

        String line;
        try {
            line = reader.readLine();
            while (line != null) {

                String[] split = line.split(" ");
                if (line.startsWith("v ") || line.startsWith("v  ")) {
                    Vector3f vector = new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
                    vertices.add(vector);
                } else if (line.startsWith("vt ") || line.startsWith("vt  ")) {
                    Vector2f vector = new Vector2f(Float.parseFloat(split[1]), Float.parseFloat(split[2]));
                    textureCoord.add(vector);
                } else if (line.startsWith("vn ") || line.startsWith("vn  ")) {
                    Vector3f vector = new Vector3f(Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
                    normals.add(vector);
                } else if (line.startsWith("f ") || line.startsWith("f  ")){
                    String face = split[1] + " " + split[2] + " " + split[3];
                    faces.add(face);
                }
                line = reader.readLine();
            }

            reader.close();

            texturesArray = new float[vertices.size() * 2];
            //System.out.println("Texture Array Size : " + texturesArray.length);
            //System.out.println("No of Texture coords ; " + textureCoord.size());
            normalsArray = new float[vertices.size() * 3];
            //System.out.println("Normals Array Size : " + normalsArray.length);
            //System.out.println("No of normals coords ; " + normals.size());

            int facesIndex = 0;
            while (true) {
                String facesLine = faces.get(facesIndex);   ++facesIndex;

                String[] currentLine = facesLine.split(" ");

                    String[] vertexData1 = currentLine[0].split("/");
                    String[] vertexData2 = currentLine[1].split("/");
                    String[] vertexData3 = currentLine[2].split("/");

                    processVertex(vertexData1, indices, textureCoord, normals, texturesArray, normalsArray);
                    processVertex(vertexData2, indices, textureCoord, normals, texturesArray, normalsArray);
                    processVertex(vertexData3, indices, textureCoord, normals, texturesArray, normalsArray);

                if (facesIndex > faces.size() - 1) break;
            }

        } catch (IOException iox){
            iox.printStackTrace();
        }


        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;

        for (Vector3f vertex : vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0 ; i < indices.size() ; i++){
            indicesArray[i] = indices.get(i);
        }

            return Loader.loadToVAO(indicesArray, verticesArray, texturesArray, normalsArray);

    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalArray ) {

        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;

        indices.add(currentVertexPointer);

        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
            textureArray[currentVertexPointer * 2] = currentTexture.x;
            textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
            normalArray[currentVertexPointer * 3] = currentNormal.x;
            normalArray[currentVertexPointer * 3 + 1] = currentNormal.y;
            normalArray[currentVertexPointer * 3 + 2] = currentNormal.z;

    }
}
