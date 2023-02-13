package towerdefender.scene;

import towerdefender.ecs.GameObject;

import towerdefender.ecs.components.Collider;
import towerdefender.ecs.components.ModelRenderer;
import towerdefender.ecs.components.PlayerController;
import towerdefender.ecs.components.RigidBody;
import towerdefender.ecs.components.cubectrl;
import towerdefender.engine.Prefabs;
import towerdefender.gfx.Material;
import towerdefender.gfx.Mesh;
import towerdefender.gfx.Model;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.tinylog.Logger;

public class TutorialScene implements SceneInitalizer {





    
    
    
    
    
    float[] vertices = new float[] {
        // VO
        -0.5f, 0.5f, 0.5f,
        // V1
        -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,
    };
    int[] indices = new int[] {
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            4, 0, 3, 5, 4, 3,
            // Right face
            3, 2, 7, 5, 3, 7,
            // Left face
            6, 1, 0, 6, 0, 4,
            // Bottom face
            2, 1, 6, 2, 6, 7,
            // Back face
            7, 6, 4, 7, 4, 5,
    };
    Model cubeModel, bulletModel, enemyModel, towerModel;

    public void loadResources(Scene scene) {
        // temp objects
        System.out.println("in tutorial");
        cubeModel = new Model("Cube-Model", new Mesh(vertices, indices), new Material(new Vector4f(1, 0, 0, 1)));

        bulletModel = new Model("Bullet-Model", new Mesh(vertices, indices), new Material(new Vector4f(0, 0, 0, 1)));
        enemyModel = new Model("Enemy-Model", new Mesh(vertices, indices), new Material(new Vector4f(0, 0, 1, 1)));
        towerModel = new Model("tower-Model", new Mesh(vertices, indices), new Material(new Vector4f(0, 1, 1, 1)));

        GameObject player = Prefabs.createPlayer(scene, "Player", new Vector3f(0, 5, 5));
        player.getComponent(PlayerController.class).bulletModel = this.bulletModel;

        GameObject enemy = Prefabs.createBasicEnemy(scene, enemyModel, "Enemy", new Vector3f(0, 6, 0));
        //enemy.addComponent(new cubectrl());
        
        
        Prefabs.createBasicCube(scene, cubeModel, "ground", new Vector3f(0,0,-2), new Vector3f(100,1, 100));

        // e = Prefabs.createBasicCube(scene, cubeModel, "ground", new Vector3f(0,0,-3), new Vector3f(1,1, 1));
        // e.removeComponent(Collider.class);
        //e.addComponent(new cubectrl());
        // Prefabs.createSpawner(scene, cubeModel, enemyModel, "spawner", new
        // Vector3f(-7, 2,0));
        // Prefabs.createBasicEnemy(scene, enemyModel, "enemy", new Vector3f(-7, 2, 0));
        // GameObject t = Prefabs.createTower(scene, towerModel, "tower", new Vector3f(0, 2, 0));
        // t.addComponent(new RigidBody(true, false));
        // t = Prefabs.createTower(scene, towerModel, "tower", new Vector3f(4, 2, 0));
        // t.addComponent(new RigidBody(false, false));
    }

    public void cleanup() {
        cubeModel.cleanup();
        bulletModel.cleanup();
        enemyModel.cleanup();
    }

}
