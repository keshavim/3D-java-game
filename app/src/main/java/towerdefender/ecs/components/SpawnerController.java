package towerdefender.ecs.components;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.engine.Prefabs;
import towerdefender.gfx.Model;
import towerdefender.scene.Scene;

public class SpawnerController extends Component {
    float spawnTimer, startTimer;
    Model model;

    public SpawnerController(Model model) {
        this.model = model;
        startTimer = 10f;
        spawnTimer = startTimer;
    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        if (spawnTimer <= 0) {
            Prefabs.createBasicEnemy(Scene.getCurrentScene(), model, "enemy",
                    gameObject.transform.getPosition().add(new Vector3f(1, 0, 0), new Vector3f()));
            spawnTimer = startTimer;
        }
        spawnTimer -= dt;
    }

}
