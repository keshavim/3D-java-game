package towerdefender.ecs.components;

import towerdefender.ecs.Component;
import towerdefender.engine.Camera;
import towerdefender.engine.Input;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerController extends Component{

    private Camera mainCamera;
    private Transform transform;

    private final float speed = 4;
    public PlayerController(Camera mainCamera){
        this.mainCamera = mainCamera;

    }
    @Override
    public void start(){
        transform = gameObject.getComponent(Transform.class);
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
        // test camera movement
        if(Input.keyPressed(GLFW_KEY_A)){
            mainCamera.move(-speed * dt, 0, 0);
        }
        if(Input.keyPressed(GLFW_KEY_D)){
            mainCamera.move(speed * dt, 0, 0);
        }
        if(Input.keyPressed(GLFW_KEY_SPACE))
            mainCamera.move(0, speed * dt, 0);

        if(Input.keyPressed(GLFW_KEY_LEFT_SHIFT))
            mainCamera.move(0, -speed * dt, 0);

        if(Input.keyPressed(GLFW_KEY_W))
            mainCamera.move(0, 0, speed * dt);
        if(Input.keyPressed(GLFW_KEY_S))
            mainCamera.move(0, 0, -speed * dt);
        transform.setPosition(mainCamera.getPosition());

        mainCamera.addRotation(Input.getDeltaPos().y * dt,
                            Input.getDeltaPos().x * dt);
        
    }
    
    public Camera getMainCamera() {
        return mainCamera;
    }
    public void setMainCamera(Camera mainCamera) {
        this.mainCamera = mainCamera;
    }
}
