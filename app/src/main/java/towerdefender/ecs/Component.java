package towerdefender.ecs;

public abstract class Component {
    public GameObject gameObject = null;

    public void start() {
    }

    public abstract void update(float dt);

    public void cleanup() {
    }
}
