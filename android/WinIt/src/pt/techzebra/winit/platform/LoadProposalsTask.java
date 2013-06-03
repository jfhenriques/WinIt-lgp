package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.Proposal;

public class LoadProposalsTask extends
        ServerTask<Void, Void, ArrayList<ArrayList<Proposal>>> {
    private String auth_token_;
    private ArrayList<ArrayList<Proposal>> proposals_;

    @Override
    protected ArrayList<ArrayList<Proposal>> doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<Proposal>> result) {
        super.onPostExecute(result);

        if (result == null) {
            if (!Utilities.hasInternetConnection(context_)) {
                Utilities.showInternetConnectionAlert(context_);
            }
        } else {
            // Intent intent = new Intent(context_, );
        }
    }

}
