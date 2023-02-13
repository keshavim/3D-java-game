package towerdefender.engine;

import java.text.NumberFormat;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import towerdefender.ecs.GameObject;
import towerdefender.ecs.components.Collider;
import towerdefender.ecs.components.RigidBody;
import towerdefender.scene.Scene;

public class ImGuiLayer {

    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public static Vector3f PlayerBase = new Vector3f(),
            opMax = new Vector3f(),
            opMin = new Vector3f(),
            scaleT = new Vector3f(),
            newBase = new Vector3f(),
            dist_cmin_to_tmax = new Vector3f();

    public static void init() {
        ImGui.createContext();
        imGuiGlfw.init(Window.getHandle(), true);
        imGuiGl3.init("#version 330");
    }

    public static void startRender() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public static void update(float dt) {
        ImGui.begin("cool window");

        ImGui.text("|TowerDefender| FPS:" + (1.0f / dt) + " ms:" + (dt * 1000));

        for (GameObject obj : Scene.getCurrentScene().getGameObjects()) {
            if (obj.getName() == "Player") {
                ImGui.text("player");
                ImGui.text("Position: "+obj.transform.getPosition().toString(NumberFormat.getInstance()));
                
                Collider col = obj.getComponent(Collider.class);
                ImGui.text("collision: "+col.getCollision(0));
                ImGui.text("left: "+col.getCollision(1));
                ImGui.text("right: "+col.getCollision(2));
                ImGui.text("top: "+col.getCollision(3));
                ImGui.text("bottom: "+col.getCollision(4));
                ImGui.text("front: "+col.getCollision(5));
                ImGui.text("back: "+col.getCollision(6));
                ImGui.text("Dist: "+ dist_cmin_to_tmax.toString(NumberFormat.getInstance()));
                
                
            }
            if (obj.getName() == "ground") {
                ImGui.text("ground");
                ImGui.text("Position: "+obj.transform.getPosition().toString(NumberFormat.getInstance()));
                ImGui.text("opMin"+ opMin.toString(NumberFormat.getInstance()));
                ImGui.text("opMax"+ opMax.toString(NumberFormat.getInstance()));
                
                




            }
            
        }

        ImGui.end();
    }

    public static void endRender() {

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }

    }

    public static void cleanup() {

        imGuiGl3.dispose();
        imGuiGlfw.dispose();
    }

}
