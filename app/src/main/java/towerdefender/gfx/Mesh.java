package towerdefender.gfx;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {
    public AIMesh mesh;
    public int vao, numElement;
    List<Integer> vboList;

    public Mesh(float[] vertices, int[] indices) {
        this.mesh = null;
        numElement = indices.length;

        vboList = new ArrayList<>();

        //*no need for GL33. */
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        vboList.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, getBuffer(vertices), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        vbo = glGenBuffers();
        vboList.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, getBuffer(indices), GL_STATIC_DRAW);

        glBindVertexArray(0);

    }

    private FloatBuffer getBuffer(float[] arr) {
        FloatBuffer buffer = MemoryUtil.memCallocFloat(arr.length);
        return buffer.put(arr).flip();
    }

    private IntBuffer getBuffer(int[] arr) {
        IntBuffer buff = MemoryUtil.memCallocInt(arr.length);
        return buff.put(arr).flip();
    }

    public Mesh(AIMesh mesh) {
        this.mesh = mesh;
        vboList = new ArrayList<>();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // vertices
        int vbo = glGenBuffers();
        vboList.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        AIVector3D.Buffer vertices = mesh.mVertices();
        nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * vertices.remaining(),
                vertices.address(), GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        // normals
        vbo = glGenBuffers();
        vboList.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        AIVector3D.Buffer normals = mesh.mNormals();
        nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * normals.remaining(),
                normals.address(), GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        // texture
        vbo = glGenBuffers();
        vboList.add(vbo);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, getTextureBuffer(), GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);
        // element
        vbo = glGenBuffers();
        vboList.add(vbo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, getElementBuffer(), GL_STATIC_DRAW);
    }

    private FloatBuffer getTextureBuffer() {
        FloatBuffer returnbuf;
        AIVector3D.Buffer buffer = mesh.mTextureCoords(0);
        if (buffer == null) {
            returnbuf = MemoryUtil.memCallocFloat(0);
        } else {
            float[] data = new float[buffer.remaining() * 2];
            returnbuf = MemoryUtil.memCallocFloat(buffer.remaining() * 2);
            int pos = 0;
            while (buffer.remaining() > 0) {
                AIVector3D textCoord = buffer.get();
                data[pos++] = textCoord.x();
                data[pos++] = textCoord.y();

            }
            returnbuf.put(data).flip();
        }
        return returnbuf;
    }

    private IntBuffer getElementBuffer() {
        int facesCount = mesh.mNumFaces();
        numElement = facesCount * 3;
        IntBuffer elementBufferData = MemoryUtil.memCallocInt(numElement);
        AIFace.Buffer facesBuffer = mesh.mFaces();
        for (int i = 0; i < facesCount; ++i) {
            AIFace face = facesBuffer.get(i);
            if (face.mNumIndices() != 3)
                throw new IllegalStateException("AIFace.mNumIndices() != 3");
            elementBufferData.put(face.mIndices());
        }
        return elementBufferData.flip();
    }

    public int getNumElement() {
        return numElement;
    }

    public int getVao() {
        return vao;
    }

    public int getMaterialIndex() {
        if (mesh != null)
            return mesh.mMaterialIndex();
        return 0;
    }

    public void cleanup() {
        vboList.forEach(bo -> glDeleteBuffers(bo));
        glDeleteVertexArrays(vao);
    }

}
