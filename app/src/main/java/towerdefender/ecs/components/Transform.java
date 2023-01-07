package towerdefender.ecs.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;

public class Transform extends Component{
    private Vector3f position, scale;
    private Quaternionf rotation;
    private Matrix4f modelMatrix;

    public GameObject gameObject = null;

    public Transform(){
        init(new Vector3f(), new Quaternionf(), new Vector3f(1));
    }
    public Transform(Vector3f position){
        init(position, new Quaternionf(), new Vector3f(1));
    }
    public Transform(Vector3f position, Quaternionf rotation){
        init(position, rotation, new Vector3f(1));
    }
    public Transform(Vector3f position, Quaternionf rotation, Vector3f scale){
        init(position, rotation, scale);
    }
    private void init(Vector3f position, Quaternionf rotation, Vector3f scale){
        this.position= position;
        this.rotation = rotation;
        this.scale = scale;
        modelMatrix = new Matrix4f();
        updateModel();
    }

    public void updateModel(){
        modelMatrix.translationRotateScale(position, rotation, scale);
    }
    public Matrix4f getModelMatrix() {
        updateModel();
        return modelMatrix;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void move(Vector3f offset){
        position.add(offset);
    }
    public void move(float x, float y, float z){
        position.add(x, y, z);
    }
    public Vector3f getPosition() {
        return position;
    }

    public void setRotation(float x, float y, float z, float angle) {
        rotation.fromAxisAngleRad(x, y, z, angle);
    }
    public void rotate(float x, float y, float z, float angle){
        rotation.rotateAxis(angle, x, y, z);
    }
    public void rotate(Vector3f offset, float angle){
        rotation.rotateAxis(angle, offset);
    }
    public Quaternionf getRotation() {
        return rotation;
    }

    public void setScale(float x, float y, float z) {
        scale.set(x, y, z);
    }
    public void addScale(float x, float y, float z){
        scale.add(x, y, z);
    }
    public Vector3f getScale() {
        return scale;
    }

    @Override
    public void update(float dt) {
        //updateModel();
    }


    
    
}
