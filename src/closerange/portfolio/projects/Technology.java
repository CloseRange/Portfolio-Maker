package closerange.portfolio.projects;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import closerange.portfolio.debug.Debug;
import closerange.portfolio.util.Loader;

public class Technology {
    public String name = "";
    public String icon = "";
    public String guid = "";

    public Technology() {
        guid = java.util.UUID.randomUUID().toString();
    }

    private static ArrayList<Technology> allTechs = null;
    public static void clear() { allTechs = null; }

    public static ArrayList<Technology> getAll() {
        if(allTechs == null) {
            Gson gson = Loader.getGson();
            try {
                String path = Loader.getProjectPath() + "/technologies.json";
                allTechs = gson.fromJson(new String(Files.readAllBytes(Paths.get(path))), new TypeToken<ArrayList<Technology>>(){}.getType());
            } catch(Exception e) {
                allTechs = new ArrayList<>();
            }
        }
        return allTechs;
    }
    public static ArrayList<Technology> getAllSorted() {
        ArrayList<Technology> techs = getAll();
        techs.sort((a, b) -> a.name.compareTo(b.name));
        return techs;
    }
    public static Technology get(String guid) {
        for(Technology tech : getAll()) {
            if(tech.guid.equals(guid)) return tech;
        }
        return null;
    }
    public static Technology create(String name, String icon) {
        Technology tech = new Technology();
        tech.name = name;
        tech.icon = icon;
        getAll().add(tech);
        save();
        return tech;
    }
    public static void save() {
        Gson gson = Loader.getGson();
        String json = gson.toJson(allTechs);
        try {
            Files.write(Paths.get(Loader.getProjectPath() + "/technologies.json"), json.getBytes());
        } catch(Exception e) {
            Debug.error("Error saving technologies file: " + e.getMessage());
        }
    }
}


