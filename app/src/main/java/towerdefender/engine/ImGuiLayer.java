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
            tpMax = new Vector3f(),
            tpMin = new Vector3f(),
            scaleT = new Vector3f(),
            newBase = new Vector3f(),
            dist_cmin_to_tmax = new Vector3f();

    public static float currentTime;
    public static float startTime;

    public static float towerHealth;
    public static int enemiesKilled;

    public static void init() {
        ImGui.createContext();
        imGuiGlfw.init(Window.getHandle(), true);
        imGuiGl3.init("#version 330");
        currentTime = 0;
        startTime = 0;
        towerHealth = 100;
        enemiesKilled = 0;
    }

    public static void startRender() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public static void update(float dt) {
        ImGui.begin("cool window");

        ImGui.text("|TowerDefender| FPS:" + (1.0f / dt) + " ms:" + (dt * 1000));

        ImGui.text("Timer: " + (currentTime - startTime) + " secs");
        ImGui.text("Tower Hp: " + towerHealth);
        ImGui.text("Enemies killed: " + enemiesKilled);

        if(towerHealth <= 0){

            ImGui.text("Game Over");
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
