package main.Models;

import main.Textures.ModelTexture;

/**
 * Created by RaJU on 5/31/2015.
 */
public class TexturedModel {

    private RawModel model;
    private ModelTexture texture;

    public TexturedModel(RawModel model, ModelTexture texture){
        this.model = model;
        this.texture = texture;
    }

    public RawModel getModel() {
        return model;
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
