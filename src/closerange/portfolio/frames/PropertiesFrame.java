package closerange.portfolio.frames;

import java.util.ArrayList;
import java.awt.Color;

import closerange.display.GuiFrame;
import closerange.display.Mouse;
import closerange.portfolio.projects.Project;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;

public class PropertiesFrame extends GuiFrame {
    private static float MARGIN = 300f;
    private static final float PADDING = 20f;
    public PropertiesFrame() {
        super("Properties");
    }
    
    private static void centerText(String text) {
        ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize(text).x) / 2);
        ImGui.text(text);
    }

    private static void applyPadding() {
        ImGui.setCursorPosX(ImGui.getCursorPosX() + PADDING);
    }
    private static void label(String text) {
        applyPadding();
        ImGui.text(text);
        ImGui.sameLine(MARGIN);
    }

    private Project selected = null;
    private ImString iName = new ImString(128);
    private ImString iDescription = new ImString(256);
    private ArrayList<Texture> images = new ArrayList<>();
    @Override
    public void onGui() {
        MARGIN = ImGui.getContentRegionAvailX() / 2f;
        ImGui.setCursorPosY(ImGui.getCursorPosY() + PADDING);
        if(selected != ProjectsFrame.getSelected()) {
            selected = ProjectsFrame.getSelected();
            if(selected != null) updateSelection(selected);
        }
        if(selected == null) {
            centerText("No project selected");
            return;
        }

        // === DETAILS ===
        label("Name");
        ImGui.inputText("##Name", iName);
        label("Description");
        ImGui.inputTextMultiline("##Description", iDescription, 0, 100);
        label("Images");
        renderImageList();



        // == BUTTONS ==
        if(button("Save")) {
            save();
        }
        if(button("Delete")) {
            Project.removeIndex(Project.getAll().indexOf(selected));
            ProjectsFrame.setSelected(-1);
        }
        
    }
    private boolean cornerButton(String text, Color color, boolean enabled) {
        if(!enabled) color = new Color(.3f, .3f, .3f, 1f);
        pushButtonColors(color, !enabled);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float btnHeight = ImGui.getTextLineHeight() + 5;
        float btnWidth = ImGui.calcTextSize(text).x + 25;
        ImGui.setCursorPosX(ImGui.getWindowWidth() - btnWidth - 10);
        boolean hit = ImGui.button(text, btnWidth, btnHeight);
        ImGui.popStyleColor(3);
        ImGui.popStyleVar();
        return hit && enabled;
    }
    private void renderImageList() {
        float size = 100f;
        if(ImGui.beginChild("##Child", ImGui.getContentRegionAvailX(), size * 2, true)) {
            ImGui.text("Images (" + images.size() + ")");
            ImGui.sameLine();
            if(cornerButton("Remove", new Color(.8f, .1f, .15f, 1f), AssetsFrame.getSelected() != null)) {
                images.remove(AssetsFrame.getSelected());
            }
            AssetsFrame.renderListOfImages(images, size * .75f, false);

            ImGui.endChild();
            if(ImGui.beginDragDropTarget()) {
                ImGui.acceptDragDropPayload("TEXTURE", Texture.class);
                Texture texture = ImGui.getDragDropPayload();
                if(texture != null && Mouse.isReleased(Mouse.Button.LEFT)) {
                    if(!images.contains(texture))
                        images.add(texture);
                }
            }
        }
    }
    private static void pushButtonColors(Color color, boolean force) {
        Color brighter = force ? color : color.brighter();
        ImGui.pushStyleColor(ImGuiCol.Button,
            color.getRed()/255f,
            color.getGreen()/255f,
            color.getBlue()/255f,
            color.getAlpha()/255f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered,
            brighter.getRed()/255f,
            brighter.getGreen()/255f,
            brighter.getBlue()/255f,
            brighter.getAlpha()/255f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive,
            color.getRed()/255f,
            color.getGreen()/255f,
            color.getBlue()/255f,
            color.getAlpha()/255f);
    }
    private String cleanupString(String value) {
        value = value.replaceAll("\n", " "); // remove new lines
        value = value.replaceAll(" +", " "); // remove double spaces
        return value;
    }
    private boolean button(String text) {
        ImGui.setCursorPosX(MARGIN);
        return ImGui.button(text, ImGui.getContentRegionAvailX() / 2, 0);
    }

    private void updateSelection(Project project) {
        if(project == null) return;
        iName.set(project.name);
        iDescription.set(project.description == null ? "" : project.description);
        images = new ArrayList<>();
        for(String img : project.images) {
            Texture tex = Library.load(img);
            if(tex != null) images.add(tex);
        }
    }
    private void save() {
        if(selected == null) return;
        selected.name = iName.get();
        selected.description = cleanupString(iDescription.get());
        String[] img = new String[images.size()];
        for(int i = 0; i < images.size(); i++) {
            img[i] = images.get(i).toString();
        }
        selected.images = img;

        Project.save();
    }
    
}