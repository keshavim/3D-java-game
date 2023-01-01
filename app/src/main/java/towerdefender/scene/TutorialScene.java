package towerdefender.scene;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Transform;
import towerdefender.engine.Input;
import towerdefender.gfx.Renderer;

import static org.lwjgl.glfw.GLFW.*;



public class TutorialScene extends Scene{

    GameObject test;
    @Override
    public void init() {
        // TODO Auto-generated method stub
        System.out.println("in tutorial");
        test = new GameObject("Test-Object");
        test.addComponent(new Transform());
        addGameObjectToScene(test);
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
        for(GameObject object : gameObjects){
            object.update();
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        test.cleanup();
    }
    
}
