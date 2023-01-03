package towerdefender.ecs.components;

import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import towerdefender.ecs.Component;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL33.*;


public class Mesh extends Component{
    private int numVertices;
    private int vao;
    private List<Integer> vboList;


    public Mesh(float[] vertices,  int[] indices){
        //auto cleans up buffers
        try(MemoryStack stack = MemoryStack.stackPush()){
            numVertices = indices.length;
            vboList = new ArrayList<>();

            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            //creating and seting the the vertex buffers
            //vertices
            int vbo = glGenBuffers();
            vboList.add(vbo);
            FloatBuffer vBuffer = stack.callocFloat(vertices.length);
            vBuffer.put(0, vertices);
            glBindBuffer(GL_ARRAY_BUFFER, vao);
            glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);

            //indices
            vbo = glGenBuffers();
            vboList.add(vbo);
            IntBuffer iBuffer = stack.callocInt(indices.length);
            iBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }
    public Mesh(float[] vertices, float[] texCoords,  int[] indices){
        //auto cleans up buffers
        try(MemoryStack stack = MemoryStack.stackPush()){
            numVertices = indices.length;
            vboList = new ArrayList<>();

            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            //creating and seting the the vertex buffers
            //vertices
            int vbo = glGenBuffers();
            vboList.add(vbo);
            FloatBuffer vBuffer = stack.callocFloat(vertices.length);
            vBuffer.put(0, vertices);
            glBindBuffer(GL_ARRAY_BUFFER, vao);
            glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);

            //texture
            vbo = glGenBuffers();
            vboList.add(vbo);
            FloatBuffer texBuffer = MemoryUtil.memAllocFloat(texCoords.length);
            texBuffer.put(0, texCoords);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, texBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(1);

            //indices
            vbo = glGenBuffers();
            vboList.add(vbo);
            IntBuffer iBuffer = stack.callocInt(indices.length);
            iBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVao() {
        return vao;
    }
    
    @Override
    public void cleanup() {
        vboList.forEach(bo -> glDeleteBuffers(bo));
        glDeleteVertexArrays(vao);
    }

}
