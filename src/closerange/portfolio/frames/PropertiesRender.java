package closerange.portfolio.frames;

import java.awt.Color;
import java.util.ArrayList;

import closerange.display.Mouse;
import closerange.portfolio.projects.Collaborator;
import closerange.portfolio.projects.Technology;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Texture;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;

public class PropertiesRender {
    
    protected static final float PADDING = 20f;
    
    protected static float margin() {
        return ImGui.getContentRegionAvailX() / 2f;
    };
    protected static void centerText(String text) {
        ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize(text).x) / 2);
        ImGui.text(text);
    }

    protected static void applyPadding() {
        ImGui.setCursorPosX(ImGui.getCursorPosX() + PADDING);
    }
    protected static void applyPaddingY() {
        ImGui.setCursorPosY(ImGui.getCursorPosY() + PADDING);
    }
    protected static void label(String text) {
        applyPadding();
        ImGui.text(text);
        ImGui.sameLine(margin());
    }
    protected static void label(String text, float widthPercent) {
        applyPadding();
        ImGui.text(text);
        ImGui.sameLine(ImGui.getContentRegionAvailX() * widthPercent);
    }
    
    protected static void pushButtonColors(Color color, boolean force) {
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
    protected static String cleanupString(String value) {
        value = value.replaceAll("\n", " "); // remove new lines
        value = value.replaceAll(" +", " "); // remove double spaces
        return value;
    }
    protected static boolean button(String text) {
        ImGui.setCursorPosX(margin());
        return ImGui.button(text, ImGui.getContentRegionAvailX() / 2, 0);
    }
    public static boolean buttonToggle(String text, Color color, boolean enabled) {
        return buttonToggle(text, color, enabled, 0f);
    }
    public static boolean buttonToggle(String text, Color color, boolean enabled, float width) {
        if(!enabled) color = new Color(.3f, .3f, .3f, 1f);
        pushButtonColors(color, !enabled);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float btnHeight = ImGui.getTextLineHeight() + 5;
        float btnWidth = width > 0 ? width : ImGui.calcTextSize(text).x + 25;
        boolean hit = ImGui.button(text, btnWidth, btnHeight);
        ImGui.popStyleColor(3);
        ImGui.popStyleVar();
        return hit && enabled;
    }
    public static boolean buttonToggleCorner(String text, Color color, boolean enabled) {
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
    protected static void renderImageList(ArrayList<Texture> images) {
        float size = 100f;
        ImGui.beginChild("##Child", ImGui.getContentRegionAvailX(), size * 2, true);
        ImGui.text("Images (" + images.size() + ")");
        ImGui.sameLine();
        if(buttonToggleCorner("Remove", new Color(.8f, .1f, .15f, 1f), AssetsFrame.getSelected() != null)) {
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
            ImGui.endDragDropTarget();
        }
    }
    public static Texture viewTexture(Texture mainImage, boolean showRemove) {
        float size = 150f;
        ImGui.beginChild("##ChildMain", size, size, true);
        if(mainImage == null) {
            ImGui.text("N/A");
        } else {
            float mult = .75f;
            float cent = (size - size * mult) / 2;
            ImGui.setCursorPos(cent, cent);
            ImGui.image(mainImage.getTextureId(), size * mult, size * mult);
        }

        ImGui.endChild();
        if(ImGui.beginDragDropTarget()) {
            ImGui.acceptDragDropPayload("TEXTURE", Texture.class);
            Texture texture = ImGui.getDragDropPayload();
            if(texture != null && Mouse.isReleased(Mouse.Button.LEFT)) {
                mainImage = texture;
            }
            ImGui.endDragDropTarget();
        }
        if(showRemove) {
            ImGui.sameLine();
            if(buttonToggleCorner("Remove", new Color(.8f, .1f, .15f, 1f), mainImage != null))
                return null;
        }
        return mainImage;
    }
    public static void renderInputText(String label, ImString input) {
        label(label, .25f);
        ImGui.inputText("##" + label, input);

    }
    public static ArrayList<String> viewTechnologies(ArrayList<String> techs) {
        float size = 100f;
        ImGui.beginChild("##ChildTech", ImGui.getContentRegionAvailX(), size * 2, true);
        int count = 3;
        int index = 1;
        float width = ImGui.getContentRegionAvailX() / count;
        for(Technology tech : Technology.getAllSorted()) {
            if(ImGui.checkbox("##" + index, techs.contains(tech.guid))) {
                if(techs.contains(tech.guid)) {
                    techs.remove(tech.guid);
                } else {
                    techs.add(tech.guid);
                }
            }
            Texture ico = Library.load(tech.icon);
            if(ico != null) {
                float s = ImGui.getTextLineHeight();
                ImGui.sameLine();
                ImGui.image(ico.getTextureId(), s, s);
            }
            ImGui.sameLine();
            ImGui.text(tech.name);
            ImGui.sameLine(width * (index % count));
            if(index % count == 0) {
                ImGui.newLine();
            }
            index++;
        }
        ImGui.endChild();
        return techs;
    }
    public static ArrayList<String> viewCollaborators(ArrayList<String> techs) {
        float size = 100f;
        ImGui.beginChild("##ChildCollabs", ImGui.getContentRegionAvailX(), size * 2, true);
        int count = 3;
        int index = 1;
        float width = ImGui.getContentRegionAvailX() / count;
        for(Collaborator tech : Collaborator.getAllSorted()) {
            if(ImGui.checkbox("##" + index, techs.contains(tech.guid))) {
                if(techs.contains(tech.guid)) {
                    techs.remove(tech.guid);
                } else {
                    techs.add(tech.guid);
                }
            }
            Texture ico = Library.load(tech.icon);
            if(ico != null) {
                float s = ImGui.getTextLineHeight();
                ImGui.sameLine();
                ImGui.image(ico.getTextureId(), s, s);
            }
            ImGui.sameLine();
            ImGui.text(tech.name);
            ImGui.sameLine(width * (index % count));
            if(index % count == 0) {
                ImGui.newLine();
            }
            index++;
        }
        ImGui.endChild();
        return techs;
    }

    public static ArrayList<String> viewCodeSamples(ArrayList<String> codeSamples, ImString sample) {
        float size = 200f;
        float width = ImGui.getContentRegionAvailX() / 4 - 6;
        int index = PropertiesFrame.currentCodeSample;
        if(buttonToggle("Prev##codeSample", new Color(.1f, .25f, .8f, 1f), index > 0, width)) {
            index--;
            sample.set(codeSamples.get(index));
        }
        ImGui.sameLine();
        if(buttonToggle("Remove##codeSample", new Color(.8f, .1f, .15f, 1f), index != -1, width)) {
            System.out.println("Removing " + index);
            codeSamples.remove((int) index);
            while(index >= codeSamples.size()) {
                index--;
            }
            if(index < 0) sample.clear();
            else sample.set(codeSamples.get(index));
        }
        ImGui.sameLine();
        if(buttonToggle("Add##codeSample", new Color(.2f, .7f, .3f, 1f), true, width)) {
            codeSamples.add("");
            index = codeSamples.size() - 1;
            sample.clear();
        }
        ImGui.sameLine();
        if(buttonToggle("Next##codeSample", new Color(.1f, .25f, .8f, 1f), index != -1 && index < codeSamples.size()-1, width)) {
            index++;
            sample.set(codeSamples.get(index));
        }
        label("");
        ImGui.text("Code Samples (" + (index+1) + " / " + codeSamples.size() + ")");
        if(codeSamples.size() > index && index >= 0) {
            label("");
            ImGui.inputTextMultiline("##CodeSample", sample, 0, size);
            codeSamples.set(index, sample.get());
        }
        PropertiesFrame.currentCodeSample = index;
        return codeSamples;
    }
}
