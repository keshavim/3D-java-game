package towerdefender.ecs.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import towerdefender.ecs.Component;

public class Transform extends Component{
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
    private float scale;
    
    @Override
    public void start() {
        // TODO Auto-generated method stub
        modelMatrix =new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        //updates the model matrix of the transform
        modelMatrix.translationRotateScale(position, rotation, scale);
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
    public Vector3f getPosition() {
        return position;
    }
    public Quaternionf getRotation() {
        return rotation;
    }
    public float getScale() {
        return scale;
    }
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
    }
    public void setRotation(float x, float y, float z, float angle) {
        rotation.fromAxisAngleRad(x, y, z, angle);
    }
    public void setScale(float scale) {
        this.scale = scale;
    }
    //todo(maybe): add more methods for transforming
}
