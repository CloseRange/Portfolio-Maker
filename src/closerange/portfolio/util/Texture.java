package closerange.portfolio.util;


import org.lwjgl.BufferUtils;

import closerange.portfolio.debug.Debug;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private transient int texID;
    private transient int width, height;
    @SuppressWarnings("unused")
    private String name;
    public Texture() {
        texID = -1;
        width = -1;
        height = -1;
    }
    /// Allocate texture with certain width and height. no data
    public Texture(int width, int height) {
        this.width = width;
        this.height = height;

        // gen tex on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height,
        0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }
    public Texture setTexture(String filepath) {
        if(filepath == null) {
            logLoadError("Texture (null path)", filepath);
            return this;
        }
        name = filepath.substring(filepath.lastIndexOf('/') + 1);

        // gen tex on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // set texture params
        // set to repeat on past 1 uv
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // when stretching / shrinking
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // STRETCH
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // SHRINK

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1); // 3 or 4 for rgb/rgba

        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        if(image != null) { // upload to gpu
            if(channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else if(channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else {
                logLoadError("Texture (invalid channel count)", filepath);
            }
            this.width = width.get(0);
            this.height = height.get(0);
            stbi_image_free(image);
        } else { // no image
            logLoadError("Texture (non-exsistant)", filepath);
        }
        glBindTexture(GL_TEXTURE_2D, 0);
        return this;
    }
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getTextureId() { return texID; }
    

    protected void logLoadError(String type, String filepath) {
        Debug.error(type + " Unloaded Asset", type + "_UNLOADED_ASSET_" + filepath, "Could not load asset '" + filepath + "'");
    }
}
