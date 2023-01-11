package towerdefender.ecs;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import towerdefender.gfx.Texture;
import towerdefender.scene.Scene;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL33.*;

//!there is no lighting 
//! more complicated models dont load proporly

//stores the material and meshes for a model that is loaded from an obj file
public class Model {
    AIScene scene;
    String id;
    List<Material> materials;
    List<Mesh> meshs;

    public Model(String id, String path){
        materials = new ArrayList<>();
        meshs = new ArrayList<>();
        loadModel(id, path, aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices |
                aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace | aiProcess_LimitBoneWeights |
                aiProcess_PreTransformVertices);
    }
    private void loadModel(String id, String path, int flags){
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("Model path does not exist [" + path + "]");
        }

        AIScene scene = aiImportFile(path, flags);
        if (scene == null) {
            throw new RuntimeException("Error loading model [path: " + path + "]");
        }
        this.scene = scene;

        
        
        int materialCount = scene.mNumMaterials();
        PointerBuffer materialBuffer = scene.mMaterials();
        for(int i = 0; i < materialCount; ++i){
            materials.add(new Material(AIMaterial.create(materialBuffer.get(i)), file.getParent()));
        }
        int meshCount = scene.mNumMeshes();
        PointerBuffer meshesBuffer = scene.mMeshes();
        for(int i = 0; i < meshCount; ++i){
            meshs.add(new Mesh(AIMesh.create(meshesBuffer.get(i))));
        }
    }
    
    public void cleanup(){
        aiReleaseImport(scene);
        scene = null;
        materials = null;
        meshs = null;
    }
    
    public String getId() {
        return id;
    }
    public List<Material> getMaterials() {
        return materials;
    }
    public List<Mesh> getMeshs() {
        return meshs;
    }
    //contains the buffer objects and sends data to the gpu
    public static class Mesh{
        public AIMesh mesh;
        public int vao, vertexBo, textureBo, elementBo, numElement; 
        List<Integer> vboList;


        public Mesh(AIMesh mesh){
            this.mesh = mesh;
            vboList = new ArrayList<>();
        
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            //vertices
            int vbo = glGenBuffers();
            vboList.add(vbo);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            AIVector3D.Buffer vertices = mesh.mVertices();
            nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * vertices.remaining(),
                                                        vertices.address(), GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT,false,  0,0);
            glEnableVertexAttribArray(0);
            //normals
            vbo = glGenBuffers();
            vboList.add(vbo);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            AIVector3D.Buffer normals = mesh.mNormals();
            nglBufferData(GL_ARRAY_BUFFER, AIVector3D.SIZEOF * normals.remaining(),
                                                        normals.address(), GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT,false,  0,0);
            glEnableVertexAttribArray(1);
            //texture
            vbo = glGenBuffers();
            vboList.add(vbo);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, getTextureBuffer(), GL_STATIC_DRAW);
            glVertexAttribPointer(2, 2, GL_FLOAT,false,  0,0);
            glEnableVertexAttribArray(2);
            //element
            vbo = glGenBuffers();
            vboList.add(vbo);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, getElementBuffer(), GL_STATIC_DRAW);
        }

        public int getNumElement() {
            return numElement;
        }

        public int getVao() {
            return vao;
        }
        public int getMaterialIndex(){
            return mesh.mMaterialIndex();
        }

        public void cleanup() {
            vboList.forEach(bo -> glDeleteBuffers(bo));
            glDeleteVertexArrays(vao);
        }

        private FloatBuffer getTextureBuffer(){
            FloatBuffer returnbuf;
            AIVector3D.Buffer buffer = mesh.mTextureCoords(0);
            if (buffer == null) {
                returnbuf = MemoryUtil.memCallocFloat(0);
            }else{
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
        private IntBuffer getElementBuffer(){
            int facesCount = mesh.mNumFaces();
            numElement = facesCount * 3;
            IntBuffer elementBufferData = MemoryUtil.memCallocInt(numElement);
            AIFace.Buffer facesBuffer = mesh.mFaces();
            for(int i = 0; i < facesCount; ++i){
                AIFace face = facesBuffer.get(i);
                if(face.mNumIndices() != 3)
                    throw new IllegalStateException("AIFace.mNumIndices() != 3");
                elementBufferData.put(face.mIndices());
            }
            return elementBufferData.flip();
        }

    } 
    //contains the texture path and color information about the texture
    public static class Material{
        public static final Vector4f DEFAULT_DIFFUSE = new Vector4f(0,0,0,1);

        String texturePath;

        private Vector4f diffuseColor, ambientColor, specularColor;

        public Material(AIMaterial material, String modelDir){
            diffuseColor = new Vector4f();
            ambientColor = new Vector4f();
            specularColor = new Vector4f();
            //gets the colors
            AIColor4D color = AIColor4D.create();
            int result = aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, color);
            if(result == aiReturn_SUCCESS){
                setDiffuseColor(color);
            }
            result = aiGetMaterialColor(material, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0, color);
            if(result == aiReturn_SUCCESS){
                setAmbientColor(color);
            }
            result = aiGetMaterialColor(material, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, color);
            if(result == aiReturn_SUCCESS){
                setSpecularColor(color);
            }

            //creates the texture
            try(MemoryStack stack = MemoryStack.stackPush()){
                AIString aiTexturePath = AIString.calloc(stack);
                aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, aiTexturePath, (IntBuffer)null,
                        null, null, null, null, null);

                String texturePath = aiTexturePath.dataString();
                if(texturePath != null && texturePath.length() > 0){
                    this.texturePath = (modelDir + File.separator + aiTexturePath.dataString());
                    Scene.getCurrentCache().createTexture(this.texturePath);
                    diffuseColor.set(Material.DEFAULT_DIFFUSE);
                }
            }
        }
        public void setDiffuseColor(AIColor4D color) {
            if(color != null)
            diffuseColor.set(color.r(), color.g(), color.b(), color.a());
        }
        public Vector4f getDiffuseColor() {
            return diffuseColor;
        }
        public void setAmbientColor(AIColor4D color) {
            if(color != null)
            ambientColor.set(color.r(), color.g(), color.b(), color.a());
        }
        public Vector4f getAmbientColor() {
            return ambientColor;
        }
        public void setSpecularColor(AIColor4D color) {
            if(color != null)
            specularColor.set(color.r(), color.g(), color.b(), color.a());
        }
        public Vector4f getSpecularColor() {
            return specularColor;
        }
        public Texture getTexture() {
            Texture.Cache cache = Scene.getCurrentCache();
            return cache.getTexture(texturePath);
        }
    }

}
