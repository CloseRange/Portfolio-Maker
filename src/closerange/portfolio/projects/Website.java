package closerange.portfolio.projects;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import closerange.portfolio.debug.Debug;
import closerange.portfolio.util.Loader;

public class Website {
    public String name;
    public String subName;
    public String description;
    public String websiteUrl;

    public String email;
    public String phone;
    public String address;

    public static Website currentWebsite = null;

    public static void clear() {
        currentWebsite = null;
    }

    public static Website getSite() {
        if (currentWebsite == null) {
            Gson gson = Loader.getGson();
            try {
                String path = Loader.getProjectPath() + "/properties.json";
                currentWebsite = gson.fromJson(new String(Files.readAllBytes(Paths.get(path))),
                        new TypeToken<Website>() {
                        }.getType());
            } catch (Exception e) {
                currentWebsite = new Website();
            }
        }
        return currentWebsite;
    }

    public static void save() {
        Gson gson = Loader.getGson();
        String json = gson.toJson(getSite());
        try {
            Files.write(Paths.get(Loader.getProjectPath() + "/properties.json"), json.getBytes());
        } catch (Exception e) {
            Debug.error("Error saving website file: " + e.getMessage());
        }
    }
}
