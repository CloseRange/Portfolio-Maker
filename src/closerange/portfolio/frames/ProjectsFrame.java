package closerange.portfolio.frames;

import closerange.display.GuiFrame;
import closerange.portfolio.projects.Project;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class ProjectsFrame extends GuiFrame {

    public ProjectsFrame() {
        super("Projects");
    }
    @Override
    public int getFlags() {
        return ImGuiWindowFlags.MenuBar;
    }

    public static Project getSelected() {
        if(selected == -1) return null;
        return Project.getAll().get(selected);
    }
    public static void setSelected(int i) {
        selected = i;
    }

    private static ImString name = new ImString(128);
    private static boolean creatingProject = false;
    private static int selected = -1;
    @Override
    public void onGui() {
        if(ImGui.beginMenuBar()) {
            if(ImGui.beginMenu("Edit")) {
                if(ImGui.menuItem("Add Project")) {
                    creatingProject = !creatingProject;
                }
                if(selected != -1) {
                    if(ImGui.menuItem("Delete Selected")) {
                        Project.removeIndex(selected);
                        selected = -1;
                    }
                }
                ImGui.endMenu();
            }
            ImGui.endMenuBar();
        }
        if(creatingProject) {
            ImGui.text("New Project");
            ImGui.text("Name:");
            ImGui.inputText("##Name", name);
            if(ImGui.button("Cancel")) {
                creatingProject = false;
            }
            ImGui.sameLine();
            if(ImGui.button("Create")) {
                if(name.get().length() == 0) return;
                Project.create(name.get());
                name.clear();
                creatingProject = false;
            }
            ImGui.separator();
        }

        int index = 0;
        for(Project proj : Project.getAll()) {
            if(ImGui.selectable(proj.name, selected == index)) {
                selected = (selected == index) ? -1 : index;
            }
            index++;
        }
    }
    
}