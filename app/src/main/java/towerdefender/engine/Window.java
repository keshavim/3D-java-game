package towerdefender.engine;



import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long handle;//the actual window
    private int width, height;
    private String title;

    private static Window instance = null;

     //sigoltion get
    public static Window get(){
        if(Window.instance == null) 
            Window.instance = new Window();
        return Window.instance;
    }

    public static void init(String title, int width, int height){
        //seting error calback
        GLFWErrorCallback.createPrint(System.err).set();
        //init glfw
        if(!glfwInit()) throw new IllegalStateException("failled to init Glfw");
        //set window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        //set opengl profile
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        //seting the width, height and title
        get().title = title;
        if(width > 0 && height > 0){
            get().width = width;
            get().height = height;
        } else{
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if(vidMode != null){
                get().width = vidMode.width() /2;
                get().height = vidMode.height() /2;
            }
        }

        //create window
        get().handle = glfwCreateWindow(get().width, get().height, get().title, NULL, NULL);
        if(get().handle == NULL) throw new IllegalStateException("failed to create window");
        
        //callbacks
        Input.setCallBacks(get().handle);
        
        //make opengl context
        glfwMakeContextCurrent(get().handle);
        //enable v-sync
        glfwSwapInterval(1);
        glfwShowWindow(get().handle);

    }

    //move to another class
    public static boolean shouldClose(){
        return glfwWindowShouldClose(get().handle);
    }
    public static void pollEvents(){
        glfwPollEvents();
    }

    public static void update(){
        glfwSwapBuffers(get().handle);
    }

    public static void cleanup(){
        glfwFreeCallbacks(get().handle);
        glfwDestroyWindow(get().handle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if(callback != null) callback.free();
    }

    public static long getHandle() {
        return get().handle;
    }
    public static int getWidth() {
        return get().width;
    }
    public static int getHeight() {
        return get().height;
    }
    public static String getTitle() {
        return get().title;
    }

}



