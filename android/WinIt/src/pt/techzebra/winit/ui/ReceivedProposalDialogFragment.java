package pt.techzebra.winit.ui;

import pt.techzebra.winit.R;
import pt.techzebra.winit.platform.AnswserReceivedProposalTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ReceivedProposalDialogFragment extends SherlockDialogFragment {
    public static final String KEY_EXTRA_PROPOSAL_MY_PCID = "my_pcid";
    public static final String KEY_EXTRA_PROPOSAL_WANT_PCID = "want_pcid";
    public static final String KEY_EXTRA_PROPOSAL_POSITION = "position";
    
    public static final int ACCEPT_PROPOSAL = 1;
    public static final int REFUSE_PROPOSAL = 2;
    
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
                int answer;
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    answer = REFUSE_PROPOSAL;
                } else if (which == DialogInterface.BUTTON_POSITIVE) {
                    answer = ACCEPT_PROPOSAL;
                } else {
                    throw new IllegalArgumentException();
                }
                
                new AnswserReceivedProposalTask().setContext(activity_).execute(my_pcid_, want_pcid_, proposal_position_, answer);
            }
        };
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_);
        
        builder.setTitle(R.string.answer_proposal)
            .setMessage(R.string.choose_to_do_with_the_proposal)
            .setPositiveButton(R.string.accept, listener_)
            .setNegativeButton(R.string.refuse, listener_)
            .setNeutralButton(R.string.cancel, null);
        
        return builder.create();
    }
}
