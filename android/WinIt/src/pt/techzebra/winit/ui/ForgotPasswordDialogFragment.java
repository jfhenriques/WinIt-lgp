package pt.techzebra.winit.ui;


import pt.techzebra.winit.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ForgotPasswordDialogFragment extends SherlockDialogFragment  {
    public interface ForgotPasswordDialogListener {
        public void onFinishForgotPasswordDialog(String email);
    }
    
    private ForgotPasswordDialogListener listener_;
    
    private EditText email_edit_;
    
    public ForgotPasswordDialogFragment() {}
    
    @Override
    public Dialog onCreateDialog(Bundle saved_instance_state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        View view = inflater.inflate(R.layout.forgot_password_fragment, null);
        
        email_edit_ = (EditText) view.findViewById(R.id.email_edit);
        
        builder.setView(view)
            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = email_edit_.getText().toString();
                    listener_.onFinishForgotPasswordDialog(email);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ForgotPasswordDialogFragment.this.getDialog().cancel();
                }
            });
        
        email_edit_.requestFocus();
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        
        return dialog;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener_ = (ForgotPasswordDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ForgotPasswordDialogListener");
        }
    }
}
