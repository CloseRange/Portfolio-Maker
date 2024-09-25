package closerange.portfolio.projects;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import closerange.portfolio.debug.Debug;
import closerange.portfolio.util.Loader;

public class Collaborator {
    
    public String name = "";
    public String icon = "";
    public String gitLink = "";
    public String guid = "";

    public Collaborator() {
        guid = java.util.UUID.randomUUID().toString();
    }

    private static ArrayList<Collaborator> allCollabs = null;

    public static ArrayList<Collaborator> getAll() {
        if(allCollabs == null) {
            Gson gson = Loader.getGson();
            try {
                String path = Loader.getProjectPath() + "/collaborators.json";
                allCollabs = gson.fromJson(new String(Files.readAllBytes(Paths.get(path))), new TypeToken<ArrayList<Collaborator>>(){}.getType());
            } catch(Exception e) {
                allCollabs = new ArrayList<>();
            }
        }
        return allCollabs;
    }
    public static ArrayList<Collaborator> getAllSorted() {
        ArrayList<Collaborator> collabs = getAll();
        collabs.sort((a, b) -> a.name.compareTo(b.name));
        return collabs;
    }
    public static Collaborator get(String guid) {
        for(Collaborator collab : getAll()) {
            if(collab.guid.equals(guid)) return collab;
        }
        return null;
    }
    public static Collaborator create(String name, String link, String icon) {
        Collaborator collab = new Collaborator();
        collab.name = name;
        collab.icon = icon;
        collab.gitLink = link;
        getAll().add(collab);
        save();
        return collab;
    }
    public static void save() {
        Gson gson = Loader.getGson();
        String json = gson.toJson(allCollabs);
        try {
            Files.write(Paths.get(Loader.getProjectPath() + "/collaborators.json"), json.getBytes());
        } catch(Exception e) {
            Debug.error("Error saving collaborators file: " + e.getMessage());
        }
    }
}
