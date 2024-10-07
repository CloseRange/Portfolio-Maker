package closerange.portfolio.frames;

import closerange.display.GuiFrame;
import closerange.portfolio.projects.Website;
import closerange.portfolio.util.Loader;
import imgui.ImGui;
import imgui.type.ImString;

public class WebsiteFrame extends GuiFrame {

    public WebsiteFrame() {
        super("Website Properties");
    }

    private Website current = null;
    private ImString iName = new ImString(128);
    private ImString iSubName = new ImString(128);
    private ImString iDescription = new ImString(256);
    private ImString iUrl = new ImString(128);
    private ImString iEmail = new ImString(128);
    private ImString iPhone = new ImString(128);
    private ImString iAddress = new ImString(128);

    @Override
    public void onGui() {
        Website site = Website.getSite();
        if (current != site)
            updateSelection(site);
        if (site == null || !Loader.loaded()) {
            return;
        }
        PropertiesRender.label("Your Name");
        ImGui.inputText("##Name", iName);
        PropertiesRender.label("Your Title");
        ImGui.inputText("##Title", iSubName);
        PropertiesRender.label("About You");
        ImGui.inputTextMultiline("##Description", iDescription, 0, 100);
        PropertiesRender.label("Website Url");
        ImGui.inputText("##Url", iUrl);
        PropertiesRender.label("Email");
        ImGui.inputText("##Email", iEmail);
        PropertiesRender.label("Phone");
        ImGui.inputText("##Phone", iPhone);
        PropertiesRender.label("Address");
        ImGui.inputText("##Address", iAddress);

        if (PropertiesRender.button("Save")) {
            save();
        }
    }

    private void save() {
        Website site = Website.getSite();
        site.name = iName.get();
        site.subName = iSubName.get();
        site.description = iDescription.get();
        site.websiteUrl = iUrl.get();
        site.email = iEmail.get();
        site.phone = iPhone.get();
        site.address = iAddress.get();
        Website.save();
    }

    private void updateSelection(Website selected) {
        current = selected;
        iName.set(selected.name);
        iSubName.set(selected.subName);
        iDescription.set(selected.description);
        iUrl.set(selected.websiteUrl);
        iEmail.set(selected.email);
        iPhone.set(selected.phone);
        iAddress.set(selected.address);
    }

}
