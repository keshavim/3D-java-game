package towerdefender.ecs.components;

import towerdefender.ecs.Component;
import towerdefender.gfx.Material;
import towerdefender.gfx.Mesh;
import towerdefender.gfx.Model;
import towerdefender.gfx.Renderer;
import towerdefender.gfx.Shader;

import static org.lwjgl.opengl.GL33.*;

//!Need to bugfix all model reladed clases

//renders the model assosiated with the gameobject
public class ModelRenderer extends Component {

    Model model;

    public ModelRenderer(Model model) {
        this.model = model;
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub

        renderDefaultColor();
    }

    private void renderDefaultColor() {
        Shader shader = Renderer.getCurrentShader();
        Transform t = gameObject.getComponent(Transform.class);
        shader.uploadUniform("uModel", t.getModelMatrix());

        Mesh mesh = model.getMeshs().get(0);

        Material material = model.getMaterials().get(mesh.getMaterialIndex());

        shader.uploadUniform("u_Color", material.getDiffuseColor());

        glBindVertexArray(mesh.getVao());
        glDrawElements(GL_TRIANGLES, mesh.getNumElement(), GL_UNSIGNED_INT, 0);

    }

    @Override
    public void cleanup() {

    }
}
