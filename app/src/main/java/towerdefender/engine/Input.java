package towerdefender.engine;


import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;


//todo:check if mouse input works proporly

//singleton that controls Input
public class Input {
    private static Input instance = null;
    public static final float MOUSE_SENSITIVITY = 0.1f;

    //for keyboard
    private boolean[] keys;

    //for mouse
    private Vector2f currentPos, lastPos, displVec;
    private boolean[] mouseButtons;
    private boolean inWindow;
    

    private Input(){
        lastPos = new Vector2f(-1,-1);
        currentPos = new Vector2f();
        displVec = new Vector2f();
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
        });
    }
    //gets mouse positions and deltas
    public static void mouseInput(){
        get().displVec.zero();
        if(get().lastPos.x > 0 && get().lastPos.y > 0 && get().inWindow){
            double deltax = get().currentPos.x - get().lastPos.x;
            double deltay = get().currentPos.y - get().lastPos.y;

            if(deltax != 0){
                get().displVec.x = (float) deltax;
            }
            if(deltay != 0){
                get().displVec.y = (float) deltay;
            }
        }
        get().lastPos.set(get().currentPos);
    }

    public static Vector2f getCurrentPos() {
        return get().currentPos;
    }
    public static Vector2f getDisplVec() {
        return get().displVec;
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

