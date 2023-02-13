package towerdefender.scene;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import towerdefender.Game;
import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Collider;
import towerdefender.engine.Camera;
import towerdefender.gfx.Renderer;
import towerdefender.gfx.Shader;
import towerdefender.gfx.Texture;

public class Scene {

    protected static Scene currentScene = null;

    protected boolean changeScene = false;
    protected float changeTime = 1f;
    protected boolean isRunning = false;

    protected Camera camera;

    protected List<GameObject> gameObjects = new ArrayList<>();
    protected List<GameObject> colliderObjects = new ArrayList<>();
    private List<GameObject> pendingAdd = new ArrayList<>();
    private List<GameObject> pendingRemove = new ArrayList<>();
    protected Texture.Cache textureCache;

    private SceneInitalizer sceneInit = null;

    public Scene(SceneInitalizer sceneInit) {
        this.sceneInit = sceneInit;
        textureCache = new Texture.Cache();
        camera = new Camera(new Vector3f(0, 2, 12f));
    }

    public void init() {
        sceneInit.loadResources(currentScene);
    }

    public void update(float dt) {
        Shader s = Renderer.getCurrentShader();
        s.uploadUniform("uView", camera.getViewMatrix());
        s.uploadUniform("uProj", camera.getProjectionMatrix());

        // s.uploadUniform("uTexture", 0);

        gameObjects.forEach(o -> o.update(dt));

        for (GameObject pend : pendingAdd) {
            gameObjects.add(pend);
            pend.start();
            if (pend.getComponent(Collider.class) != null) {
                colliderObjects.add(pend);
                pend.getComponent(Collider.class).setCollidableObjects(colliderObjects);
            }
        }
        pendingAdd.clear();
        for (GameObject pend : pendingRemove) {
            gameObjects.remove(pend);
            pend.delete();

        }
        pendingRemove.clear();

        s.unBind();
    }

    public void cleanup() {
        sceneInit.cleanup();
        gameObjects.forEach(o -> o.delete());
    }

    public void start() {
        // starts all gameobjects
        colliderObjects.clear();
        for (GameObject obj : gameObjects) {
            obj.start();
            if(obj.getComponent(Collider.class) != null){
                colliderObjects.add(obj);
            }
        }
        
        for (GameObject gameObject : colliderObjects) {
            gameObject.getComponent(Collider.class).setCollidableObjects(colliderObjects);

        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject obj) {
        // always add
        if (!isRunning) {
            gameObjects.add(obj);
            if (obj.getComponent(Collider.class) != null) {
                colliderObjects.add(obj);
            }
        }
        // start if running
        if (isRunning) {
            pendingAdd.add(obj);
        }
    }

    public void removeGameObjectFromScene(GameObject obj) {
        pendingRemove.add(obj);
    }

    public Camera getCamera() {
        return camera;
    }

    public Texture.Cache getTextureCache() {
        return textureCache;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public static void changeScene(SceneInitalizer sceneInit) {
        if (currentScene != null)
            currentScene.cleanup();
        currentScene = new Scene(sceneInit);
        currentScene.init();
        currentScene.start();
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void updateCurrentScene(float dt) {
        currentScene.update(dt);
    }

    public static Texture.Cache getCurrentCache() {
        return currentScene.textureCache;
    }
}
