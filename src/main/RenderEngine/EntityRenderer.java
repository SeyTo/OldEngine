package main.RenderEngine;

import main.Entities.Entity;
import main.Models.RawModel;
import main.Models.TexturedModel;
import main.Shaders.StaticShader;
import main.Textures.ModelTexture;
import main.Tools.Maths;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

/**
 * Created by RaJU on 5/27/2015.
 */
public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix){
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);//this projection matrix can be loaded at "masterrenderer" but highly doubt it will be needed later
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }



    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);  //vertices array
        GL20.glEnableVertexAttribArray(1);  //textures array
        GL20.glEnableVertexAttribArray(2);  //normals array

        {//Load Texture and shine
            ModelTexture texture = model.getTexture();
            shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        }
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void unbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

}


