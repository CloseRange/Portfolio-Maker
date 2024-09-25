package closerange.portfolio.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import closerange.portfolio.debug.Debug;

public class Library {
    private static HashMap<String, Texture> library = new HashMap<>();
    public static final String TEXTURE_PATH = "/textures/";

    public static Texture load(String name) {
        if (!library.containsKey(name)) {
            String path = Loader.getProjectPath() + TEXTURE_PATH + name;
            if(!(new File(path)).exists()) {
                Debug.error("Texture not found: " + name);
            }
            library.put(name, new Texture().setTexture(path));
        }
        return library.get(name);
    }

    public static Texture copyFileToProject(String path) {
        path = path.replaceAll("\\\\", "/");
        if(!path.endsWith(".png")) {
            Debug.error("Invalid file type: " + path);
            return null;
        }
        String loc = Loader.getProjectPath() + TEXTURE_PATH;
        new File(loc).mkdirs();
        try {
            String name = path.substring(path.lastIndexOf('/') + 1);
            Path src = new File(path).toPath();
            Path dest = new File(loc + "/" + name).toPath();
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            Loader.makeDirty();
            return Library.load(name);
        } catch(Exception e) {
            e.printStackTrace();
            Debug.error("Failed to copy file: " + path);
        }
        return null;
    }
}


