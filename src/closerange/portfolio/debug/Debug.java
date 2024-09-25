package closerange.portfolio.debug;

import java.util.HashSet;


public class Debug {

    public static void log(String message) {        Logger.log(message, Logger.NONE); }
    public static void info(String message) {       Logger.log(message, Logger.INFO); }
    public static void warn(String message) {       Logger.log(message, Logger.WARNING); }
    public static void error(String message) {      Logger.log(message, Logger.ERROR); }
    public static void fatal(String message) {      Logger.log(message, Logger.FATAL); }
    public static void success(String message) {    Logger.log(message, Logger.SUCCESS); }

    

    public static void errorSingle(String message) {
        if(Logger.logs.size() != 0 &&
            Logger.logs.get(Logger.logs.size() - 1).message.equals(message)) return;
        Logger.log(message, Logger.ERROR);
    }

    protected static HashSet<String> errorNotes = new HashSet<>();

    public static void clear() { Logger.clear(); }
    public static void clear(int n) {
        for(int i = 0; i < n; i++) Logger.logs.remove(Logger.logs.size() - 1);
    }
    public static void error(String title, String uniqueCode, String... subMessage) {
        if(uniqueCode != null) {
            if(errorNotes.contains(uniqueCode)) return;
            errorNotes.add(uniqueCode);
        }
        String message = "Error: " + title;
        for(String m : subMessage)
            message += "\n" + tab + m.replace("\n", "\n" + tab);
        Logger.log(message, Logger.ERROR);
    }

    private static String tab = "        ";
    public static void exception(Exception e) {
        Logger.log(e.getMessage(), Logger.ERROR);
        for(StackTraceElement ste : e.getStackTrace()) {
            String s = ste.toString();
            if(s.contains(".gen."))
                Logger.log(tab + s.split(".gen.")[1], Logger.ERROR);
        }
    }
    public static void assetLoadError(String type, String filepath) {
        Debug.error(type + " Unloaded Asset",
        type + "_UNLOADED_ASSET_" + filepath,
        "Could not load asset '" + filepath + "'");
    }

}

