package pt.techzebra.winit.ui;

import com.actionbarsherlock.app.SherlockDialogFragment;

import pt.techzebra.winit.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ProposePromotionDialogFragment extends SherlockDialogFragment {
    public interface ProposePromotionListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    private ProposePromotionListener listener_;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener_ = (ProposePromotionListener) activity;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle saved_instance_state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_propose_trade)
            .setPositiveButton(R.string.propose_trade, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener_.onDialogPositiveClick(ProposePromotionDialogFragment.this);
                }
            })
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener_.onDialogNegativeClick(ProposePromotionDialogFragment.this);
                }
            });
        
        return builder.create();
    }
}
