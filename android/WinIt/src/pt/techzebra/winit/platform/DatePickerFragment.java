package pt.techzebra.winit.platform;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    public static final String TAG = "DatePickerFragment";
    
    public static final String ARG_YEAR = "year";
    public static final String ARG_MONTH = "month";
    public static final String ARG_DAY = "day";
    
    private OnDateSetListener listener_;
    
    public static DatePickerFragment newInstance(OnDateSetListener listener, int year, int month, int day) {
        final DatePickerFragment date_picker = new DatePickerFragment();
        date_picker.setListener(listener);
        
        final Bundle arguments = new Bundle();
        arguments.putInt(ARG_YEAR, year);
        arguments.putInt(ARG_MONTH, month);
        arguments.putInt(ARG_DAY, day);
        date_picker.setArguments(arguments);
        
        return date_picker;
    }
    
    public void setListener(OnDateSetListener listener) {
        listener_ = listener;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle saved_instance_state) {
        final Bundle arguments = getArguments();
        final int year = arguments.getInt(ARG_YEAR);
        final int month = arguments.getInt(ARG_MONTH);
        final int day = arguments.getInt(ARG_DAY);
        
        return new DatePickerDialog(getActivity(), listener_, year, month, day);
    }
}
