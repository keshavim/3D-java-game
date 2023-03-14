package towerdefender.ecs.components;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.engine.ImGuiLayer;
import towerdefender.scene.Scene;


public class EnemyController extends Component{

    Collider collider = null;
    GameObject target;
    Transform transform;
    RigidBody rBody;
    float speed;
    float damage;


    @Override
    public void start() {
        // TODO Auto-generated method stub
        collider = gameObject.getComponent(Collider.class);
        target = gameObject.getGameObject("tower");
        transform = gameObject.getComponent(Transform.class);
        rBody = gameObject.getComponent(RigidBody.class);
        speed = 0.002f;
        damage = 0.5f;
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
        for (GameObject other : collider.otherList) {
            if(other.getName() == "tower"){
                ImGuiLayer.towerHealth -= damage;
                System.out.println("hit");
                Scene.getCurrentScene().removeGameObjectFromScene(gameObject);
            }
            
        }


        Vector3f position = target.getComponent(Transform.class).getPosition();
        if(transform.getPosition().distance(position) > 1.3f)
            transform.getPosition().lerp(position, speed);
        
    }
    
}
