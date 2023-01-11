package towerdefender.scene;

import java.util.ArrayList;
import java.util.List;

import towerdefender.ecs.GameObject;
import towerdefender.engine.Camera;
import towerdefender.gfx.Texture;

public abstract class Scene {

    protected static Scene currentScene = null;

    protected boolean changeScene = false;
    protected float changeTime = 1f;
    protected boolean isRunning = false;

    protected Camera camera;

    protected List<GameObject> gameObjects = new ArrayList<>();
    protected Texture.Cache textureCache;


    public abstract void init();
    public abstract void update(float deltaTime);
    public abstract void cleanup();

    public void start(){
        //starts all gameobjects
        for(GameObject obj: gameObjects){
            obj.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject obj){
        //always add
        gameObjects.add(obj);
        //start if running
        if(isRunning){
            obj.start();
        }
    }

    public static void changeScene(int id){
        if(currentScene != null)currentScene.cleanup();
        switch(id){
            case 0:
                currentScene = new TutorialScene();
                break;
            case 1:
                currentScene = new level1Scene();
                break;
            default:
                throw new RuntimeException("Scene id does not exist");
        }
        currentScene.init();
        currentScene.start();
    }
    public Camera getCamera() {
        return camera;
    }
    public Texture.Cache getTextureCache() {
        return textureCache;
    }
    public static Scene getCurrentScene() {
        return currentScene;
    }
    public static void updateCurrentScene(float dt){
        currentScene.update(dt);
    }
    public static Texture.Cache getCurrentCache() {
        return currentScene.textureCache;
    }
}
