package towerdefender.engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

import towerdefender.scene.Scene;

import static org.joml.Math.*;
import static org.lwjgl.glfw.GLFW.*;

//todo:check if mouse input works proporly

//singleton that controls Input
public class Input {
    private static Input instance = null;
    public static final float MOUSE_SENSITIVITY = 0.01f;

    // for keyboard
    private boolean[] keys;

    // for mouse
    private Vector2f currentPos, lastPos, deltaPos;
    private float yaw, pitch;
    private boolean[] mouseButtons;
    private boolean firstMouse;

    private Input() {
        lastPos = new Vector2f(-1, -1);
        mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        keys = new boolean[GLFW_KEY_LAST];
        firstMouse = true;

        yaw = -90;
        pitch = 0;
    }

    public static Input get() {
        if (Input.instance == null) {
            Input.instance = new Input();
        }
        return Input.instance;
    }

    // sets all input related callbacks
    public static void setCallBacks(long windowHandle) {
        glfwSetCursorPosCallback(windowHandle, (handle, xposIn, yposIn) -> {
            float xpos = (float) xposIn;
            float ypos = (float) yposIn;

            if (get().firstMouse) {
                get().lastPos.x = xpos;
                get().lastPos.y = ypos;
                get().firstMouse = false;
            }
            float deltax = (xpos - get().lastPos.x) * MOUSE_SENSITIVITY;
            float deltay = (get().lastPos.y - ypos) * MOUSE_SENSITIVITY;
            get().lastPos.set(xpos, ypos);
            Scene.getCurrentScene().getCamera().setDirection(deltax, deltay);

        });
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode) -> {
            get().mouseButtons[button] = action != GLFW_RELEASE;
        });

        glfwSetKeyCallback(windowHandle, (handle, key, scancode, action, mods) -> {
            get().keys[key] = action != GLFW_RELEASE;// makes it true if pressed or repeated

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(handle, true);
            }
            if (key == GLFW_KEY_TAB && action == GLFW_RELEASE) {
                int mode = glfwGetInputMode(handle, GLFW_CURSOR);
                glfwSetInputMode(handle, GLFW_CURSOR,
                        mode == GLFW_CURSOR_NORMAL ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
            }
        });
    }

    public static boolean mouseButtonPressed(int button) {
        return get().mouseButtons[button];
    }

    public static boolean mouseButtonReleased(int button) {
        return !get().mouseButtons[button];
    }

    public static boolean keyPressed(int key) {
        return get().keys[key];

    }

    public static boolean keyReleased(int key) {
        return !get().keys[key];
    }

}
