package towerdefender.scene;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Material;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.engine.Camera;
import towerdefender.engine.Input;
import towerdefender.gfx.Mesh;
import towerdefender.gfx.Shader;
import towerdefender.gfx.Texture;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;
import org.joml.Vector4f;



public class TutorialScene extends Scene{


    float[] vertexArray = new float[]{
        // V0
        -0.5f, 0.5f, 0.5f,
        // V1
        -0.5f, -0.5f, 0.5f,
        // V2
        0.5f, -0.5f, 0.5f,
        // V3
        0.5f, 0.5f, 0.5f,
        // V4
        -0.5f, 0.5f, -0.5f,
        // V5
        0.5f, 0.5f, -0.5f,
        // V6
        -0.5f, -0.5f, -0.5f,
        // V7
        0.5f, -0.5f, -0.5f,

        // For text coords in top face
        // V8: V4 repeated
        -0.5f, 0.5f, -0.5f,
        // V9: V5 repeated
        0.5f, 0.5f, -0.5f,
        // V10: V0 repeated
        -0.5f, 0.5f, 0.5f,
        // V11: V3 repeated
        0.5f, 0.5f, 0.5f,

        // For text coords in right face
        // V12: V3 repeated
        0.5f, 0.5f, 0.5f,
        // V13: V2 repeated
        0.5f, -0.5f, 0.5f,

        // For text coords in left face
        // V14: V0 repeated
        -0.5f, 0.5f, 0.5f,
        // V15: V1 repeated
        -0.5f, -0.5f, 0.5f,

        // For text coords in bottom face
        // V16: V6 repeated
        -0.5f, -0.5f, -0.5f,
        // V17: V7 repeated
        0.5f, -0.5f, -0.5f,
        // V18: V1 repeated
        -0.5f, -0.5f, 0.5f,
        // V19: V2 repeated
        0.5f, -0.5f, 0.5f,
    };
    float[] textureCoord = new float[]{
        0.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 0.0f,
        1.0f, 1.0f,

        0.0f, 1.0f,
        1.0f, 1.0f,
        0.0f, 0.0f,
        1.0f, 0.0f,

        // For text coords in top face
        1.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        0.0f, 0.0f,

        // For text coords in right face
        0.0f, 1.0f,
        0.0f, 0.0f,

        // For text coords in left face
        1.0f, 1.0f,
        1.0f, 0.0f,

        // For text coords in bottom face
        1.0f, 1.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        0.0f, 0.0f
    };
    int[] indices = new int[]{
        // Front face
        0, 1, 3, 3, 1, 2,
        // Top Face
        8, 10, 11, 9, 8, 11,
        // Right face
        12, 13, 7, 5, 12, 7,
        // Left face
        14, 15, 6, 4, 14, 6,
        // Bottom face
        16, 18, 19, 17, 16, 19,
        // Back face
        4, 6, 7, 5, 4, 7,};

    Shader cubeShader;
    @Override
    public void init() {
        // temp objects
        System.out.println("in tutorial");
        
        cubeShader = new Shader("Default.glsl");
        camera = new Camera(new Vector3f(0,0,2f));

        GameObject cube = new GameObject("cube");
        cube.addComponent(new Material(vertexArray, textureCoord, indices, "ambitious.jpg"));
        cube.addComponent(new ModelRenderer());
        addGameObjectToScene(cube);
    }

    @Override
    public void update(float dt) {
        // test camera movement
        if(Input.keyPressed(GLFW_KEY_A)){
            camera.moveLeft(1 * dt);
        }else if(Input.keyPressed(GLFW_KEY_D)){
            camera.moveRight(1 * dt);
        }else if(Input.keyPressed(GLFW_KEY_UP)){
            camera.moveUp(1 * dt);
        }else if(Input.keyPressed(GLFW_KEY_DOWN)){
            camera.moveDown(1 * dt);
        }else if(Input.keyPressed(GLFW_KEY_W)){
            camera.moveForward(1 * dt);
        }else if(Input.keyPressed(GLFW_KEY_S)){
            camera.moveBackward(1 * dt);
        }
        camera.addRotation(Input.getDeltaPos().y * dt,
                            Input.getDeltaPos().x * dt);
        
        //temp drawing
        cubeShader.bind();
        cubeShader.uploadUniform("uProj", camera.getProjectionMatrix());
        cubeShader.uploadUniform("uView", camera.getViewMatrix());

        cubeShader.uploadUniform("uTexture", 0);

        gameObjects.forEach(o -> o.update(dt));

        cubeShader.unBind();
    }

    @Override
    public void cleanup() {
        
        cubeShader.cleanup();
        gameObjects.forEach(o -> o.cleanup());
    }
    
}
