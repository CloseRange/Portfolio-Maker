package closerange.portfolio.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import closerange.portfolio.debug.Debug;
import closerange.portfolio.projects.*;

public class Loader {
    public static boolean isLoaded = false;
    private static String path = null;
    public static final String NAMES_LOCATION = "/redirect_names.json";
    public static final String META_LOCATION = "res/meta.json";
    private static ArrayList<String> textureNames = null;
    private static ArrayList<Texture> textures = null;
    private static HashMap<String, String> textureRealNames = new HashMap<>();
    private static HashMap<String, String> meta = new HashMap<>();

    public static void loadSite() {
        loadMeta();
        String path = meta.get("lastWebsite");
        if (path == null || !(new File(path)).exists()) {
            Debug.error("No last website found");
            return;
        }
        loadSite(path);
    }

    public static void loadSite(String path) {
        textures = null;
        textureNames = null;
        textureRealNames = new HashMap<>();
        Collaborator.clear();
        Project.clear();
        Technology.clear();
        Website.clear();

        Loader.path = path;
        System.out.println("Loading site: " + path);

        loadNames();
        isLoaded = true;

        meta.put("lastWebsite", path);
        saveMeta();
    }

    public static boolean loaded() {
        return isLoaded;
    }

    public static void linkTextureName(String name, String realName) {
        textureRealNames.put(name, realName);
        saveNames();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    private static void loadNames() {
        Gson gson = getGson();
        try {
            String inFile = new String(Files.readAllBytes(
                    Paths.get(path + NAMES_LOCATION)));
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            textureRealNames = gson.fromJson(inFile, type);
        } catch (Exception e) {
            // e.printStackTrace();
            Debug.error("Error loading linker file: " + e.getMessage());
        }
    }

    private static void saveNames() {
        if (!isLoaded)
            return;
        Gson gson = getGson();
        String json = gson.toJson(textureRealNames);
        try {
            File linkerFile = new File(path + NAMES_LOCATION);
            if (!linkerFile.exists())
                (new File(path)).mkdirs();
            FileWriter writer = new FileWriter(path + NAMES_LOCATION);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Debug.error("Error saving linker file: " + e.getMessage());
        }
    }

    private static void loadMeta() {
        Gson gson = getGson();
        try {
            String inFile = new String(Files.readAllBytes(Paths.get(META_LOCATION)));
            Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();
            meta = gson.fromJson(inFile, type);
        } catch (Exception e) {
            // e.printStackTrace();
            Debug.error("Error loading meta file: " + e.getMessage());
        }
    }

    private static void saveMeta() {
        if (!isLoaded)
            return;
        Gson gson = getGson();
        String json = gson.toJson(meta);
        try {
            File linkerFile = new File(META_LOCATION);
            if (!linkerFile.exists())
                (new File(path)).mkdirs();
            FileWriter writer = new FileWriter(META_LOCATION);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Debug.error("Error saving meta file: " + e.getMessage());
        }
    }

    public static String getProjectPath() {
        return Loader.path;
    }

    public static void makeDirty() {
        textureNames = null;
        textures = null;
    }

    private static void verifyTextureNames() {
        if (textureNames == null) {
            textureNames = new ArrayList<>();
            String loc = Loader.getProjectPath() + Library.TEXTURE_PATH;
            File folder = new File(loc);
            File[] files = folder.listFiles();
            if (files == null)
                return;
            for (File file : files) {
                if (file.getName().endsWith(".png")) {
                    textureNames.add(file.getName());
                }
            }
        }
    }

    public static ArrayList<Texture> getAllTexture() {
        verifyTextureNames();
        if (textures == null) {
            textures = new ArrayList<>();
            for (String name : textureNames) {
                textures.add(Library.load(name));
            }
        }
        return textures;
    }

    public static String getName(Texture texture) {
        return getName(texture.toString());
    }

    public static String getName(String name) {
        return textureRealNames.getOrDefault(name, name);
    }
}
