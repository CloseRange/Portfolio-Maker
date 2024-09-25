package closerange.portfolio.core;

import closerange.display.Display;
import closerange.display.DisplayLoop;
import closerange.display.ImGuiBinder;
import closerange.portfolio.util.FrameManager;

public class AppMainLoop implements DisplayLoop {
    private static Display display;

    public static void runProgram(String name) {
        ImGuiBinder.addFont("Roboto", 30, "res/fonts/Roboto-Regular.ttf");
        display = new Display(name, 1920, 1080, false, new AppMainLoop());
        display.start();
    }
    @Override
    public void onClose() {
    }

    @Override
    public void onFileDrop(String dt) {
    }

    @Override
    public void onStart() {
        FrameManager.startAll();
    }

    @Override
    public void onTick(float dt) {
    }
    
}
