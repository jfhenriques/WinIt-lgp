package pt.techzebra.winit.platform;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;

public class RevokeSentProposalTask extends ServerTask<Integer, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Integer... params) {
        int my_pcid = params[0];
        int want_pcid = params[1];
        
        boolean result = NetworkUtilities.revokeSentProposal(my_pcid, want_pcid);
        
        return result;
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Utilities.showToast(context_, result == true ? R.string.proposal_successfully_revoked : R.string.an_error_has_occured);
    }

}
