package towerdefender.gfx;

import org.lwjgl.system.MemoryStack;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.lwjgl.opengl.GL33.*;


public class Shader {
    
    private int id;
    private String filePath;
    Map<Integer, String> sourceStrings;

    public Shader(String fileName){
        id = glCreateProgram();
        
        filePath = Shader.getFile(fileName);
        sourceStrings = readFile(filePath);


        List<Integer> compiledShaders = new ArrayList<>();
        compiledShaders.add(compileShader(GL_VERTEX_SHADER));
        compiledShaders.add(compileShader(GL_FRAGMENT_SHADER));
        
        link(compiledShaders);
    }
    
    private Map<Integer, String> readFile(String filePath){
        String source;
        //reads file
        Map<Integer, String> sourceMap = new HashMap<>();
        try{
            source = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch(IOException e){
            throw new RuntimeException("Couldnt load shader file: "+ filePath, e);
        }
        //splits the string from each type
        String[] split = source.split("(#type)( )+([a-zA-Z]+)");

        //adds the string and its type to the map
        int index = source.indexOf("#type") + 6;
        int eol = source.indexOf("\n", index);
        String pattern = source.substring(index, eol).trim();
        sourceMap.put(getShaderType(pattern), split[1]);

        index = source.indexOf("#type", eol) + 6;
        eol = source.indexOf("\n", index);
        pattern = source.substring(index, eol).trim();
        sourceMap.put(getShaderType(pattern), split[2]);

        return sourceMap;
    }

    private int getShaderType(String pattern){
        switch (pattern.toUpperCase()) {
            case "VERTEX": return GL_VERTEX_SHADER;
            case "FRAGMENT": return GL_FRAGMENT_SHADER;
            default: throw new RuntimeException("Invalid shader type: " + pattern);
        }
    }

    private int compileShader(int type){
        int shaderId = glCreateShader(type);
        String source = sourceStrings.get(type);
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE){
            throw new RuntimeException("Failed to compile shader\nType: " + type + "\nlog: "+
                                        glGetShaderInfoLog(shaderId));
        }
        glAttachShader(id, shaderId);
        return shaderId;
    }
    private void link(List<Integer> compiledShaders){
        glLinkProgram(id);
        if(glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE){
            throw new RuntimeException("ERROR linking Program " + glGetProgramInfoLog(id, 1024));
        }

        compiledShaders.forEach(s -> glDeleteShader(s));
    }

    public void bind() { glUseProgram(id);}
    public void unBind() {glUseProgram(0);}
    public void cleanup(){
        unBind();
        if(id != 0) glDeleteProgram(id);
    }
    public Map<Integer, String> getSourceFiles() {
        return sourceStrings;
    }

    //*uniform control methods
    private int getLocation(String name){
        int location = glGetUniformLocation(id, name);
        if (location < 0) {
            throw new RuntimeException("Could not find uniform [" + name + "] in shader program [" +
                    id + "]");
        }
        return location;
    }
    public void uploadUniform(String name, int value){
        glUniform1i(getLocation(name), value);
    }
    public void uploadUniform(String name, float value){
        glUniform1f(getLocation(name), value);
    }
    public void uploadUniform(String name, boolean value){
        glUniform1i(getLocation(name), value ? 1 : 0);
    }
    public void uploadUniform(String name, Vector2f value){
        glUniform2f(getLocation(name), value.x, value.y);
    }
    public void uploadUniform(String name, Vector3f value){
        glUniform3f(getLocation(name), value.x, value.y, value.z);
    }
    public void uploadUniform(String name, Vector4f value){
        glUniform4f(getLocation(name), value.x, value.y, value.z, value.w);
    }
    public void uploadUniform(String name, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()){
            glUniformMatrix4fv(getLocation(name), false, value.get(stack.mallocFloat(16)));
        }
        
    }


    public static String getFile(String file){
        return "app/src/main/resources/shaders/" + file;
    }
}
