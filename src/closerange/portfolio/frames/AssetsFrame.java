package closerange.portfolio.frames;

import closerange.display.GuiFrame;
import closerange.portfolio.util.Loader;
import closerange.portfolio.util.Texture;
import imgui.ImGui;

public class AssetsFrame extends GuiFrame {

    public AssetsFrame() {
        super("Assets");
    }

    @Override
    public void onGui() {
        for(Texture asset : Loader.getAllTexture()) {
            ImGui.text(asset.toString());
        }
    }
    
}
