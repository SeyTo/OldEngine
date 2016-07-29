package main.RenderEngine;

import main.Models.RawModel;
import main.Models.TexturedModel;
import main.Shaders.TerrainShader;
import main.Terrains.Terrain;
import main.Textures.ModelTexture;
import main.Textures.TerrainTexturePack;
import main.Tools.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;
import java.util.Map;

/**
 * Created by rj on 6/9/2015.
 */
public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix){
        this.shader = shader;

        shader.start();

        shader.loadMultiTextureSamplers();
        shader.loadProjectionMatrix(projectionMatrix);//this projection matrix can be loaded at "masterrenderer" but highly doubt it will be needed later
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains){
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);  //vertices array
        GL20.glEnableVertexAttribArray(1);  //textures array
        GL20.glEnableVertexAttribArray(2);  //normals array

        {//Load Texture and shine
               //TODO get shine and reflectivity according to multitexture
            shader.loadShineVariables(1, 0);
            bindTexturePack(terrain);
        }
    }

        private void bindTexturePack(Terrain terrain){
            TerrainTexturePack texturePack = terrain.getTexturePack();

            GL13.glActiveTexture(GL13.GL_TEXTURE0); //background texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());

            GL13.glActiveTexture(GL13.GL_TEXTURE1); //red texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());

            GL13.glActiveTexture(GL13.GL_TEXTURE2); //green texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());

            GL13.glActiveTexture(GL13.GL_TEXTURE3); //blue texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());

            GL13.glActiveTexture(GL13.GL_TEXTURE4); //blendMap
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());

        }

    private void loadModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
