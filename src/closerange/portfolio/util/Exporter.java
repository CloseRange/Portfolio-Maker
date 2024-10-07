package closerange.portfolio.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;

import closerange.portfolio.projects.Collaborator;
import closerange.portfolio.projects.Project;
import closerange.portfolio.projects.Technology;
import closerange.portfolio.projects.Website;

public class Exporter {
    public static void exportSite(String path, String name) {
        if (!Loader.loaded())
            return;
        Website site = Website.getSite();
        if (site == null)
            return;
        String fullPath = path + "/" + name;
        // Create Directory
        File file = new File(fullPath);
        if (!file.exists())
            file.mkdirs();
        // Copy main files
        copyFilesInFolder("res/content/", "assets", fullPath);
        copyFilesInFolder("res/content/", "images", fullPath);
        copyFilesInFolder(Loader.getProjectPath() + "/textures", "", fullPath + "/images");
        ArrayList<Project> projects = Project.getAll();

        String mainFile = getFileContent("index.html");
        mainFile = doMainReplacment(mainFile, name);
        mainFile = doProjectListReplacement(mainFile, projects);
        saveFileContent(fullPath, "index.html", mainFile);

        String landing = getFileContent("landing.html");
        int index = 0;
        for (Project project : projects) {
            String newFile = doProjectReplacement(landing, project.name, project, index++);
            saveFileContent(fullPath, project.getHtmlName(), newFile);
        }

    }

    private static void saveFileContent(String path, String name, String content) {
        try {
            Files.write(Paths.get(path + "/" + name), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileContent(String name) {
        Path path = Paths.get("res/content/" + name);
        try {
            List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            for (String line : allLines) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void copyFilesInFolder(String loc, String folder, String dest) {
        if (!new File(dest + "/" + folder).exists())
            new File(dest + "/" + folder).mkdirs();
        File folderFile = new File(loc + folder);
        File[] files = folderFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                copyFilesInFolder(loc, folder + "/" + file.getName(), dest);
            } else {
                copyFile(loc, folder + "/" + file.getName(), dest);
            }
        }
    }

    private static void copyFile(String loc, String source, String dest) {
        if (!new File(dest + "/" + source).exists())
            new File(dest + "/" + source).mkdirs();
        Path sourceFile = Path.of(loc + source);
        Path destinationFile = Path.of(dest + "/" + source);

        try {
            Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String cleanString(String content) {
        content = content.replaceAll("\n", " ").replaceAll("\r", "");
        content = content.replaceAll(" +", " ");
        return content;
    }

    private static String doMainReplacment(String original, String title) {
        String next = original;
        next = next.replaceAll("#TITLE#", title);
        next = next.replaceAll("#NAME#", Website.getSite().name);
        next = next.replaceAll("#SUB_NAME#", Website.getSite().subName);
        next = next.replaceAll("#DESCRIPTION#", cleanString(Website.getSite().description));
        next = next.replaceAll("#WEBSITE_LINK#", Website.getSite().websiteUrl);
        next = next.replaceAll("#EMAIL#", Website.getSite().email);
        next = next.replaceAll("#PHONE#", Website.getSite().phone);
        next = next.replaceAll("#ADDRESS#", Website.getSite().address);
        return next;
    }

    private static String doProjectListReplacement(String original, ArrayList<Project> projects) {
        String next = original;
        String projectContent = getFileContent("PROJECT.html");
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Project project : projects) {
            String newContent = doProjectReplacement(projectContent, "", project, i++);
            builder.append(newContent);
            builder.append("\n");
        }
        next = next.replaceAll("#WEB_PROJECTS#", builder.toString());
        return next;
    }

    private static String doProjectReplacement(String original, String title, Project project, int index) {
        String next = doMainReplacment(original, title);
        next = next.replaceAll("#PROJECT_NAME#", project.name);
        next = next.replaceAll("#PROJECT_DESC#", cleanString(project.description));
        next = next.replaceAll("#PROJECT_LINK#", cleanString(project.getHtmlName()));
        next = next.replaceAll("#PROJECT_URL#", cleanString(project.link));
        next = next.replaceAll("#FULL_DESC#", cleanString(project.bigDescription));
        next = next.replaceAll("#TECHNOLOGIES#", cleanString(project.techDescription));
        next = next.replaceAll("#DEV_PROCESS#", cleanString(project.devProcessDescription));
        next = next.replaceAll("#USAGE#", cleanString(project.usageDescription));
        // images
        next = next.replaceAll("#PROJECT_IMAGE#", "images/" + project.mainImage);
        if (project.images.length > 0)
            next = next.replaceAll("#TECHNOLOGIES_IMG#", "images/" + project.images[0]);
        if (project.images.length > 1)
            next = next.replaceAll("#DEV_PROCESS_IMG#", "images/" + project.images[1]);
        if (project.images.length > 2)
            next = next.replaceAll("#USAGE_IMG#", "images/" + project.images[2]);
        next = next.replaceAll("#IMAGES#", getImageList(project.images, 3));

        // lists
        next = next.replaceAll("#TECHNOLOGIES_LOGOS#", getTechList(Technology.getAll()));
        next = next.replaceAll("#TIMELINE#", project.timeline);
        next = next.replaceAll("#TIMELINE_TITLE#", project.timeline.isBlank() ? "" : "Timeline");
        next = next.replaceAll("#COLLABORATORS#", getCollabList(Collaborator.getAll()));
        next = next.replaceAll("#COLLABORATORS_TITLE#", Collaborator.getAll().size() == 0 ? "" : "Collaborators");
        next = next.replaceAll("#TEAM#", project.team);
        next = next.replaceAll("#TEAM_TITLE#", project.team.isBlank() ? "" : "Technologies");
        next = next.replaceAll("#ROLE#", project.role);
        next = next.replaceAll("#ROLE_TITLE#", project.role.isBlank() ? "" : "Role");

        String url_box = getFileContent("URL.html");
        url_box = url_box.replaceAll("#PROJECT_URL#", project.link);
        url_box = url_box.replaceAll("#IS_GIT#", project.isGithub ? "Visit Github" : "Visit Page");
        if (project.link.isBlank())
            next = next.replaceAll("#PROJECT_URL_CONTENT#", "");
        else
            next = next.replaceAll("#PROJECT_URL_CONTENT#", url_box);
        next = next.replaceAll("#STYLE_CLASS#", "style" + (index % 6 + 1));
        return next;
    }

    private static String getImageList(String[] images, int offset) {
        String content = getFileContent("IMAGES.html");
        StringBuilder builder = new StringBuilder();
        for (String image : images) {
            if (offset-- > 0)
                continue;
            String next = content.replaceAll("#NAME#", "images/" + image);
            builder.append(next);
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String getCollabList(ArrayList<Collaborator> collaborators) {
        String content = getFileContent("LOGO.html");
        StringBuilder builder = new StringBuilder();
        for (Collaborator collab : collaborators) {
            String next = content.replaceAll("#NAME#", collab.name);
            next = next.replaceAll("#IMAGE#", "images/" + collab.icon);
            next = next.replaceAll("#ONCLICK#", "window.open('" + collab.gitLink + "')");
            builder.append(next);
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String getTechList(ArrayList<Technology> techs) {
        String content = getFileContent("LOGO.html");
        StringBuilder builder = new StringBuilder();
        for (Technology tech : techs) {
            String next = content.replaceAll("#NAME#", tech.name);
            next = next.replaceAll("#IMAGE#", "images/" + tech.icon);
            next = next.replaceAll("#ONCLICK#", "");
            builder.append(next);
            builder.append("\n");
        }
        return builder.toString();
    }

}
