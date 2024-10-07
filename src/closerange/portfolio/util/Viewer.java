package closerange.portfolio.util;

import imgui.ImGui;

public class Viewer {
    public static final float WIDTH = 250f;

    public static void viewTexture(Texture texture) {
        centerText(Loader.getName(texture), WIDTH);
        if (texture == null)
            return;
        renderTexture(texture, WIDTH);
        detail("Width", "" + texture.getWidth(), WIDTH);
        detail("Height", "" + texture.getHeight(), WIDTH);
    }

    public static void viewTooltip(Texture texture) {
        if (texture == null)
            return;
        ImGui.setNextWindowSize(WIDTH, 0);
        ImGui.beginTooltip();
        centerText(Loader.getName(texture), WIDTH);
        ImGui.endTooltip();
    }

    private static void renderTexture(Texture tex, float width) {
        if (tex != null) {
            float scx = width / tex.getWidth();
            float scy = width / tex.getHeight();
            float sc = Math.min(scx, scy);
            ImGui.setCursorPosX((width - tex.getWidth() * sc) / 2);
            ImGui.image(tex.getTextureId(), tex.getWidth() * sc, tex.getHeight() * sc);
        }
    }

    private static void detail(String name, String value, float width) {
        if (value == null)
            return;
        ImGui.text(name);
        var w = ImGui.calcTextSize(value).x;
        ImGui.sameLine(width - w);
        ImGui.text(value);
    }

    private static void centerText(String text, float width) {
        ImGui.setCursorPosX((width - ImGui.calcTextSize(text).x) / 2);
        ImGui.text(text);
    }
}
