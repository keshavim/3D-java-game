package towerdefender.ecs.components;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.engine.Camera;
import towerdefender.engine.Input;
import towerdefender.engine.Prefabs;
import towerdefender.gfx.Model;
import towerdefender.scene.Scene;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

public class PlayerController extends Component {

    private Camera mainCamera;
    private Transform transform;
    private RigidBody rBody;
    private Collider collider;
    public Model bulletModel;
    private float shootSpeed, shootInterval;

    private final float speed = 4;
    private float jumpForce = 0;

    public PlayerController(Camera mainCamera) {
        this.mainCamera = mainCamera;
        shootSpeed = 1;
        shootInterval = shootSpeed;

    }

    @Override
    public void start() {
        transform = gameObject.getComponent(Transform.class);
        rBody = gameObject.getComponent(RigidBody.class);
        collider = gameObject.getComponent(Collider.class);
        transform.getPosition().add(new Vector3f(0, 1, 0), mainCamera.getPosition());

    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub

        updateMovement(dt);
        shootInterval -= dt;

        if (Input.keyPressed(GLFW_KEY_E) && shootInterval <= 0) {
            Vector3f position = new Vector3f();
            mainCamera.getPosition().add(mainCamera.getFront(), position);
            Prefabs.createBasicBullet(Scene.getCurrentScene(), bulletModel, position,
                    new Vector3f(mainCamera.getFront()));
            shootInterval = shootSpeed;
        }

        if (collider.other != null && collider.other.getName() == "ground") {
            if (rBody != null)
                transform.move(0, -rBody.gravity * dt, 0);
        }
        

    }

    private void updateMovement(float dt) {

        Vector3f offset = new Vector3f();
        if (Input.keyPressed(GLFW_KEY_A)) {
            mainCamera.getRight().mul(speed * dt, offset);
            offset.y = 0;
            transform.move(offset.negate());
        }
        if (Input.keyPressed(GLFW_KEY_D)) {
            mainCamera.getRight().mul(speed * dt, offset);
            offset.y = 0;
            transform.move(offset);
        }
        if (Input.keyPressed(GLFW_KEY_W)) {
            mainCamera.getFront().mul(speed * dt, offset);
            offset.y = 0;
            transform.move(offset);
        }
        if (Input.keyPressed(GLFW_KEY_S)) {
            mainCamera.getFront().mul(speed * dt, offset);
            offset.y = 0;
            transform.move(offset.negate());
        }

        if (Input.keyPressed(GLFW_KEY_SPACE) && collider.getCollision(Collider.BOTTOM)) {
            jumpForce = 1;
        }
        while(jumpForce > 0){
            transform.move(0, jumpForce * dt, 0);
            jumpForce -= 0.01;

        }
        
        // if (transform.getPosition().x <= -7)
        //     transform.getPosition().x += 0.1f;
        // if (transform.getPosition().x >= 7)
        //     transform.getPosition().x -= 0.1f;
        // if (transform.getPosition().z <= -7)
        //     transform.getPosition().z += 0.1f;
        // if (transform.getPosition().z >= 7)
        //     transform.getPosition().z -= 0.1f;

        transform.getPosition().add(new Vector3f(0, 1, 0), mainCamera.getPosition());

    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera(Camera mainCamera) {
        this.mainCamera = mainCamera;
    }
}
