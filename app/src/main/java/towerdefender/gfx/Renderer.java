package towerdefender.gfx;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;



import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector4f;

public class Renderer {

    private Callback debugproc;
    private static Vector4f bg;

    


    public Renderer(){
        GL.createCapabilities();
        debugproc = GLUtil.setupDebugMessageCallback();
        //set render settings

        bg = new Vector4f();
    }

    public void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(bg.x,bg.y,bg.z,bg.w);
    }
    public void setBg(float x, float y, float z, float w) {
        bg.set(x,y,z,w);
    }
    public Vector4f getBg() {
        return bg;
    }

    public void cleanup(){
        if(debugproc != null) debugproc.free();
    }
}
