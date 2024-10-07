package closerange.portfolio.util;

import closerange.portfolio.frames.*;

public class FrameManager {
    public static void startAll() {
        new AssetsFrame();
        new ProjectsFrame();
        new PropertiesFrame();
        // new LogFrame();
        new TechnologyFrame();
        new CollaboratorFrame();
        new WebsiteFrame();
    }
}
