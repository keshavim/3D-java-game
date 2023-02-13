package towerdefender.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.system.MemoryUtil.NULL;;

public class Window {
    private long handle;// the actual window
    private int width, height, fbWidth, fbHeight;
    private String title;

    private static Window instance = null;

    // sigoltion get
    public static Window get() {
        if (Window.instance == null)
            Window.instance = new Window();
        return Window.instance;
    }

    public static void init(String title, int width, int height) {
        // seting error calback
        GLFWErrorCallback.createPrint(System.err).set();
        // init glfw
        if (!glfwInit())
            throw new IllegalStateException("failled to init Glfw");
        // set window hints
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        // set opengl profile
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        // seting the width, height and title
        get().title = title;
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        if (width > 0 && height > 0) {
            get().width = width;
            get().height = height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            if (vidMode != null) {
                get().width = vidMode.width() / 2;
                get().height = vidMode.height() / 2;
            }
        }
        // create window
        get().handle = glfwCreateWindow(get().width, get().height, get().title, NULL, NULL);
        if (get().handle == NULL)
            throw new IllegalStateException("failed to create window");
        glfwSetCursorPos(get().handle, width / 2, height / 2);
        glfwSetWindowPos(get().handle, (vidMode.width() - get().width) / 2, (vidMode.height() - get().height) / 2);

        // callbacks
        Input.setCallBacks(get().handle);
        glfwSetFramebufferSizeCallback(get().handle, (window, w, h) -> {
            if (w > 0 && h > 0 && (get().fbWidth != w || get().fbHeight != h)) {
                get().fbWidth = w;
                get().fbHeight = h;
            }
        });

        // make opengl context
        glfwMakeContextCurrent(get().handle);
        // enable v-sync
        glfwSwapInterval(1);
        glfwShowWindow(get().handle);

        glfwSetInputMode(get().handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];
        glfwGetFramebufferSize(get().handle, arrWidth, arrHeight);
        get().width = arrWidth[0];
        get().height = arrHeight[0];

    }

    // move to another class
    public static boolean shouldClose() {
        return glfwWindowShouldClose(get().handle);
    }

    public static void update() {

        glfwSwapBuffers(get().handle);
        glfwPollEvents();
    }

    public static void cleanup() {
        glfwFreeCallbacks(get().handle);
        glfwDestroyWindow(get().handle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null)
            callback.free();
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
