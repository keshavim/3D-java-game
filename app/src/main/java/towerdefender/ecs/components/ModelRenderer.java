package towerdefender.ecs.components;

import towerdefender.ecs.Component;
import static org.lwjgl.opengl.GL33.*;

//renders the model assosiated with the gameobject
public class ModelRenderer extends Component {

    Material material;
    @Override
    public void start(){
        material = gameObject.getComponent(Material.class);
        if(material == null){
            throw new RuntimeException("ModelRenderer error: material is null in "
                                    + gameObject.getName());
        }
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub

        glActiveTexture(GL_TEXTURE0);
        material.getTexture().bind();

        glBindVertexArray(material.getMesh().getVao());
        glDrawElements(GL_TRIANGLES, material.getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        
    }
    
}
