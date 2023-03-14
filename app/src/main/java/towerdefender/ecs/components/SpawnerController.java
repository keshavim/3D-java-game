package towerdefender.ecs.components;



import java.util.Random;
import org.joml.Vector3f;
import org.tinylog.Logger;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.engine.Prefabs;
import towerdefender.gfx.Model;
import towerdefender.scene.Scene;

public class SpawnerController extends Component {
    float spawnTimer, startTimer;
    Random random;
    Model model;

    public SpawnerController(Model model) {
        this.model = model;
        random = new Random();
        startTimer = 3f;
        spawnTimer = startTimer;
        
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        if (spawnTimer <= 0) {
            Vector3f offset = new Vector3f();
            Vector3f pos = gameObject.transform.getPosition();
            float sign = random.nextInt(1);

            if(Math.abs(pos.x) == 50){
                int base = random.nextInt(50) * ((sign == 0) ? 1 : -1);
                
                offset.set(1 * Integer.signum((int)pos.x), 0, base);
            }else if(Math.abs(pos.z) == 50){
                int base = random.nextInt(50) * ((sign == 0) ? -1 : 1);
                offset.set(base, 0, 1 * Integer.signum((int)pos.z));
            }

            
            Vector3f position = pos.add(offset, new Vector3f());
            Prefabs.createBasicEnemy(Scene.getCurrentScene(), model, "enemy",
                    position);
            spawnTimer = startTimer + random.nextInt(7);
        }
        spawnTimer -= dt;
    }

}
