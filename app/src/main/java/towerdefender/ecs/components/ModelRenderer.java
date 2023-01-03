package towerdefender.ecs.components;

import towerdefender.ecs.Component;
import static org.lwjgl.opengl.GL33.*;

public class ModelRenderer extends Component {

    Mesh mesh;
    @Override
    public void start(){
        mesh = gameObject.getComponent(Mesh.class);
        if(mesh == null){
            throw new RuntimeException("ModelRenderer error: mesh is null in "
                                    + gameObject.getName());
        }
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        glBindVertexArray(mesh.getVao());
        glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
        
    }
    
}
