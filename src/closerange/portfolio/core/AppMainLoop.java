package closerange.portfolio.core;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.stb.STBImage.stbi_load;

import closerange.display.Display;
import closerange.display.DisplayLoop;
import closerange.display.ImGuiBinder;
import closerange.display.Window;
import closerange.portfolio.util.FrameManager;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Loader;

public class AppMainLoop implements DisplayLoop {
    private static Display display;

    public static void runProgram(String name) {
        ImGuiBinder.addFont("Roboto", 30, "res/fonts/Roboto-Regular.ttf");
        display = new Display(name, 1920, 1080, false, new AppMainLoop());

        Window window = display.getWindow();
        GLFWImage.Buffer images = loadIconImage("res/textures/PortfolioMaker_Logo2.png");
        GLFW.glfwSetWindowIcon(window.getID(), images);
        display.start();
    }

    private static GLFWImage.Buffer loadIconImage(String imagePath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Image loading logic (e.g., using stb_image)
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer imageData = stbi_load(imagePath, w, h, comp, 0);

            GLFWImage image = GLFWImage.malloc();
            image.set(w.get(0), h.get(0), imageData);

            GLFWImage.Buffer images = GLFWImage.malloc(1);
            images.put(0, image);
            return images;
        }
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onFileDrop(String path) {
        Library.copyFileToProject(path);
    }

    @Override
    public void onStart() {
        Loader.loadSite();
        FrameManager.startAll();
    }

    @Override
    public void onTick(float dt) {
    }

}
