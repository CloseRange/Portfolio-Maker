package closerange.portfolio.frames;

import closerange.portfolio.filepicker.FilePicker;
import closerange.portfolio.util.Exporter;
import closerange.portfolio.util.Loader;
import imgui.ImGui;

public class MainMenuBar {
    public static float menuHeight = 0;
    private static boolean isNew = false;
    private static boolean isExport = false;

    public static void onGui() {
        if (ImGui.beginMainMenuBar()) {
            menuHeight = ImGui.getWindowSizeY();
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("Open Project")) {
                    FilePicker.pickFile(null);
                    isNew = false;
                }
                if (ImGui.menuItem("New Project")) {
                    FilePicker.pickFile("Project Name");
                    isNew = true;
                }
                if (Loader.isLoaded && ImGui.menuItem("Export Project")) {
                    FilePicker.pickFile("Project Name");
                    isExport = true;
                }
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }
        if (FilePicker.hasFile()) {
            if (!isExport) {
                String path = FilePicker.grabLocation();
                if (isNew)
                    path += "/" + FilePicker.grabFile();
                isNew = false;
                Loader.loadSite(path);
            } else {
                isExport = false;
                Exporter.exportSite(FilePicker.grabLocation(), FilePicker.grabFile());
            }
        }
        FilePicker.onGui();
    }
}
