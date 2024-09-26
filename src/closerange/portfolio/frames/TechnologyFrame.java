package closerange.portfolio.frames;

import java.awt.Color;
import java.util.ArrayList;

import closerange.display.GuiFrame;
import closerange.portfolio.projects.Technology;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Loader;
import closerange.portfolio.util.Texture;
import imgui.ImGui;
import imgui.type.ImString;

public class TechnologyFrame extends GuiFrame {

    public TechnologyFrame() {
        super("Technologies");
    }

    ImString iName = new ImString(128);
    Texture icon = null;
    @Override
    public void onGui() {
        if(!Loader.loaded()) return;
        ArrayList<Technology> techs = Technology.getAll();

        ImGui.text("Create New Technology");
        PropertiesRender.renderInputText("Name", iName);
        icon = PropertiesRender.viewTexture(icon, false);
        boolean valid = iName.get().length() > 0 && icon != null;
        if(PropertiesRender.buttonToggleCorner("Create", new Color(.2f, .7f, .3f, 1f), valid)) {
            Technology.create(iName.get(), icon.toString());
            iName.clear();
            icon = null;
        }

        ImGui.separator();

        for(Technology t : techs) {
            ImGui.pushID("tech" + t.name);
            Texture ico = Library.load(t.icon);
            if(ico != null) {
                float s = ImGui.getTextLineHeight();
                ImGui.image(ico.getTextureId(), s, s);
                ImGui.sameLine();
            }
            ImGui.text(t.name);
            ImGui.sameLine();
            if(PropertiesRender.buttonToggleCorner("X", new Color(.1f, .1f, .15f, 1f), true)) {
                techs.remove(t);
                Technology.save();
                ImGui.popID();
                break;
            }
            ImGui.popID();
        }
    }
    
}
