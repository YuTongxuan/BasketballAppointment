package cumt.innovative.training.project.basketballappointment.logger;

import cumt.innovative.training.project.basketballappointment.config.ConfigurationModel;
import cumt.innovative.training.project.basketballappointment.utils.DateUtil;
import cumt.innovative.training.project.basketballappointment.utils.ExceptionUtil;

import java.io.FileWriter;
import java.io.IOException;

/* Logger APIs will never throw RuntimeException */
public class Logger {

    public final static int DANGER = 2;
    public final static int WARNING = 1;
    public final static int INFO = 0;

    public static String generateLogString(Throwable throwable, int level, Object message) {
        StackTraceElement stackTraceElement = throwable.getStackTrace()[0];
        int lineNumber = stackTraceElement.getLineNumber();
        String fileName = stackTraceElement.getFileName();
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        String levelName = level == INFO ? "INFO" : (level == WARNING ? "WARNING" : (level == DANGER ? "DANGER" : "UNKNOWN"));
        String dateString = DateUtil.getCurrentDateString();

        int index = className.lastIndexOf('.');
        className = className.substring(index >= 0 ? (index + 1) : 0);
        return dateString + ";" + levelName + ";" + fileName + ";" + className + "." + methodName + "();" + lineNumber + "=>" + message.toString();
    }

    public static void log(Throwable throwable, int level, Object message, boolean databaseMode /* default false */) {
        if (!ConfigurationModel.getInstance().logEnabled) {
            return;
        }
        if (!ConfigurationModel.getInstance().databaseLogEnabled && databaseMode) {
            return;
        }
        if (level < ConfigurationModel.getInstance().logLevel) {
            return;
        }
        String logString = generateLogString(throwable, level, message);
        if (ConfigurationModel.getInstance().consoleLogEnabled) {
            System.out.println(logString);
        }
        // log to file
        FileWriter writer = null;
        try {
            writer = new FileWriter(ConfigurationModel.getInstance().logFile, true);
            writer.write(logString + System.getProperty("line.separator"));
        } catch (IOException e) {
            System.out.println(ExceptionUtil.getSimpleMessage(e));
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println(ExceptionUtil.getSimpleMessage(e));
            }
        }
    }

    public static void log(Throwable throwable, int level, Object message) {
        log(throwable, level, message, false);
    }
}
