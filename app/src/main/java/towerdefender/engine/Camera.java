package towerdefender.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Transform;

import static org.joml.Math.toRadians;
import static org.joml.Math.sin;
import static org.joml.Math.cos;
import static org.lwjgl.glfw.GLFW.glfwGetInputMode;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

//!need to fix camera rotation and interaction with transform
public class Camera {
    private Matrix4f projectionMatrix, viewMatrix, viewProj;
    private Vector3f position, front, right, up, worldup;

    private float yaw, pitch;

    private final float fov = (float) Math.toRadians(60), zNear = 0.1f, zFar = 1000f;

    public Camera(Vector3f position) {
        this.position = position;
        front = new Vector3f(0, 0, -1f);
        right = new Vector3f(1f, 0, 0);
        up = new Vector3f(0, 1f, 0);
        worldup = new Vector3f(0, 1f, 0);

        yaw = -90;
        pitch = 0;

        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        viewProj = new Matrix4f();

        adjustProjection(Window.getWidth(), Window.getHeight());
        updateVectors();

    }

    public void adjustProjection(int width, int height) {
        projectionMatrix.identity();
        projectionMatrix.setPerspective(fov, ((float) (width / height)), zNear, zFar);
    }

    public void setDirection(float deltax, float deltay) {
        yaw += deltax;
        pitch += deltay;

        if (pitch > 89.0f)
            pitch = 89.0f;
        if (pitch < -89.0f)
            pitch = -89.0f;

        updateVectors();
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        // recalculate();
    }

    public void setPosition(Vector3f other) {
        position.set(other);
        // recalculate();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void move(float x, float y, float z) {

        position.add(x, y, z);
    }

    public void move(Vector3f offset) {
        position.add(offset);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity();
        Vector3f center = new Vector3f();
        position.add(front, center);
        return viewMatrix.lookAt(position, center, up);
    }

    public Matrix4f getViewProj() {
        projectionMatrix.mul(viewMatrix, viewProj);
        return viewProj;
    }

    public Vector3f getFront() {
        return front;
    }

    public Vector3f getRight() {
        return right;
    }

    public Vector3f getUp() {
        return up;
    }

    private void updateVectors() {

        if (glfwGetInputMode(Window.getHandle(), GLFW_CURSOR) == GLFW_CURSOR_NORMAL)
            return;

        Vector3f front = new Vector3f();
        front.x = cos(toRadians(yaw)) * cos(toRadians(pitch));
        front.y = sin(toRadians(pitch));
        front.z = sin(toRadians(yaw)) * cos(toRadians(pitch));
        front.normalize(this.front);

        front.cross(worldup, right);
        right.normalize();
        right.cross(front, up);
        up.normalize();
    }
}
