package closerange.portfolio.filepicker;

import java.awt.Color;
import java.io.File;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class FilePicker {
    private static boolean shouldOpen = false;
    private static boolean fileIsReady = false;
    private static String file = "";
    private static String location;

    private static String fileDesc;
    private static ImString iFile = new ImString(128);
    private static String currentLocation = "";

    public static void onGui() {
        if(shouldOpen) {
            ImGui.openPopup("File Picker##FILE_PICKER");
            shouldOpen = false;
        }
        ImVec2 center = ImGui.getMainViewport().getCenter();
        ImGui.setNextWindowPos(center.x, center.y, ImGuiCond.Appearing, 0.5f, 0.5f);
        ImVec2 size = ImGui.getMainViewport().getSize();
        float width = size.x * 4/8;
        float height = size.y * 3/8;
        ImGui.setNextWindowSize(width, 0);

        if (ImGui.beginPopupModal("File Picker##FILE_PICKER", ImGuiWindowFlags.AlwaysAutoResize)) {
            String[] fileParts = currentLocation.replaceAll("\\\\", "/").split("/");
            for(int i=0; i<fileParts.length; i++) {
                if(button(fileParts[i] + "##" + i, false)) {
                    if(i != fileParts.length - 1) {
                        currentLocation = currentLocation.substring(0, currentLocation.indexOf(fileParts[i]) + fileParts[i].length());
                    }
                }
                if(i != fileParts.length - 1) ImGui.sameLine();
            }
            ImGui.beginChild("##FILE_PICKER_CHILD", 0, height, true);

            int index = 0;
            for(File f : new File(currentLocation).listFiles()) {
                if(f.isDirectory()) {
                    int c = index++ % 2 == 0 ? 30 : 50;
                    ImGui.pushStyleColor(ImGuiCol.ChildBg, c, c, c, 255);
                    ImGui.beginChild("##" + f.getName(), 0, ImGui.getTextLineHeight());
                    ImGui.setCursorPosX(ImGui.getCursorPosX() + 15);
                    if(ImGui.selectable(f.getName())) {
                        currentLocation = f.getAbsolutePath();
                    }
                    ImGui.endChild();
                    ImGui.popStyleColor();
                }
            }

            ImGui.endChild();

            if(fileDesc != null) {
                ImGui.pushStyleColor(ImGuiCol.Button, 0f, 0f, 0.f, 1.0f);
                ImGui.separator();
                ImGui.text(fileDesc);
                ImGui.sameLine();
                ImGui.inputText("##FILE_PICKER", iFile);
                ImGui.popStyleColor();
            }

            if (button("OK")) {
                file = iFile.get();
                fileIsReady = true;
                location = currentLocation;
                ImGui.closeCurrentPopup();
            }
            ImGui.setItemDefaultFocus();
            ImGui.sameLine();
            if (button("Cancel")) { ImGui.closeCurrentPopup(); }
            ImGui.endPopup();
        }
    }
    private static boolean button(String text) {
        return button(text, true);
    }
    private static boolean button(String text, boolean forceSize) {
        Color color = new Color(153, 72, 14);
        Color brighter = color.brighter();
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 7.0f);
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
        boolean r;
        if(forceSize) r = ImGui.button(text, 120, 0);
        else r = ImGui.button(text);
        ImGui.popStyleColor(3);
        ImGui.popStyleVar();
        return r;
    }

    public static void pickFile(String fileDesc) {
        pickFile(fileDesc, System.getProperty("user.dir"));
    }
    public static void pickFile(String fileDesc, String location) {
        shouldOpen = true;
        fileIsReady = false;
        file = "";
        currentLocation = location;
        iFile.clear();
        FilePicker.fileDesc = fileDesc;
    }
    public static boolean hasFile() {
        boolean hasF = fileIsReady;
        fileIsReady = false;
        return hasF;
    }
    public static String grabFile() {
        String f = file;
        file = "";
        return f;
    }
    public static String grabLocation() {
        String l = location;
        location = "";
        return l;
    }
}
