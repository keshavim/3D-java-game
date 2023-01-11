package towerdefender.scene;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.Model;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.ecs.components.Transform;
import towerdefender.gfx.Renderer;
import towerdefender.gfx.Shader;
import towerdefender.gfx.Texture;
import towerdefender.engine.Camera;
import org.joml.Vector3f;


import static org.lwjgl.opengl.GL33.glBindVertexArray;


public class TutorialScene extends Scene{

    Shader deflaultShader;
    
    @Override
    public void init() {
        // temp objects
        System.out.println("in tutorial");
        
        deflaultShader = new Shader("Default.glsl");
        textureCache = new Texture.Cache();
        camera = new Camera(new Vector3f(0,2,12f));
        Model cubeModel = new Model("Magnet-Model","app/src/main/resources/models/cube/cube.obj");

        //for(int i = 0; i < 10; i++){
        GameObject cube = new GameObject("cube", new Transform());
        cube.addComponent(new ModelRenderer(cubeModel));
        addGameObjectToScene(cube);

        cube = new GameObject("cube", new Vector3f(0,5,0));
        cube.addComponent(new ModelRenderer(cubeModel));
        addGameObjectToScene(cube);

        cube = new GameObject("cube", new Vector3f(0,7,0));
        cube.addComponent(new ModelRenderer(cubeModel));
        addGameObjectToScene(cube);

       // }
    }

    @Override
    public void update(float dt) {
        
        camera.update(dt);
        
        //temp drawing
        Renderer.bindShader(deflaultShader);
        
        deflaultShader.uploadUniform("uView", camera.getViewMatrix());
        deflaultShader.uploadUniform("uProj", camera.getProjectionMatrix());

        deflaultShader.uploadUniform("uTexture", 0);

        gameObjects.forEach(o -> o.update(dt));

        glBindVertexArray(0);
        deflaultShader.unBind();
    }

    @Override
    public void cleanup() {
        
        deflaultShader.cleanup();
        gameObjects.forEach(o -> o.cleanup());
    }
    
}
