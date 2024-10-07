package closerange.portfolio.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.UUID;

import closerange.portfolio.debug.Debug;

public class Library {
    private static HashMap<String, Texture> library = new HashMap<>();
    private static HashMap<String, Texture> systemTextures = new HashMap<>();

    public static final String TEXTURE_PATH = "/textures/";

    public static Texture load(String name) {
        if (!library.containsKey(name)) {
            String path = Loader.getProjectPath() + TEXTURE_PATH + name;
            if (!(new File(path)).exists()) {
                Debug.error("Texture not found: " + name);
            }
            library.put(name, new Texture().setTexture(path));
        }
        return library.get(name);
    }

    public static void removeTexture(Texture texture) {
        String name = texture.toString();
        library.remove(name);

        String loc = Loader.getProjectPath() + TEXTURE_PATH;
        File f = new File(loc + name);
        if (f.exists()) {
            f.delete();
            Loader.makeDirty();
        }
    }

    public static Texture copyFileToProject(String path) {
        if (!Loader.isLoaded)
            return null;
        String uuidName = UUID.randomUUID().toString().replaceAll("[^-A-Za-z0-9]", "");
        path = path.replaceAll("\\\\", "/");
        String realName = path
                .substring(path.lastIndexOf("/") + 1) // remove path
                .replace(".png", ""); // remove extension
        realName = realName.replaceAll("_", " ");
        realName = realName.substring(0, 1).toUpperCase() + realName.substring(1);
        // capitalize first letter

        if (!path.endsWith(".png")) {
            Debug.error("Invalid file type: " + path);
            return null;
        }
        String loc = Loader.getProjectPath() + TEXTURE_PATH;
        new File(loc).mkdirs();
        try {
            String name = uuidName + ".png";
            Path src = new File(path).toPath();
            Path dest = new File(loc + "/" + name).toPath();
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            Loader.makeDirty();
            Loader.linkTextureName(name, realName);
            return Library.load(name);
        } catch (Exception e) {
            e.printStackTrace();
            Debug.error("Failed to copy file: " + path);
        }
        return null;
    }

    public static Texture loadSystemTexture(String name) {
        if (!systemTextures.containsKey(name)) {
            String path = "res/" + TEXTURE_PATH + name;
            if (!(new File(path)).exists()) {
                Debug.error("Texture not found: " + name);
            }
            systemTextures.put(name, new Texture().setTexture(path));
        }
        return systemTextures.get(name);

    }
}
