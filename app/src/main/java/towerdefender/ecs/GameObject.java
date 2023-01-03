package towerdefender.ecs;

import java.util.List;
import java.util.ArrayList;

//gameobject: it does things based on the componets it has within it
public class GameObject {
    private String name;
    private List<Component> components;


    public GameObject(String name){
        this.name = name;
        components = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    //returns the component if available
    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    // TODO: handle exception
                    throw new RuntimeException("Error: casting component " + e.getStackTrace());
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for(int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        components.add(c);
        c.gameObject = this;
    }

    public void start(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }
    public void update(float dt){
        for(int i = 0; i < components.size(); i++){
            components.get(i).update(dt);
        }
    }
    public void cleanup(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).cleanup();
        }
    }
}
