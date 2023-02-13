package towerdefender.ecs.components;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.scene.Scene;


public class EnemyController extends Component{

    Collider collider = null;
    GameObject target;
    Transform transform;
    RigidBody rBody;


    @Override
    public void start() {
        // TODO Auto-generated method stub
        collider = gameObject.getComponent(Collider.class);
        target = gameObject.getGameObject("tower");
        transform = gameObject.getComponent(Transform.class);
        rBody = gameObject.getComponent(RigidBody.class);
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        if(collider.other != null && collider.other.getName() == "bullet"){
            Scene.getCurrentScene().removeGameObjectFromScene(gameObject);
        }
        Vector3f position = target.getComponent(Transform.class).getPosition();
        if(transform.getPosition().distance(position) > 1.3f)
            transform.getPosition().lerp(position, 0.001f);

        if(collider.other != null && collider.other.getName() == "ground"){
            if(rBody != null)
            transform.move(0, -rBody.gravity * dt, 0);
        }
    }
    
}
