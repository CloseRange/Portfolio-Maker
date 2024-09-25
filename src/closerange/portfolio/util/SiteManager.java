package closerange.portfolio.util;

public class SiteManager {
    
    private static String path = "D:\\Programs\\github\\Portfolio-Maker\\testSite\\";
    public static void loadSite(String path) {
        SiteManager.path = path;
    }
    public static String getProjectPath() {
        return SiteManager.path;
    }
}
