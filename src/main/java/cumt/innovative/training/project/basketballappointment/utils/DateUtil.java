package cumt.innovative.training.project.basketballappointment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public final static String PATTERN = "yyyy-MM-dd-HH:mm:ss";
    public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(PATTERN,Locale.ENGLISH);
    public static String getCurrentDateString() {
        return DATE_FORMAT.format(new Date());
    }

    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date getDateFromString(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(ExceptionUtil.getSimpleMessage(e));
        }
    }
}
