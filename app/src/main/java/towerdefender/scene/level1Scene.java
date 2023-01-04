package towerdefender.scene;

import towerdefender.engine.Input;
import towerdefender.gfx.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public class level1Scene extends Scene{

    @Override
    public void init() {
        // TODO Auto-generated method stub
        System.out.println("in level 1");
        
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        if(Input.keyPressed(GLFW_KEY_LEFT) && !changeScene){
            changeScene = true;
        }
        if(changeScene && changeTime > 0){
            changeTime -= dt;
        } else if(changeScene){
            Scene.changeScene(0);
            changeTime = 1f;
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub
        
    }
    
}
