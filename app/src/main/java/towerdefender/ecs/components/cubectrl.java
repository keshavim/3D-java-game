package towerdefender.ecs.components;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.engine.Input;
import static org.lwjgl.glfw.GLFW.*;

public class cubectrl extends Component {

    private Transform transform;
    private RigidBody rBody;
    private Collider collider;
    private final float speed = 4;

    @Override
    public void start() {
        // TODO Auto-generated method stub
        transform = gameObject.getComponent(Transform.class);
        rBody = gameObject.getComponent(RigidBody.class);
        collider = gameObject.getComponent(Collider.class);
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        updateMovement(dt);

        
    }

    private void updateMovement(float dt) {

        Vector3f offset = new Vector3f();
        if (Input.keyPressed(GLFW_KEY_LEFT)) {
            offset.x = speed * dt;
            transform.move(offset.negate());
        }
        if (Input.keyPressed(GLFW_KEY_RIGHT)) {
            offset.x = speed * dt;
            transform.move(offset);
        }
        if (Input.keyPressed(GLFW_KEY_DOWN)) {
            offset.z = speed * dt;
            transform.move(offset);
        }
        if (Input.keyPressed(GLFW_KEY_UP)) {
            offset.z = speed * dt;
            transform.move(offset.negate());
        }

        if (Input.keyPressed(GLFW_KEY_RIGHT_CONTROL)) {
            offset.y = speed;
            transform.move(0, speed * dt, 0);
        }

        if (Input.keyPressed(GLFW_KEY_RIGHT_SHIFT)) {
            offset.y = speed;
            transform.move(0, -speed * dt, 0);
        }
    

    }

}
