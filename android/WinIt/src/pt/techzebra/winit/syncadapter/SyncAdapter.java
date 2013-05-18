package pt.techzebra.winit.syncadapter;

import java.util.Date;

import pt.techzebra.winit.Constants;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.client.User;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";

    private final AccountManager account_manager_;
    private final Context context_;

    private Date last_updated_;

    public SyncAdapter(Context context, boolean auto_initialize) {
        super(context, auto_initialize);
        context_ = context;
        account_manager_ = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
            ContentProviderClient provider, SyncResult sync_result) {
        User user;
        String auth_token = null;
        
        try {
            // Use the account manager to request the credentials
            auth_token = account_manager_.blockingGetAuthToken(account, Constants.AUTHTOKEN_TYPE, true);
            
            // Fetch the user information
            user = NetworkUtilities.fetchUserInformation(auth_token, last_updated_);
            // Update the last synced date
            last_updated_ = new Date();
            
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
