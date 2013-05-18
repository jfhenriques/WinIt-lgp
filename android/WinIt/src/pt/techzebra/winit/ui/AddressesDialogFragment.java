package pt.techzebra.winit.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AddressesDialogFragment extends SherlockDialogFragment {
    public interface AddressesDialogListener {
        public void onDialogPositiveClick(int which, DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    SignupActivity activity_;
    AddressesDialogListener listener_;
    
    @Override
    public Dialog onCreateDialog(Bundle saved_instance_state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        builder.setTitle("Pick an address")
            .setItems(activity_.addresses_, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener_.onDialogPositiveClick(which, AddressesDialogFragment.this);
                }
            });
        
        return builder.create();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity_ = (SignupActivity) activity;
        listener_ = (AddressesDialogListener) activity;
    }
}
