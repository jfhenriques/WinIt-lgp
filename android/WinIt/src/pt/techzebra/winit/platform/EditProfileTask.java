package pt.techzebra.winit.platform;

import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.EditProfileActivity;
import pt.techzebra.winit.ui.EditProfileActivity.UserHolder;

public class EditProfileTask extends ServerTask<UserHolder, Void, Integer> {
    private UserHolder user_holder_;
    
    @Override
    protected Integer doInBackground(UserHolder... params) {
        user_holder_ = params[0];
        
        return NetworkUtilities.editProfile(user_holder_, context_);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        
        if (result == 0) {
            ((EditProfileActivity) context_).profile_activity_.updateUser(user_holder_);
        }
        
        ((EditProfileActivity) context_).finish();
        Utilities.showToast(context_, result == 0 ? R.string.profile_successfully_updated : R.string.an_error_has_occured);
    }
    
}
