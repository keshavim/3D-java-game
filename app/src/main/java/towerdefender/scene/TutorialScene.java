package towerdefender.scene;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Material;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.gfx.Mesh;
import towerdefender.gfx.Shader;
import towerdefender.gfx.Texture;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4f;



public class TutorialScene extends Scene{


    float[] vertexArray = new float[]{
        0f,0f,0f,
        0f,1f,0f,
        1f,1f,0f,
        1f,0f,0f
    };
    float[] textureCoord = new float[]{
        0f, 0f,
        0f, 1f,
        1f, 1f,
        1f, 0f

    };
    int[] indices = new int[]{
        0, 1, 2,
        2, 3, 0
    };

    Shader cubeShader;
    @Override
    public void init() {
        // TODO Auto-generated method stub
        System.out.println("in tutorial");
        
        cubeShader = new Shader("Default.glsl");

        GameObject cube = new GameObject("cube");
        cube.addComponent(new Material(vertexArray, textureCoord, indices, "ambitious.jpg"));
        cube.addComponent(new ModelRenderer());
        addGameObjectToScene(cube);
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
        cubeShader.bind();
        cubeShader.uploadUniform("uTexture", 0);

        gameObjects.forEach(o -> o.update(dt));

        cubeShader.unBind();
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        cubeShader.cleanup();
        gameObjects.forEach(o -> o.cleanup());
    }
    
}
