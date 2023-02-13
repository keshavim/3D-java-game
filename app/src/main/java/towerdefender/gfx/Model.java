package towerdefender.gfx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import static org.lwjgl.assimp.Assimp.*;

//!there is no lighting 
//! more complicated models dont load proporly
//! need to do a rewrite eventually

//stores the material and meshes for a model that is loaded from an obj file
public class Model {
    AIScene scene;
    String id;
    List<Material> materials;
    List<Mesh> meshs;

    public Model(String id, Mesh mesh, Material material) {
        this.id = id;
        materials = new ArrayList<>();
        meshs = new ArrayList<>();
        this.materials.add(material);
        this.meshs.add(mesh);
    }

    public Model(String id, String path) {
        materials = new ArrayList<>();
        meshs = new ArrayList<>();
        loadModel(id, path, aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices |
                aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace
                | aiProcess_LimitBoneWeights |
                aiProcess_PreTransformVertices);
    }

    private void loadModel(String id, String path, int flags) {
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
        for (int i = 0; i < materialCount; ++i) {
            materials.add(new Material(AIMaterial.create(materialBuffer.get(i)), file.getParent()));
        }
        int meshCount = scene.mNumMeshes();
        PointerBuffer meshesBuffer = scene.mMeshes();
        for (int i = 0; i < meshCount; ++i) {
            meshs.add(new Mesh(AIMesh.create(meshesBuffer.get(i))));
        }
    }

    public void cleanup() {
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

}
