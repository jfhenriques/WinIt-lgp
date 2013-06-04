package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.Promotion;
import pt.techzebra.winit.client.Proposal;
import pt.techzebra.winit.client.TradingContainer;
import pt.techzebra.winit.ui.TradingActivity;
import android.content.Intent;

public class LoadTradingTask extends ServerTask<Void, Void, TradingContainer> {
	@Override
	protected TradingContainer doInBackground(Void... params) {
		ArrayList<Promotion> proposable_promotions = NetworkUtilities.fetchProposableTradings();
		ArrayList<Proposal> received_proposals = NetworkUtilities.fetchProposals(NetworkUtilities.FETCH_RECEIVED_PROPOSALS);
		ArrayList<Proposal> sent_proposals = NetworkUtilities.fetchProposals(NetworkUtilities.FETCH_SENT_PROPOSALS);
		
		if (proposable_promotions == null || received_proposals == null || sent_proposals == null) {
		    return null;
		}
		
		return new TradingContainer(proposable_promotions, received_proposals, sent_proposals);
	}

	@Override
	protected void onPostExecute(TradingContainer result) {
		super.onPostExecute(result);

		if(result == null) {
			if (!Utilities.hasInternetConnection(context_)) {
			    Utilities.showInternetConnectionAlert(context_);
			}
		} else {
		    Intent intent = new Intent(context_, TradingActivity.class);
            intent.putExtra(TradingActivity.KEY_EXTRA_TRADING_CONTAINER, result);
            context_.startActivity(intent);
		}
	}
}
