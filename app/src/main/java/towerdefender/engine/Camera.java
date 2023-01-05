package towerdefender.engine;

import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector3f position, direction, right, up;
    private Vector2f rotation;

    private final float fov = (float)Math.toRadians(60), zNear = 0.1f, zFar = 1000f;

    public Camera(Vector3f position){
        this.position = position;
        rotation = new Vector2f();
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();

        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();

        recalculate();
        adjustProjection();

    }
    //recaulates the view matrix from an identity with new rotation and position values
    public void recalculate(){
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }
    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.setPerspective(fov, ((float)(Window.getWidth()/Window.getHeight())), zNear, zFar);
    }

    public void move(float xInc, float yInc, float zInc){
        viewMatrix.positiveX(right).mul(xInc);
        position.add(right);

        viewMatrix.positiveY(up).mul(xInc);
        position.add(up);

        viewMatrix.positiveZ(direction).negate().mul(xInc);
        position.add(direction);

        recalculate();
    }

    public void moveUp(float inc){
        //gets the direction for the spacific axis multiplied by inc
        viewMatrix.positiveY(up).mul(inc);
        //adds or subs it to the position based on direction
        position.add(up);
        recalculate();
    }
    public void moveDown(float inc){
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        recalculate();
    }
    public void moveRight(float inc){
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        recalculate();
    }
    public void moveLeft(float inc){
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        recalculate();
    }
    public void moveForward(float inc){
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        recalculate();
    }
    public void moveBackward(float inc){
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        recalculate();
    }


    public void setPosition(float x, float y, float z){
        position.set(x,y,z);
        recalculate();
    }
    public Vector3f getPosition(){return position;}

    public void setRotation(float x, float y){
        rotation.set((float)Math.toRadians(x),(float) Math.toRadians(y));
        recalculate();
    }
    public void addRotation(float x, float y){
        rotation.add((float)Math.toRadians(x), (float)Math.toRadians(y));
        recalculate();
    }

    public Matrix4f getViewMatrix(){return viewMatrix;}
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public Matrix4f getViewProj(){return projectionMatrix.mul(viewMatrix);}
}
