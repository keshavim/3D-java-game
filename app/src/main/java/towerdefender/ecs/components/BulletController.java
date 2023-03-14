package towerdefender.ecs.components;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.engine.ImGuiLayer;
import towerdefender.scene.Scene;

public class BulletController extends Component {

    private float speed;
    private Transform transform;
    private Collider collider;
    private Vector3f front;

    public BulletController(Vector3f front) {
        speed = 5;
        this.front = front;

    }

    @Override
    public void start() {
        transform = gameObject.getComponent(Transform.class);
        collider = gameObject.getComponent(Collider.class);
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        Vector3f movement = front.mul(speed * dt, new Vector3f());
        transform.move(movement);

        for (GameObject other : collider.otherList) {
            if(other.getName() == "enemy"){
                Scene.getCurrentScene().removeGameObjectFromScene(other);
                Scene.getCurrentScene().removeGameObjectFromScene(gameObject);
                ImGuiLayer.enemiesKilled += 1;

            }
        }
    }

}
