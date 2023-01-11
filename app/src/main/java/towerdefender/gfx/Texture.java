package towerdefender.gfx;



import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;


//todo add a texture cache sub class
public class Texture {
    private int id;
    private String filePath;

    public Texture(int width, int height, ByteBuffer buf, int format){
        filePath = "generated";
        generateTexture(width, height, buf, format);
    }
    public Texture(String fileName){
        try(MemoryStack stack = MemoryStack.stackPush()){
            this.filePath = fileName;
            stbi_set_flip_vertically_on_load(true);
    
            //loading the file and geting infomation to generate texture
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
    
            ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
    
            if(image == null){
                throw new RuntimeException("failed to load image at path "+ filePath + stbi_failure_reason());
            }
    
            generateTexture(width.get(), height.get(), image, channels.get(0) == 3 ? GL_RGB : GL_RGBA);
    
            stbi_image_free(image);

        }

    }
    //generating the texture 
    private void generateTexture(int width, int height, ByteBuffer buf, int format){
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height,
                    0, format, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }
    public void cleanup(){
        glDeleteTextures(id);
    }

    public String getFilePath() {
        return filePath;
    }

    public static String getFile(String file){
        return "app/src/main/resources/images/" + file;
    }
    //stores loaded texures so the same texture isnt reloaded
    public static class Cache{
        final Texture DEFAULT_TEXTURE = new Texture(getFile("java_logo.png"));
        Map<String, Texture> textureMap;

        public Cache(){
            textureMap = new HashMap<>();
            //textureMap.put("DEFAULT", DEFAULT_TEXTURE);
        }
        public void cleanup(){
            textureMap.values().forEach(Texture::cleanup);
        }

        public void createTexture(String path){
            textureMap.computeIfAbsent(path, Texture::new);
            
        }
        public Texture getTexture(String path) {
            Texture texture = null;
            if(path != null){
                texture = textureMap.getOrDefault(path, DEFAULT_TEXTURE);
            }
            return texture;
        }
    }
}
