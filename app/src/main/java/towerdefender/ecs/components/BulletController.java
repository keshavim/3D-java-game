package towerdefender.ecs.components;

import org.joml.Vector3f;

import towerdefender.ecs.Component;

public class BulletController extends Component {

    private float speed;
    private Transform transform;
    private Vector3f front;

    public BulletController(Vector3f front) {
        speed = 1;
        this.front = front;

    }

    @Override
    public void start() {
        transform = gameObject.getComponent(Transform.class);
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        transform.move(front.mul(speed * dt, new Vector3f()));
    }

}
