package main.RenderEngine;

import main.Entities.Text;
import main.Loaders.Loader;
import main.Models.RawModel;
import main.Shaders.TextShader;
import main.Tools.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

/**
 * Created by RaJU on 6/13/2015.
 */
public class TextRenderer {

    private final RawModel quad;
    private TextShader shader;
    private int glyphSheetID;

    public TextRenderer(TextShader shader, int glyphSheetID){
        float[] vertices = {-1,1, -1,-1, 1,1, 1,-1};
        quad = Loader.loadToVAO(vertices,2);
        this.shader = shader;
        this.glyphSheetID = glyphSheetID;
    }

    public void render(List<Text> texts) {
        shader.start();
        prepareTexturedGUI();
        positionY = 0;
        for (Text text : texts) {
            positionX = 0;
            for (Vector2f letter : text.getWord()) {
                prepareInstance(text, letter);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
            }
            positionY += text.getScale().getY();
        }

        unbindTexturedGUI();
        shader.stop();
    }

    /**
     * This method seems wierd because the GUI is rendered in the entire screen. The GUI is actually only transformed later on. Thus,
     * there is only one(same) VAO and one(same) VBO for vertices for all GUIs.
     */

    float positionX = 0;
    float positionY = 0;
    private void prepareInstance(Text text, Vector2f letter){

        Vector2f setPosition = new Vector2f(text.getPosition().x + positionX, text.getPosition().y + positionY);//create new vector at new positionX for new letters
        positionX += text.getScale().getX(); //pointing to the next positionX
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(setPosition, 0, text.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadSheetCoordVector(letter);
    }

    private void prepareTexturedGUI() {
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);  //vertices array

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        {//Load Texture
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyphSheetID);
        }
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
