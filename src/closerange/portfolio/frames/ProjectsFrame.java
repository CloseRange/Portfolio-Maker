package closerange.portfolio.frames;

import java.awt.Color;

import closerange.display.GuiFrame;
import closerange.portfolio.projects.Project;
import closerange.portfolio.util.Loader;
import imgui.ImGui;
import imgui.type.ImString;

public class ProjectsFrame extends GuiFrame {

    public ProjectsFrame() {
        super("Projects");
    }

    public static Project getSelected() {
        if(selected == -1) return null;
        return Project.getAll().get(selected);
    }
    public static void setSelected(int i) {
        selected = i;
    }

    private static ImString iName = new ImString(128);
    private static int selected = -1;
    @Override
    public void onGui() {
        if(!Loader.loaded()) return;
        ImGui.text("New Project");
        PropertiesRender.renderInputText("Name", iName);
        if(PropertiesRender.buttonToggleCorner("Create", new Color(.2f, .7f, .3f, 1f), iName.get().length() > 0)) {
            Project.create(iName.get());
            iName.clear();
        }
        ImGui.separator();

        int index = 0;
        ImGui.columns(2);
        ImGui.setColumnWidth(0, ImGui.getWindowWidth() - 75);
        ImGui.setColumnWidth(1, 75);
        PropertiesRender.pushButtonColors(new Color(.25f, .1f, .15f, 1f), false);
        for(Project proj : Project.getAll()) {
            ImGui.pushID(index);
            if(ImGui.selectable(proj.name, selected == index)) {
                selected = (selected == index) ? -1 : index;
            }
            ImGui.nextColumn();
            boolean remove = false;
            if(ImGui.button("X", 50, 0)) {
                Project.removeIndex(index);
                selected = -1;
                remove = true;
            }
            ImGui.nextColumn();
            ImGui.popID();
            index++;
            if(remove) break;
        }
        ImGui.popStyleColor(3);
        ImGui.columns(1);
    }
    
}