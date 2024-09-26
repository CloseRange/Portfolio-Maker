package closerange.portfolio.frames;

import java.awt.Color;
import java.util.ArrayList;

import closerange.display.GuiFrame;
import closerange.portfolio.projects.Collaborator;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Loader;
import closerange.portfolio.util.Texture;
import imgui.ImGui;
import imgui.type.ImString;

public class CollaboratorFrame extends GuiFrame {

    public CollaboratorFrame() {
        super("Collaborators");
    }

    ImString iName = new ImString(128);
    ImString iLink = new ImString(128);
    Texture icon = null;
    @Override
    public void onGui() {
        if(!Loader.loaded()) return;
        ArrayList<Collaborator> collabs = Collaborator.getAll();

        ImGui.text("Create New Collaborator");
        PropertiesRender.renderInputText("Name", iName);
        PropertiesRender.renderInputText("Link", iLink);
        icon = PropertiesRender.viewTexture(icon, false);
        boolean valid = iName.get().length() > 0 && icon != null && iLink.get().length() > 0;
        if(PropertiesRender.buttonToggleCorner("Create", new Color(.2f, .7f, .3f, 1f), valid)) {
            Collaborator.create(iName.get(), iLink.get(), icon.toString());
            iName.clear();
            iLink.clear();
            icon = null;
        }

        ImGui.separator();

        for(Collaborator t : collabs) {
            ImGui.pushID("collab" + t.name);
            Texture ico = Library.load(t.icon);
            if(ico != null) {
                float s = ImGui.getTextLineHeight();
                ImGui.image(ico.getTextureId(), s, s);
                ImGui.sameLine();
            }
            ImGui.text(t.name);
            ImGui.sameLine();
            if(PropertiesRender.buttonToggleCorner("X", new Color(.1f, .1f, .15f, 1f), true)) {
                collabs.remove(t);
                Collaborator.save();
                ImGui.popID();
                break;
            }
            ImGui.popID();
        }
    }
    
}
