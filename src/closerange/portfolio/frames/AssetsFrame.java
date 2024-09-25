package closerange.portfolio.frames;

import java.util.ArrayList;

import org.joml.Vector2f;

import closerange.display.GuiFrame;
import closerange.display.Keyboard;
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

public class AssetsFrame extends GuiFrame {

    private static final float size = 150f;

    public AssetsFrame() {
        super("Assets");
    }

    private static Texture selected = null;
    @Override
    public void onGui() {
        ArrayList<Texture> textures = Loader.getAllTexture();
        
        if(textures.size() == 0) {
            ImGui.text("No files found");
            return;
        }
        
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;

        if(Keyboard.Key.ESCAPE.isPressed()) {
            selected = null;
        }

        
        int index = 0;
        for(Texture tex : textures) {
            String name = tex.toString();
            
            if(renderBar(tex, index++, name)) {
                selected = tex;
            }
            
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + size;
            if(index < textures.size() && nextButtonX2 < windowX2)
                ImGui.sameLine();
        }
    }
    

    private boolean renderBar(Texture texture, int id, String text) {
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

        ImGui.beginChild("Child" + id, w, h, true, ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse );
        act = renderTexture(texture, size);

        ImGui.pushStyleColor(ImGuiCol.Border, 0f, 0f, 0.f, 1.0f);
        if(ImGui.beginDragDropSource()) {
            ImGui.setDragDropPayload("TEXTURE", texture);
            Viewer.viewTexture(texture);
            ImGui.endDragDropSource();
        } else {
            if(ImGui.isWindowHovered()) {
                Viewer.viewTooltip(texture);
                // if(Mouse.Button.RIGHT.isReleased())
                //     OptionsRenderer.toggleExplorerPopup(id);
            }
        }
        ImGui.popStyleColor();
        
        float textSize = textW + padX * 2.0f;
        float avail = ImGui.getContentRegionAvailX();

        float dx = ImGui.getCursorPosX();
        float dy = ImGui.getCursorPosY();
        ImGui.setCursorPosY(dy -textH-padY*2);
        Texture point = Library.loadSystemTexture("point.png");
        ImGui.image(point.getTextureId(), avail, textH, 0, 0, 1, 1, 0, 0, 0, .75f);

        float off = (avail - textSize) * .5f;
        if (off > 0.0f)
            ImGui.setCursorPosX(dx + off);
        ImGui.setCursorPosY(dy -textH-padY*2);
        ImGui.text(text);

        ImGui.endChild();
        

        ImGui.popStyleColor(4);
        ImGui.popStyleVar();
        
        
        return act;
    }

    
    public static boolean renderTexture(Texture texture, float size) {
        if(texture == null) {
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
        return ImGui.imageButton(Library.loadSystemTexture("point.png").getTextureId(), size, size, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0);
    }
}
