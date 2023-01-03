package towerdefender.scene;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Mesh;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.gfx.Shader;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4f;



public class TutorialScene extends Scene{


    float[] vertexArray = new float[]{
        0f,0f,0f,
        0f,1f,0f,
        1f,1f,0f,
        1f,0f,0f
    };
    int[] indices = new int[]{
        0, 1, 2,
        2, 3, 0
    };

    GameObject cube;
    Shader cubeShader;
    @Override
    public void init() {
        // TODO Auto-generated method stub
        System.out.println("in tutorial");
        
        cubeShader = new Shader("Default.glsl");

        cube = new GameObject("cube");
        cube.addComponent(new Mesh(vertexArray, indices));
        cube.addComponent(new ModelRenderer());
        addGameObjectToScene(cube);
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
        cubeShader.bind();

        cubeShader.setUniform("uColor", new Vector4f(1,1,1,1));
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
