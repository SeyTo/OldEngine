package main.RenderEngine;

import main.Loaders.Loader;
import main.Models.RawModel;
import main.Shaders.GUIShader;
import main.Textures.GUITexture;
import main.Tools.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;

/**
 * Created by rj on 6/12/2015.
 */
public class GUIRenderer {


    private final RawModel quad;
    private GUIShader shader;

    public GUIRenderer(GUIShader shader){
        float[] vertices = {-1,1, -1,-1, 1,1, 1,-1};
        quad = Loader.loadToVAO(vertices,2);
        this.shader = shader;
    }

    public void render(List<GUITexture> guis) {
        shader.start();
            prepareTexturedGUI();
            for (GUITexture gui : guis) {
                prepareInstance(gui);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
            }

            unbindTexturedGUI();
        shader.stop();

    }

    /**
     * This method seems wierd because the GUI is rendered in the entire screen. The GUI is actually only transformed later on. Thus,
     * there is only one(same) VAO and one(same) VBO for vertices for all GUIs.
     */
    private void prepareTexturedGUI() {
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);  //vertices array

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private void prepareInstance(GUITexture gui){
        {//Load Texture
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTextureID());
        }
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getRotZ(), gui.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void unbindTexturedGUI(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void cleanUp(){
        shader.cleanUp();
    }
}
