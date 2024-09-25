package closerange.portfolio.util;

import java.io.File;
import java.util.ArrayList;

public class Loader {
    
    private static String path = "D:\\Programs\\github\\Portfolio-Maker\\testSite";
    private static ArrayList<String> textureNames = null;
    private static ArrayList<Texture> textures = null;
    public static void loadSite(String path) {
        Loader.path = path;
    }
    public static String getProjectPath() {
        return Loader.path;
    }
    public static void makeDirty() {
        textureNames = null;
    }
    private static void verifyTextureNames() {
        if(textureNames == null) {
            textureNames = new ArrayList<>();
            String loc = Loader.getProjectPath() + Library.TEXTURE_PATH;
            File folder = new File(loc);
            File[] files = folder.listFiles();
            for(File file : files) {
                if(file.getName().endsWith(".png")) {
                    textureNames.add(file.getName());
                }
            }
        }
    }
    public static ArrayList<Texture> getAllTexture() {
        verifyTextureNames();
        if(textures == null) {
            textures = new ArrayList<>();
            for(String name : textureNames) {
                textures.add(Library.load(name));
            }
        }
        return textures;
    }
    public static String getName(Texture texture) {
        return texture.toString();
    }
}
