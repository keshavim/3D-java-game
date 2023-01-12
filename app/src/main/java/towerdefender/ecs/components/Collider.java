package towerdefender.ecs.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.gfx.Renderer;

public class Collider extends Component{

    private List<GameObject> collidableObjects;
    public boolean collision = false;
    
    public Collider(){
        collidableObjects = new ArrayList<>();
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        collision = checkCollision();
        if(collision){
            Renderer.setBg(1, 0, 0, 1);
        }
        if(! collision){
            Renderer.setBg(0, 0, 0, 1);
            
        }
    }
    public boolean checkCollision(){
        boolean collision = false;
        for (GameObject other : collidableObjects) {
            if(other == this.gameObject) continue;
            Vector3f op = other.getComponent(Transform.class).getPosition();
            Vector3f tp = this.gameObject.getComponent(Transform.class).getPosition();
            
            if((tp.y <= op.y && tp.y >= op.y - 1) && 
            (tp.x <= op.x + 1 && tp.x >= op.x) && 
            (tp.z <= op.z + 1 && tp.z >= op.z))
            {
                collision = true;
            }
            if((tp.y-1 <= op.y && tp.y-1 >= op.y - 1) && 
            (tp.x +1 <= op.x + 1 && tp.x + 1 >= op.x) && 
            (tp.z +1 <= op.z + 1 && tp.z+1 >= op.z))
            {
                collision = true;
            }

        }
        return collision;
    }
    public List<GameObject> getCollidableObjects() {
        return collidableObjects;
    }

    public void setCollidableObjects(List<GameObject> collidableObjects) {
        this.collidableObjects = collidableObjects;
    }
    public void addToCollidableObjects(GameObject other){
        collidableObjects.add(other);
    }

}
