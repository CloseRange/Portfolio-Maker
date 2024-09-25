package closerange.portfolio.debug;


import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

public class Logger {
    public static final int NONE = 0;
    public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;
    public static final int FATAL = 4;
    public static final int SUCCESS = 5;

    protected static ArrayList<Log> logs = new ArrayList<>();

    protected static void log(String message, int type) {
        logs.add(new Log(message, type, ""));
    }
    protected static void clear() {
        logs.clear();
    }
    protected static class Log {
        protected String message;
        protected int type;
        protected String time;

        public Log(String message, int type, String time) {
            this(message, type, time, "IDE");
        }
        public Log(String message, int type, String time, String caller) {
            this.message = message;
            this.type = type;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[HH:mm:ss]");
            LocalDateTime now = LocalDateTime.now();
            this.time = String.format("[%s]", dtf.format(now));
        }
    }
}


