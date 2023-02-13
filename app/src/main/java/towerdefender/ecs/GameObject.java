package towerdefender.ecs;

import java.util.List;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import towerdefender.ecs.components.Transform;

import towerdefender.scene.Scene;

import java.util.ArrayList;

//gameobject: it does things based on the componets it has within it
public class GameObject {
    private String name;
    private List<Component> components;

    private GameObject parent;
    private List<GameObject> children;
    public Transform transform;

    public GameObject(String name) {
        init(name);
        addComponent(new Transform());
        transform = getComponent(Transform.class);
    }

    public GameObject(String name, Transform transform) {
        init(name);
        addComponent(transform);
        transform = getComponent(Transform.class);
    }

    public GameObject(String name, Vector3f position) {
        init(name);
        addComponent(new Transform(position));
        transform = getComponent(Transform.class);
    }

    public GameObject(String name, Vector3f position, Quaternionf rotation) {
        init(name);
        addComponent(new Transform(position, rotation));
        transform = getComponent(Transform.class);
    }

    public GameObject(String name, Vector3f position, Quaternionf rotation, Vector3f scale) {
        init(name);
        addComponent(new Transform(position, rotation, scale));
        transform = getComponent(Transform.class);
    }

    public void init(String name) {
        this.name = name;
        components = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    // returns the component if available
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    // TODO: handle exception
                    throw new RuntimeException("Error: casting component " + e.getStackTrace());
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void delete() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).cleanup();
        }

    }

    public GameObject getGameObject(String name) {
        GameObject obj = null;
        List<GameObject> gameObjects = Scene.getCurrentScene().getGameObjects();
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getName() == name)
                obj = gameObject;
        }
        return obj;
    }
}
