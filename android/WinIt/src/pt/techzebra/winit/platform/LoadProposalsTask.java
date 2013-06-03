package pt.techzebra.winit.platform;

import java.util.ArrayList;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.Proposal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoadProposalsTask extends AsyncTask<Void, Void, ArrayList<ArrayList<Proposal>>> {
    private Context context_;
    private ProgressDialog progress_dialog_;
    private String auth_token_;
    private ArrayList<ArrayList<Proposal>> proposals_;
    
    public LoadProposalsTask(Context context) {
        context_ = context;
        auth_token_ = WinIt.getAuthToken();
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress_dialog_ = new ProgressDialog(context_);
        progress_dialog_.setIndeterminate(true);
        progress_dialog_.setMessage("Loading...");
        progress_dialog_.show();
    }
    
    @Override
    protected ArrayList<ArrayList<Proposal>> doInBackground(Void... params) {
        return null;
    }
    
    @Override
    protected void onPostExecute(ArrayList<ArrayList<Proposal>> result) {
        super.onPostExecute(result);
        progress_dialog_.dismiss();
        
        if (result == null) {
            if (!Utilities.hasInternetConnection(context_)) {
                Utilities.showInternetConnectionAlert(context_);
            }
        } else {
            //Intent intent = new Intent(context_, );
        }
    }

}
