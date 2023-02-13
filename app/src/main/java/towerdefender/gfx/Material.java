package towerdefender.gfx;

import java.io.File;
import java.nio.IntBuffer;

import org.joml.Vector4f;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;

import towerdefender.scene.Scene;

import static org.lwjgl.assimp.Assimp.*;

//contains the texture path and color information about the texture
public class Material {
    public static final Vector4f DEFAULT_DIFFUSE = new Vector4f(0, 0, 0, 1);

    String texturePath;

    private Vector4f diffuseColor, ambientColor, specularColor;

    public Material(Vector4f color) {
        diffuseColor = color;
        ambientColor = null;
        specularColor = null;
        texturePath = null;
    }

    public Material(AIMaterial material, String modelDir) {
        diffuseColor = new Vector4f();
        ambientColor = new Vector4f();
        specularColor = new Vector4f();
        // gets the colors
        AIColor4D color = AIColor4D.create();
        int result = aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
        if (result == aiReturn_SUCCESS) {
            setDiffuseColor(color);
        }
        result = aiGetMaterialColor(material, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
        if (result == aiReturn_SUCCESS) {
            setAmbientColor(color);
        }
        result = aiGetMaterialColor(material, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
        if (result == aiReturn_SUCCESS) {
            setSpecularColor(color);
        }

        // creates the texture
        try (MemoryStack stack = MemoryStack.stackPush()) {
            AIString aiTexturePath = AIString.calloc(stack);
            aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, aiTexturePath, (IntBuffer) null,
                    null, null, null, null, null);

            String texturePath = aiTexturePath.dataString();
            if (texturePath != null && texturePath.length() > 0) {
                this.texturePath = (modelDir + File.separator + aiTexturePath.dataString());
                Scene.getCurrentCache().createTexture(this.texturePath);
                diffuseColor.set(Material.DEFAULT_DIFFUSE);
            }
        }
    }

    public void setDiffuseColor(AIColor4D color) {
        if (color != null)
            diffuseColor.set(color.r(), color.g(), color.b(), color.a());
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setAmbientColor(AIColor4D color) {
        if (color != null)
            ambientColor.set(color.r(), color.g(), color.b(), color.a());
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setSpecularColor(AIColor4D color) {
        if (color != null)
            specularColor.set(color.r(), color.g(), color.b(), color.a());
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public Texture getTexture() {
        Texture.Cache cache = Scene.getCurrentCache();
        return cache.getTexture(texturePath);
    }
}
