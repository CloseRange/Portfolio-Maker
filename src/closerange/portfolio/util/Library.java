package closerange.portfolio.util;

import java.io.File;
import java.util.HashMap;

import closerange.portfolio.debug.Debug;

public class Library {
    private HashMap<String, Texture> library = new HashMap<>();

    public Texture load(String name) {
        if (!library.containsKey(name)) {
            String path = SiteManager.getProjectPath() + "textures/" + name;
            if(!(new File(path)).exists()) {
                Debug.error("Texture not found: " + name);
            }
            library.put(name, new Texture().setTexture(name));
        }
        return library.get(name);
    }

    public Texture copyFromFile(String path) {
    }
}


