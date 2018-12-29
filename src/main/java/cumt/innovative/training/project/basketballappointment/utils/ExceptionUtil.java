package cumt.innovative.training.project.basketballappointment.utils;

public class ExceptionUtil {
    public static String getSimpleMessage(Exception ex) {
        return ex.getClass().getSimpleName() + ": " + ex.getMessage();
    }

    public static String getSimpleMessage(RuntimeException ex) {
        return ex.getClass().getSimpleName() + ": " + ex.getMessage();
    }
}
