package towerdefender.ecs.components;

import towerdefender.ecs.Component;

public class RigidBody extends Component {

    private float gravity = -2.5f;
    private boolean gravityOn = true;
    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        if(gravityOn)
            gameObject.getComponent(Transform.class).move(0, gravity * dt, 0);

        

    }
    
}
