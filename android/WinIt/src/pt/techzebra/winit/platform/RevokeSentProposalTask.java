package pt.techzebra.winit.platform;

import com.actionbarsherlock.app.SherlockFragment;

import android.support.v4.view.ViewPager;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.TradingActivity;
import pt.techzebra.winit.ui.TradingActivity.ProposalsFragment;
import pt.techzebra.winit.ui.TradingActivity.TradingPromotionsPagerAdapter;

public class RevokeSentProposalTask extends ServerTask<Integer, Void, Boolean> {
    int proposal_position_;
    
    @Override
    protected Boolean doInBackground(Integer... params) {
        int my_pcid = params[0];
        int want_pcid = params[1];
        proposal_position_ = params[2];
        
        boolean result = NetworkUtilities.revokeSentProposal(my_pcid, want_pcid);
        
        return result;
    }
    
    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        
        if (result) {
            ViewPager view_pager = ((TradingActivity) context_).getViewPager();
            int position = view_pager.getCurrentItem();
            TradingPromotionsPagerAdapter adapter = (TradingPromotionsPagerAdapter) view_pager.getAdapter();
            ProposalsFragment fragment = (ProposalsFragment) adapter.getFragment(position);
            ((TradingActivity) context_).getSentProposals().remove(proposal_position_);
            fragment.getAdapter().notifyDataSetChanged();
        }
        
        Utilities.showToast(context_, result == true ? R.string.proposal_successfully_revoked : R.string.an_error_has_occured);
    }

}
