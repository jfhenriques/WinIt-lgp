package pt.techzebra.winit.platform;

import pt.techzebra.winit.WinIt;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.widget.TextView;

/**
 * <p>This class contains date-related utilities for creating text
 * and display it in Views.</p>
 */
public class DateUtilities {
    /**
     * Fills in a TextView with a date provided in milliseconds.
     * 
     * @param view The field to update.
     * @param millis The time in milliseconds.
     */
    public static void setDate(TextView view, long millis) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
        
        String date_string = DateUtils.formatDateTime(WinIt.getAppContext(), millis, flags);

        view.setText(date_string);
    }
    
    /**
     * Fills in a TextView with a time provided in milliseconds.
     * 
     * @param view The field to update.
     * @param millis The time in milliseconds.
     */
    public static void setTime(TextView view, long millis) {
        int flags = DateUtils.FORMAT_SHOW_TIME;
        if (DateFormat.is24HourFormat(WinIt.getAppContext())) {
            flags |= DateUtils.FORMAT_24HOUR;
        }
        
        String time_string = DateUtils.formatDateTime(WinIt.getAppContext(), millis, flags);
        
        view.setText(time_string);
    }
}
