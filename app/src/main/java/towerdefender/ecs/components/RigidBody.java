package towerdefender.ecs.components;

import org.tinylog.Logger;

import towerdefender.ecs.Component;

public class RigidBody extends Component {

    public float gravity = -1.5f;
    public boolean gravityOn, isStatic, isTrigger;

    public RigidBody(boolean isStatic, boolean isTrigger){
        this.isStatic = isStatic;
        this.isTrigger = isTrigger;
    }
    @Override
    public void start() {
        // TODO Auto-generated method stub
        gravityOn = true;
        if(isStatic || isTrigger)
            gravityOn = false;
        
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        
        if (gravityOn) {
            gameObject.getComponent(Transform.class).move(0, gravity * dt, 0);
            // Logger.debug(gravityOn);
        }

    }

    public void setGravityOn(Boolean v){
        gravityOn = v;
    }

}
