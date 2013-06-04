package pt.techzebra.winit.platform;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.WinIt;
import pt.techzebra.winit.client.Badge;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.BadgesActivity;

public class LoadBadgesTask extends ServerTask<Void, Void, ArrayList<Badge>> {

    @Override
    protected ArrayList<Badge> doInBackground(Void... params) {
        return NetworkUtilities.fetchMyBadges(WinIt.getAuthToken());
    }

    @Override
    protected void onPostExecute(ArrayList<Badge> result) {
        super.onPostExecute(result);

        if(result != null) {
            Intent intent = new Intent(context_, BadgesActivity.class);
            intent.putExtra(BadgesActivity.KEY_EXTRA_BADGES, result);
            context_.startActivity(intent);
        } else {
            if(!Utilities.hasInternetConnection(context_)){
                AlertDialog.Builder builder = new AlertDialog.Builder(context_);
                builder.setMessage("No Internet connection. Do you wish to open Settings?");
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context_.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    }
                });
                builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

}
