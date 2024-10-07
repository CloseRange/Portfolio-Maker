package closerange.portfolio.projects;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import closerange.portfolio.debug.Debug;
import closerange.portfolio.util.Loader;

public class Project {
    public static final String PROJECTS_LOCATION = "/projects.json";

    public String name;
    public String description;
    public String bigDescription;
    public String timeline = "";
    public String team = "";
    public String role = "";
    public String[] images = new String[0];
    public String mainImage;
    public String link = "";
    public boolean isGithub = false;
    public String techDescription = "";
    public String devProcessDescription = "";
    public String usageDescription = "";
    public String[] technologies = new String[0];
    public String[] collaborators = new String[0];
    public String[] codeSamples = new String[0];

    private static ArrayList<Project> projects = null;

    public static void clear() {
        projects = null;
    }

    public String getHtmlName() {
        return name.toLowerCase().replace(" ", "_") + ".html";
    }

    public static ArrayList<Project> getAll() {
        if (projects == null) {
            Gson gson = Loader.getGson();
            try {
                String path = Loader.getProjectPath() + PROJECTS_LOCATION;
                if (!(new File(path).exists())) {
                    projects = new ArrayList<>();
                    return projects;
                }
                String inFile = new String(Files.readAllBytes(Paths.get(path)));
                projects = gson.fromJson(inFile, new TypeToken<ArrayList<Project>>() {
                }.getType());
            } catch (Exception e) {
                Debug.error("Error loading linker file: " + e.getMessage());
            }
        }
        return projects;
    }

    public static void removeIndex(int i) {
        getAll();
        if (i < projects.size() && i >= 0) {
            projects.remove(i);
            save();
        }

    }

    public static void create(String name) {
        Project proj = new Project();
        proj.name = name;
        projects.add(proj);
        save();
    }

    public static void save() {
        // save
        Gson gson = Loader.getGson();
        String json = gson.toJson(projects);
        try {
            String path = Loader.getProjectPath() + PROJECTS_LOCATION;
            Files.write(Paths.get(path), json.getBytes());
        } catch (Exception e) {
            Debug.error("Error saving linker file: " + e.getMessage());
        }
    }
}
