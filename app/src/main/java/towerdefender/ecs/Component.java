package towerdefender.ecs;

public abstract class Component {
    public GameObject gameObject = null;

    public void start(){}
    public abstract void update();
    public void cleanup(){}
}
