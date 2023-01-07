package towerdefender.gfx;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;



import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4f;

public class Renderer {

    private Callback debugproc;
    private static Vector4f bg;

    private static Shader curentShader;


    public Renderer(){
        GL.createCapabilities();
        debugproc = GLUtil.setupDebugMessageCallback();
        //set render settings

        bg = new Vector4f();

        glEnable(GL_DEPTH_TEST);
    }

    public void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(bg.x,bg.y,bg.z,bg.w);
    }
    public static void setBg(float x, float y, float z, float w) {
        bg.set(x,y,z,w);
    }
    public static Vector4f getBg() {
        return bg;
    }

    public void cleanup(){
        if(debugproc != null) debugproc.free();
    }

    public static void bindShader(Shader shader){
        curentShader = shader;
        curentShader.bind();
    }
    public static Shader getCurrentShader() {
        return curentShader;
    }
}
