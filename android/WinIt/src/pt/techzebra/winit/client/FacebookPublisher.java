package pt.techzebra.winit.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

import pt.techzebra.winit.ui.AuthenticationActivity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;

public class FacebookPublisher {
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
//	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
//	private static boolean pendingPublishReauthorization = false;
	private static Activity activity_;
	
	public String generateHashKey(Activity a_){
		PackageInfo info = null;
		String hash = null;
		try {
			info = a_.getPackageManager().getPackageInfo("pt.techzebra.winit",  PackageManager.GET_SIGNATURES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		for (Signature signature : info.signatures)
		{
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			md.update(signature.toByteArray());
			hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
		}
		return hash;
	}


	public static boolean publishStory(String name, String caption, String description, String link, String picture, Activity activity) {
		//Session session = Session.getActiveSession();
		activity_ = activity;
		
		Session session = AuthenticationActivity.forceGetActiveSession(activity_);
		if (session != null && session.isOpened()){
			
			// Check for publish permissions    
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				//pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(activity_, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				//return false;
			}

			Bundle postParams = new Bundle();
			postParams.putString("name", name);
			postParams.putString("caption", caption);
			postParams.putString("description", description);
			postParams.putString("link", link);
			postParams.putString("picture", picture);

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (Exception e) {
						Log.i("Facebook publish",
								"JSON error "+ e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(activity_.getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(activity_.getApplicationContext(), postId, Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams, 
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
			return true;
		}
		return false;
	}

	private static boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
	
}
