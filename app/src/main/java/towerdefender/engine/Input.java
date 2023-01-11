package towerdefender.engine;


import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;


//todo:check if mouse input works proporly

//singleton that controls Input
public class Input {
    private static Input instance = null;
    public static final float MOUSE_SENSITIVITY = 1.5f;

    //for keyboard
    private boolean[] keys;

    //for mouse
    private Vector2f currentPos, lastPos, deltaPos;
    private boolean[] mouseButtons;
    private boolean inWindow;
    

    private Input(){
        lastPos = new Vector2f(-1,-1);
        currentPos = new Vector2f();
        deltaPos = new Vector2f();
        mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        keys = new boolean[GLFW_KEY_LAST];
        inWindow = false;
    }
    public static Input get(){
        if(Input.instance == null){
            Input.instance = new Input();
        }
        return Input.instance;
    }

    //sets all input related callbacks
    public static void setCallBacks(long windowHandle){
        glfwSetCursorPosCallback(windowHandle, (handle,  xpos, ypos)->{
            get().currentPos.x = (float) xpos;
            get().currentPos.y = (float) ypos;

            
        });
        glfwSetCursorEnterCallback(windowHandle, (handle, entered)-> get().inWindow = entered);
        glfwSetMouseButtonCallback(windowHandle, (handle, button, action, mode)->{
            get().mouseButtons[button] = action != GLFW_RELEASE;
        });

        glfwSetKeyCallback(windowHandle, (handle, key, scancode, action, mods) -> {
            get().keys[key] = action != GLFW_RELEASE;//makes it true if pressed or repeated

            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(handle, true);
            }
            if(key == GLFW_KEY_TAB && action == GLFW_RELEASE){
                int mode = glfwGetInputMode(handle, GLFW_CURSOR);
                glfwSetInputMode(handle, GLFW_CURSOR,
                    mode == GLFW_CURSOR_NORMAL ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
            }
        });
    }
    public static void mouseInput(){
        //gets the amount the mouse moves, delta position
        get().deltaPos.zero();
        if(get().inWindow){
            double deltax = get().currentPos.x - get().lastPos.x;
            double deltay = get().currentPos.y - get().lastPos.y;

            if(deltax != 0){
                get().deltaPos.x = (float) deltax * MOUSE_SENSITIVITY;
            }
            if(deltay != 0){
                get().deltaPos.y = (float) deltay * MOUSE_SENSITIVITY;
            }
        }
        get().lastPos.x = (float)get().currentPos.x;
        get().lastPos.y = (float)get().currentPos.y;
    }

    public static Vector2f getCurrentPos() {
        return get().currentPos;
    }
    public static Vector2f getDeltaPos() {
        return get().deltaPos;
    }

    public static boolean mouseButtonPressed(int button) {
        return get().mouseButtons[button];
    }
    public static boolean mouseButtonReleased(int button) {
        return !get().mouseButtons[button];
    }


    public static boolean keyPressed(int key){
        return get().keys[key];

    }
    public static boolean keyReleased(int key){
        return !get().keys[key];
    }

}

