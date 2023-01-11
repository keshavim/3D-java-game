package towerdefender.ecs.components;

import towerdefender.ecs.Component;

import towerdefender.ecs.Model;
import towerdefender.gfx.Renderer;
import towerdefender.gfx.Shader;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.List;


//!Need to bugfix all model reladed clases

//renders the model assosiated with the gameobject
public class ModelRenderer extends Component {

    Model model;

    public ModelRenderer(Model model){
        this.model = model;
    }
    @Override
    public void start(){
        
    }
    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub

        Shader shader = Renderer.getCurrentShader();
        Transform t = gameObject.getComponent(Transform.class);
        shader.uploadUniform("uModel", t.getModelMatrix());
        
        for(Model.Mesh mesh: model.getMeshs()){

            Model.Material material = model.getMaterials().get(mesh.getMaterialIndex());
            glActiveTexture(GL_TEXTURE0);
            if(material.getTexture() != null)
                material.getTexture().bind();
            shader.uploadUniform("material.diffuse", material.getDiffuseColor());
            

            glBindVertexArray(mesh.getVao());
            glDrawElements(GL_TRIANGLES, mesh.getNumElement(), GL_UNSIGNED_INT, 0);
        }
    }

    @Override
    public void cleanup(){
        model.cleanup();
    }
}
