package closerange.portfolio.util;
import org.joml.Vector2f;

public class PMath {
    
    public static float calcImageClampScale(float currW, float currH, float targW, float targH) {
        float scw = targW / currW;
        float sch = targH / currH;
        float min = Math.min(scw, sch);
        float max = Math.max(scw, sch);
        return min == 0 ? max : min;
    }
    public static Vector2f calcImageClampCenter(float currW, float currH, float targW, float targH, float sc) {
        float w = currW * sc;
        float h = currH * sc;
        
        return new Vector2f((targW-w)/2f, (targH-h)/2f);
    }
}