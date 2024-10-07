package closerange.portfolio.frames;

import java.util.ArrayList;

import org.joml.Vector2f;

import closerange.display.GuiFrame;
import closerange.display.Keyboard;
import closerange.display.Mouse;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Loader;
import closerange.portfolio.util.PMath;
import closerange.portfolio.util.Texture;
import closerange.portfolio.util.Viewer;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class AssetsFrame extends GuiFrame {

    private static final float size = 150f;

    public AssetsFrame() {
        super("Assets");
    }

    private static Texture selected = null;
    private static ArrayList<String> folders = new ArrayList<>();
    private static String currentFolder = "";

    @Override
    public void onGui() {
        if (!Loader.loaded())
            return;
        ArrayList<Texture> textures = Loader.getAllTexture();

        if (textures.size() == 0) {
            ImGui.text("No files found");
            return;
        }

        if (Keyboard.Key.ESCAPE.isPressed()) {
            selected = null;
        }

        renderListOfImages(textures, size);
    }

    public static void renderListOfImages(ArrayList<Texture> textures, float size) {
        renderListOfImages(textures, size, true);
    }

    public static void renderListOfImages(ArrayList<Texture> textures, float size, boolean showNames) {

        folders.clear();
        for (Texture tex : textures) {
            if (currentFolder != "")
                continue;
            String folder = Loader.getFolder(tex);
            if (folder.equals(""))
                continue;
            if (!folders.contains(folder))
                folders.add(folder);
        }

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;

        int index = 0;
        if (!currentFolder.equals("")) {
            if (renderBar(Library.loadSystemTexture("folder.png"), index++, "ROOT", size, showNames, "")) {
                currentFolder = "";
            }

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + size;
            if (nextButtonX2 < windowX2)
                ImGui.sameLine();
        }
        for (String folder : folders) {
            if (renderBar(Library.loadSystemTexture("folder.png"), index++, folder, size, showNames, folder)) {
                currentFolder = folder;
                // selected = tex;
            }

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + size;
            if (nextButtonX2 < windowX2)
                ImGui.sameLine();
        }
        int preIndex = index;
        for (Texture tex : textures) {
            String name = Loader.getName(tex);
            if (!Loader.getFolder(tex).equals(currentFolder)) {
                index++;
                continue;
            }

            if (renderBar(tex, index++, name, size, showNames, null)) {
                selected = tex;
            }

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + size;
            if (index - preIndex < textures.size() && nextButtonX2 < windowX2)
                ImGui.sameLine();
        }
    }

    public static Texture getSelected() {
        return selected;
    }

    private static boolean renderBar(Texture texture, int id, String text, float size, boolean showNames,
            String folder) {
        float textW = ImGui.calcTextSize(text).x;
        float textH = ImGui.calcTextSize(text).y;
        float padX = ImGui.getStyle().getFramePaddingX();
        float padY = ImGui.getStyle().getFramePaddingY();

        float w = size + padX * 2;
        float h = size + padY * 2;// + textH;

        boolean act = false;

        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 5f);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.f, 0.f, 0.f, 0.f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.f, 0.f, 0.f, 0.f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.f, 0.f, 0.f, 0.f);
        float s = texture == selected ? 1f : 0f;
        ImGui.pushStyleColor(ImGuiCol.Border, 0f, s, s, 1.0f);

        ImGui.beginChild("Child" + id, w, h, true, ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        act = renderTexture(texture, size);

        ImGui.pushStyleColor(ImGuiCol.Border, 0f, 0f, 0.f, 1.0f);
        if (folder == null) {
            if (ImGui.beginDragDropSource()) {
                ImGui.setDragDropPayload("TEXTURE", texture);
                Viewer.viewTexture(texture);
                ImGui.endDragDropSource();
            } else {
                if (ImGui.isWindowHovered()) {
                    Viewer.viewTooltip(texture);
                    if (Mouse.Button.RIGHT.isReleased())
                        ImGui.openPopup("explorer_popup" + id);
                }
            }
            onExplorerPopup(texture, "explorer_popup" + id);
        } else {
            if (ImGui.beginDragDropTarget()) {
                Texture tex = ImGui.acceptDragDropPayload("TEXTURE", Texture.class);
                if (tex != null) {
                    Loader.linkTextureFolder(tex.toString(), folder);
                }
                ImGui.endDragDropTarget();
            }
        }

        ImGui.popStyleColor();

        float textSize = textW + padX * 2.0f;
        float avail = ImGui.getContentRegionAvailX();

        float dx = ImGui.getCursorPosX();
        float dy = ImGui.getCursorPosY();
        ImGui.setCursorPosY(dy - textH - padY * 2);
        if (showNames) {
            Texture point = Library.loadSystemTexture("point.png");
            ImGui.image(point.getTextureId(), avail, textH, 0, 0, 1, 1, 0, 0, 0, .75f);

            float off = (avail - textSize) * .5f;
            if (off > 0.0f)
                ImGui.setCursorPosX(dx + off);
            ImGui.setCursorPosY(dy - textH - padY * 2);
            ImGui.text(text);
        }

        ImGui.endChild();

        ImGui.popStyleColor(4);
        ImGui.popStyleVar();

        return act;
    }

    public static boolean renderTexture(Texture texture, float size) {
        if (texture == null) {
            return renderEmpty(0, 0, size);
        }
        float dx = ImGui.getCursorPosX();
        float dy = ImGui.getCursorPosY();
        // calculate new scale
        float w = texture.getWidth();
        float h = texture.getHeight();
        float sc = PMath.calcImageClampScale(w, h, size, size);
        // calculate and set offset
        Vector2f offset = PMath.calcImageClampCenter(w, h, size, size, sc);
        ImVec2 curr = ImGui.getCursorPos();
        ImGui.setCursorPos(curr.x + offset.x, curr.y + offset.y);

        ImGui.image(texture.getTextureId(), w * sc, h * sc);
        // renderCorner(dx, dy, size, "texture.png");
        return renderEmpty(dx, dy, size);
    }

    public static boolean renderEmpty(float dx, float dy, float size) {
        ImGui.setCursorPos(dx, dy);
        return ImGui.imageButton(Library.loadSystemTexture("point.png").getTextureId(), size, size, 0, 0, 1, 1, 1, 0, 0,
                0, 0, 1, 1, 1, 0);
    }

    private static ImString newName = new ImString(128);
    private static ImString newFolder = new ImString(128);

    private static void onExplorerPopup(Texture texture, String id) {
        if (ImGui.beginPopup(id)) {
            if (ImGui.beginMenu("Rename")) {
                ImGui.inputText("##newName" + id, newName);
                ImGui.sameLine();
                if (ImGui.menuItem("Accept") || Keyboard.isPressed(Keyboard.Key.ENTER)) {
                    Loader.linkTextureName(texture.toString(), newName.get());
                    newName.set("");
                    ImGui.closeCurrentPopup();
                }
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Change Folder")) {
                ImGui.inputText("##newFolder" + id, newFolder);
                ImGui.sameLine();
                if (ImGui.menuItem("Accept") || Keyboard.isPressed(Keyboard.Key.ENTER)) {
                    Loader.linkTextureFolder(texture.toString(), newFolder.get());
                    newFolder.set("");
                    ImGui.closeCurrentPopup();
                }
                ImGui.endMenu();
            }
            if (ImGui.menuItem("Delete")) {
                Library.removeTexture(texture);
            }
            ImGui.endPopup();
        }

    }
}
