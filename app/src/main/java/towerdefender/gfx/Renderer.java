package towerdefender.gfx;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import towerdefender.scene.Scene;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4f;

public class Renderer {

    private static Renderer instance = null;

    private Callback debugproc;
    private static Vector4f bg;

    public static Renderer get(){
        if(Renderer.instance == null) 
            Renderer.instance = new Renderer();
        return Renderer.instance;
    }

    public static void init(){
        GL.createCapabilities();
        get().debugproc = GLUtil.setupDebugMessageCallback();
        //set render settings

        bg = new Vector4f();
    }

    public static void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(bg.x,bg.y,bg.z,bg.w);
    }
    public static void setBg(float x, float y, float z, float w) {
        bg.set(x,y,z,w);
    }
    public static Vector4f getBg() {
        return bg;
    }

    public static void cleanup(){
        if(get().debugproc != null) get().debugproc.free();
    }
}
