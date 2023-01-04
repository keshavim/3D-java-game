package towerdefender.ecs.components;

import towerdefender.ecs.Component;
import towerdefender.gfx.Mesh;
import towerdefender.gfx.Texture;

public class Material extends Component{
    
    Mesh mesh;
    Texture texture;

    public Material(Mesh mesh, Texture texture){
        this.mesh = mesh;
        this.texture = texture;
    }
    public Material(float[] vert, float[] texCoord, int[] indices, String fileName){
        mesh = new Mesh(vert, texCoord, indices);
        texture = new Texture(fileName);
    }

    public Mesh getMesh() {
        return mesh;
    }
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void cleanup(){
        mesh.cleanup();
        texture.cleanup();
    }
}
