package towerdefender.scene;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.Model;
import towerdefender.ecs.components.Collider;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.ecs.components.PlayerController;
import towerdefender.ecs.components.RigidBody;
import towerdefender.ecs.components.Transform;
import towerdefender.gfx.Renderer;
import towerdefender.gfx.Shader;
import towerdefender.gfx.Texture;
import towerdefender.engine.Camera;
import org.joml.Vector3f;


import static org.lwjgl.opengl.GL33.glBindVertexArray;


public class TutorialScene extends Scene{
    
    @Override
    public void init() {
        // temp objects
        System.out.println("in tutorial");
        textureCache = new Texture.Cache();
        camera = new Camera( new Vector3f(0,2, 12f));
        Model cubeModel = new Model("Magnet-Model","app/src/main/resources/models/cube/cube.obj");

        GameObject player = new GameObject("player");
        player.addComponent(new PlayerController(camera));
        player.addComponent(new Collider());
        addGameObjectToScene(player);

        //for(int i = 0; i < 10; i++){
        GameObject cube = new GameObject("cube");
        cube.addComponent(new ModelRenderer(cubeModel));
        cube.addComponent(new Collider());
        addGameObjectToScene(cube);
        
        // cube = new GameObject("cube", new Vector3f(0,5,0));
        // cube.addComponent(new Collider());
        // cube.addComponent(new RigidBody());
        // cube.addComponent(new ModelRenderer(cubeModel));
        // addGameObjectToScene(cube);


       // }
    }

    @Override
    public void update(float dt) {
        
        
        Shader s = Renderer.getCurrentShader();
        s.uploadUniform("uView", camera.getViewMatrix());
        s.uploadUniform("uProj", camera.getProjectionMatrix());

        s.uploadUniform("uTexture", 0);

        gameObjects.forEach(o -> o.update(dt));

        glBindVertexArray(0);
        s.unBind();
    }

    @Override
    public void cleanup() {
        gameObjects.forEach(o -> o.cleanup());
    }
    
}
