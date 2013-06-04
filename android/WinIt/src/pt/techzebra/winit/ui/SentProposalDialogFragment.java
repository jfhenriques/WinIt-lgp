package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import pt.techzebra.winit.platform.RevokeSentProposalTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class SentProposalDialogFragment extends SherlockDialogFragment {
    public static final String KEY_EXTRA_PROPOSAL_MY_PCID = "my_pcid";
    public static final String KEY_EXTRA_PROPOSAL_WANT_PCID = "want_pcid";
    public static final String KEY_EXTRA_PROPOSAL_POSITION = "position";
    
    private int my_pcid_;
    private int want_pcid_;
    private int proposal_position_;
    
    private TradingActivity activity_;
    private DialogInterface.OnClickListener listener_;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity_ = (TradingActivity) activity;
    }
    
    @Override
    public void onCreate(Bundle saved_instance_state) {
        super.onCreate(saved_instance_state);
        
        Bundle arguments = getArguments();
        my_pcid_ = arguments.getInt(KEY_EXTRA_PROPOSAL_MY_PCID, -1);
        want_pcid_ = arguments.getInt(KEY_EXTRA_PROPOSAL_WANT_PCID, -1);
        proposal_position_ = arguments.getInt(KEY_EXTRA_PROPOSAL_POSITION, -1);
        if (my_pcid_ == -1 || want_pcid_ == -1 || proposal_position_ == -1) {
            throw new IllegalArgumentException();
        }
        
        listener_ = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    new RevokeSentProposalTask().setContext(activity_).execute(my_pcid_, want_pcid_, proposal_position_);
                }
            }
        };
    }
    
    @Override
    public Dialog onCreateDialog(Bundle saved_instance_state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_);
        
        builder.setTitle(R.string.revoke_sent_proposal)
            .setMessage(R.string.you_will_lose_the_opportunity_to_trade_your_promotion)
            .setPositiveButton(R.string.revoke, listener_)
            .setNegativeButton(R.string.cancel, listener_);
            
        
        return builder.create();
    }
}
