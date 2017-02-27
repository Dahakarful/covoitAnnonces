import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ragonda on 20/02/2017.
 */
public class Utils {

    public static Calendar getCalendar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:");
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal;
    }

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm");
        Date newDate = dateFormat.parse(date);
        return newDate;
    }

    public static Date isoDateToDate(String date){
        Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(date);
        System.out.println(cal.getTime());
        return cal.getTime();
    }
}
