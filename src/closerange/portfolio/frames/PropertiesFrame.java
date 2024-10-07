package closerange.portfolio.frames;

import java.util.ArrayList;

import closerange.display.GuiFrame;
import closerange.portfolio.projects.Project;
import closerange.portfolio.util.Library;
import closerange.portfolio.util.Loader;
import closerange.portfolio.util.Texture;
import imgui.ImGui;
import imgui.type.ImString;

public class PropertiesFrame extends GuiFrame {
    public PropertiesFrame() {
        super("Properties");
    }

    private Project selected = null;
    private ImString iName = new ImString(128);
    private ImString iDescription = new ImString(256);
    private ImString iBigDescription = new ImString(256 * 3);
    private ImString iTechDescription = new ImString(256 * 3);
    private ImString iDevProcessDescription = new ImString(256 * 3);
    private ImString iUsageDescription = new ImString(256 * 3);
    private ImString iTimeline = new ImString(128);
    private ImString iTeam = new ImString(128);
    private ImString iRole = new ImString(128);
    private ImString iLink = new ImString(128);
    private boolean isGithub = false;
    private ArrayList<Texture> images = new ArrayList<>();
    private Texture mainImage = null;
    private ArrayList<String> technologies = new ArrayList<>();
    private ArrayList<String> collaborators = new ArrayList<>();
    private ArrayList<String> codeSamples = new ArrayList<>();
    private ImString iCodeSample = new ImString(256 * 3);
    protected static Integer currentCodeSample = -1;

    @Override
    public void onGui() {
        MainMenuBar.onGui();
        if (!Loader.loaded())
            return;
        PropertiesRender.applyPaddingY();
        if (selected != ProjectsFrame.getSelected()) {
            selected = ProjectsFrame.getSelected();
            if (selected != null)
                updateSelection(selected);
        }
        if (selected == null) {
            PropertiesRender.centerText("No project selected");
            return;
        }

        // === DETAILS ===
        PropertiesRender.label("Name");
        ImGui.inputText("##Name", iName);
        PropertiesRender.label("Description");
        ImGui.inputTextMultiline("##Description", iDescription, 0, 75);
        PropertiesRender.label("Ful Description");
        ImGui.inputTextMultiline("##FullDescription", iBigDescription, 0, 150);
        PropertiesRender.label("Tech Description");
        ImGui.inputTextMultiline("##TechDescription", iTechDescription, 0, 150);
        PropertiesRender.label("Dev Process Description");
        ImGui.inputTextMultiline("##DevProcessDescription", iDevProcessDescription, 0, 150);
        PropertiesRender.label("Usage Description");
        ImGui.inputTextMultiline("##UsageDescription", iUsageDescription, 0, 150);
        PropertiesRender.label("Timeline");
        ImGui.inputText("##Timeline", iTimeline);
        PropertiesRender.label("Team");
        ImGui.inputText("##Team", iTeam);
        PropertiesRender.label("Role");
        ImGui.inputText("##Role", iRole);
        PropertiesRender.label("Link");
        ImGui.inputText("##Link", iLink);
        PropertiesRender.label("Is Github Link");
        if (ImGui.checkbox("##Github", isGithub))
            isGithub = !isGithub;
        PropertiesRender.label("Images");
        PropertiesRender.renderImageList(images);
        PropertiesRender.label("Main Image");
        mainImage = PropertiesRender.viewTexture(mainImage, true);
        PropertiesRender.label("Technologies");
        technologies = PropertiesRender.viewTechnologies(technologies);
        PropertiesRender.label("Collaborators");
        collaborators = PropertiesRender.viewCollaborators(collaborators);
        PropertiesRender.label("Code Samples");
        codeSamples = PropertiesRender.viewCodeSamples(codeSamples, iCodeSample);

        // == BUTTONS ==
        if (PropertiesRender.button("Save")) {
            save();
        }
        if (PropertiesRender.button("Delete")) {
            Project.removeIndex(Project.getAll().indexOf(selected));
            ProjectsFrame.setSelected(-1);
        }

    }

    private void updateSelection(Project project) {
        if (project == null)
            return;
        iName.set(project.name);
        iDescription.set(project.description == null ? "" : project.description);
        iBigDescription.set(project.bigDescription == null ? "" : project.bigDescription);
        iTechDescription.set(project.techDescription == null ? "" : project.techDescription);
        iDevProcessDescription.set(project.devProcessDescription == null ? "" : project.devProcessDescription);
        iUsageDescription.set(project.usageDescription == null ? "" : project.usageDescription);
        iTimeline.set(project.timeline);
        iTeam.set(project.team);
        iRole.set(project.role);
        iLink.set(project.link);
        isGithub = project.isGithub;
        images = new ArrayList<>();
        for (String img : project.images) {
            Texture tex = Library.load(img);
            if (tex != null)
                images.add(tex);
        }
        mainImage = Library.load(project.mainImage);
        technologies = new ArrayList<>();
        for (String tech : project.technologies) {
            technologies.add(tech);
        }
        collaborators = new ArrayList<>();
        for (String collab : project.collaborators) {
            collaborators.add(collab);
        }
        codeSamples = new ArrayList<>();
        for (String code : project.codeSamples) {
            codeSamples.add(code);
        }
        currentCodeSample = codeSamples.size() - 1;
        if (currentCodeSample > 0 && codeSamples.get(currentCodeSample) != null)
            iCodeSample.set(codeSamples.get(currentCodeSample));

    }

    private void save() {
        if (selected == null)
            return;
        selected.name = iName.get();
        selected.description = iDescription.get();
        selected.bigDescription = iBigDescription.get();
        selected.techDescription = iTechDescription.get();
        selected.devProcessDescription = iDevProcessDescription.get();
        selected.usageDescription = iUsageDescription.get();
        selected.timeline = iTimeline.get();
        selected.team = iTeam.get();
        selected.role = iRole.get();
        selected.link = iLink.get();
        selected.isGithub = isGithub;
        String[] img = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            img[i] = images.get(i).toString();
        }
        selected.images = img;
        selected.mainImage = mainImage == null ? "" : mainImage.toString();
        selected.technologies = technologies.toArray(new String[technologies.size()]);
        selected.collaborators = collaborators.toArray(new String[collaborators.size()]);
        selected.codeSamples = codeSamples.toArray(new String[codeSamples.size()]);

        Project.save();
    }

}