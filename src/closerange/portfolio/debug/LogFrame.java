package closerange.portfolio.debug;


import closerange.display.*;
import imgui.ImGui;

public class LogFrame extends GuiFrame {

    private static final int[] DEFAULT = new int[] { 144, 164, 174 };
    private static final int[] INFO = new int[] { 25, 118, 210 };
    private static final int[] WARNING = new int[] { 249, 268, 37 };
    private static final int[] ERROR =   new int[] { 244, 67, 54 };
    private static final int[] FATAL =   new int[] { 197, 134, 192 };
    private static final int[] SUCCESS =   new int[] { 76, 175, 80 };

    public LogFrame() {
        super("Logger");
    }
    public void onGui() {
        for(Logger.Log log : Logger.logs) {
            int[] col = DEFAULT;
            switch(log.type) {
                case Logger.NONE: col = DEFAULT; break;
                case Logger.INFO: col = INFO; break;
                case Logger.WARNING: col = WARNING; break;
                case Logger.ERROR: col = ERROR; break;
                case Logger.FATAL: col = FATAL; break;
                case Logger.SUCCESS: col = SUCCESS; break;
            }
            String message = log.time + " " + log.message;
            ImGui.textColored(col[0], col[1], col[2], 255, message);
        }
    }
}