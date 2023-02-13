package towerdefender.ecs.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import towerdefender.ecs.Component;
import towerdefender.ecs.GameObject;
import towerdefender.engine.ImGuiLayer;
import towerdefender.gfx.Renderer;

//gets the collision of the object and stors information is collsion object
public class Collider extends Component {

    private List<GameObject> collidableObjects;
    public static int ANY = 0, LEFT = 1, RIGHT = 2, TOP = 3, BOTTOM = 4, FRONT = 5, BACK = 6;
    private boolean[] side;
    public GameObject other = null;
    private Vector3f tpMin, tpMax, opMin, opMax, range;

    public Collider() {
        side = new boolean[7];
        range = new Vector3f(1);
        collidableObjects = new ArrayList<>();
    }
    
    @Override
    public void start() {

    }

    @Override
    public void update(float dt) {
        // TODO Auto-generated method stub
        checkCollision();

    }

    public void checkCollision() {
        reset();
        for (GameObject other : collidableObjects) {
            if (other == this.gameObject)
                continue;

            checkIntersect(gameObject, other);
        }

    }
    private void checkIntersect(GameObject thisObject, GameObject other) {
        Vector3f rangeT = new Vector3f(thisObject.getComponent(Transform.class).getScale());
        Vector3f rangeO = new Vector3f(other.getComponent(Transform.class).getScale());
        
        

        // gets the vetices in the botom, left, back (min) and right, top, front (max) from the objects perspactive

        Vector3f tbase = thisObject.getComponent(Transform.class).getPosition();
        Vector3f obase = other.getComponent(Transform.class).getPosition();

        opMin = new Vector3f(obase);
        tpMin = new Vector3f(tbase);
        //readjusy for scale
        opMin.x -= (rangeO.x -1) /2;
        tpMin.x -= (rangeT.x -1) /2;
        opMin.y += (rangeO.y -1) /2;
        tpMin.y += (rangeT.y -1) /2;
        opMin.z -= (rangeO.z -1) /2;
        tpMin.z -= (rangeT.z -1) /2;


        opMax = opMin.add(rangeO.x, 0, rangeO.z, new Vector3f()); 
        tpMax = tpMin.add(rangeT.x, 0, rangeT.z, new Vector3f());

        opMin.y -= rangeO.y;
        tpMin.y -= rangeO.y;

        float xDist = Vector3f.distance(tpMin.x, 0, 0, opMax.x, 0, 0);
        float yDist = Vector3f.distance(0, tpMin.y, 0, 0, opMax.y, 0);
        float zDist = Vector3f.distance(0, 0, tpMin.z, 0, 0, opMax.z);

        if(other.getName() == "ground"){
            ImGuiLayer.opMax = opMax;
            ImGuiLayer.opMin = opMin;
            ImGuiLayer.dist_cmin_to_tmax = new Vector3f(xDist, yDist, zDist);
        }

        

        //calculates collision
        boolean check_left = tpMin.x <= opMax.x;
        boolean check_right = tpMax.x >= opMin.x;
        boolean check_top = tpMin.y <= opMax.y;
        boolean check_bottom = tpMax.y >= opMin.y;
        boolean check_front = tpMin.z <= opMax.z;
        boolean check_back = tpMax.z >= opMin.z;

        if (check_back && check_bottom && check_front && check_left && check_right && check_top) {
            side[ANY] = true;
            this.other = other;

            //TODO:improve collision so it can detect static objects and movable objects
            boolean tstatic = thisObject.getComponent(RigidBody.class).isStatic;
            boolean otrigger = other.getComponent(RigidBody.class).isTrigger;


            if(!tstatic ){

                if (xDist < 0.1) {
                    side[LEFT] = true;
                    tbase.set(opMax.x, tbase.y, tbase.z);

                }
                if (zDist < 0.1) {
                    side[FRONT] = true;
                    tbase.set(tbase.x, tbase.y, opMax.z);
                }
                if (yDist < 0.1) {
                    side[BOTTOM] = true;
                    tbase.set(tbase.x, opMax.y + rangeO.y, tbase.z);
                    
                }
                if (xDist > rangeT.x + rangeO.x - 0.1f) {
                    side[RIGHT] = true;
                    tbase.set(opMin.x - rangeT.x, tbase.y, tbase.z);

                }
                if (zDist > rangeT.z + rangeO.z - 0.1f) {
                    side[BACK] = true;
                    tbase.set(tbase.x, tbase.y, opMin.z - rangeT.z);
                }
                if (yDist > rangeO.y + rangeT.y - 0.1f) {
                    side[TOP] = true;
                    tbase.set(tbase.x, opMin.y, tbase.z);
                }
            }
            

        }
    }
    private void reset() {
        for (int i = 0; i < side.length; i++) {
            side[i] = false;
        }
        other = null;
    }

    public void setRange(float x, float y, float z) {
        this.range.set(x,y,z);
    }
    public Vector3f getRange() {
        return range;
    }



    public List<GameObject> getCollidableObjects() {
        return collidableObjects;
    }

    public void setCollidableObjects(List<GameObject> collidableObjects) {
        this.collidableObjects = collidableObjects;
    }

    public void addToCollidableObjects(GameObject other) {
        collidableObjects.add(other);
    }

    public boolean getCollision(int direction) {
        return side[direction];
    }

    
}
