package towerdefender.engine;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.BulletController;
import towerdefender.ecs.components.Collider;
import towerdefender.ecs.components.EnemyController;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.ecs.components.PlayerController;
import towerdefender.ecs.components.RigidBody;
import towerdefender.ecs.components.SpawnerController;
import towerdefender.gfx.Model;
import towerdefender.scene.Scene;

public class Prefabs {

    public static GameObject createPlayer(Scene scene, String name, Vector3f position) {
        GameObject player = new GameObject(name, position, new Quaternionf(), new Vector3f(1,1,1));
        player.addComponent(new PlayerController(scene.getCamera()));
        player.addComponent(new Collider());
        player.addComponent(new RigidBody(false, false));
        scene.addGameObjectToScene(player);
        return player;
    }

    public static GameObject createBasicBullet(Scene scene, Model model, Vector3f position, Vector3f front) {
        GameObject cube = new GameObject("bullet", position, new Quaternionf(), new Vector3f(0.1f));
        cube.addComponent(new ModelRenderer(model));
        cube.addComponent(new RigidBody(false, true));
        cube.addComponent(new Collider());
        cube.addComponent(new BulletController(front));
        scene.addGameObjectToScene(cube);
        return cube;
    }

    public static GameObject createBasicEnemy(Scene scene, Model model, String name, Vector3f position) {
        GameObject cube = new GameObject(name, position);
        cube.addComponent(new ModelRenderer(model));
        cube.addComponent(new Collider());
        cube.addComponent(new RigidBody(false, false));
        cube.addComponent(new EnemyController());
        scene.addGameObjectToScene(cube);
        return cube;
    }

    public static GameObject createTower(Scene scene, Model model, String name, Vector3f position) {
        GameObject cube = new GameObject(name, position, new Quaternionf(), new Vector3f(1, 4, 1));
        cube.addComponent(new ModelRenderer(model));
        cube.addComponent(new Collider());
        cube.addComponent(new RigidBody(true, false));
        scene.addGameObjectToScene(cube);
        return cube;
    }

    public static GameObject createBasicCube(Scene scene, Model model, String name, Vector3f position, Vector3f scale) {
        GameObject cube = new GameObject(name, position, new Quaternionf(), scale);
        cube.addComponent(new ModelRenderer(model));
        cube.addComponent(new RigidBody(true, false));
        cube.addComponent(new Collider());
        scene.addGameObjectToScene(cube);
        return cube;
    }

    public static GameObject createSpawner(Scene scene, Model thisModel, Model enemyModel, String name,
            Vector3f position) {
        GameObject cube = new GameObject(name, position, new Quaternionf(), new Vector3f(0.25f));
        if(thisModel != null)cube.addComponent(new ModelRenderer(thisModel));
        cube.addComponent(new SpawnerController(enemyModel));
        scene.addGameObjectToScene(cube);
        return cube;
    }

}
