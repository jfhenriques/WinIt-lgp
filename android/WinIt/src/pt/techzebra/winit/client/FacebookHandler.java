package pt.techzebra.winit.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;

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

public class FacebookHandler {
	
	private Session session_;
	private Activity activity_;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

	public FacebookHandler(Activity activity, Session.StatusCallback callback){
		activity_ = activity;
		session_ = Session.openActiveSessionFromCache(activity_);
		if(session_ != null){
			session_.closeAndClearTokenInformation();
		}
		session_ = Session.openActiveSession(activity, true, callback);
	}
	
	
	
	public Session getSession() {
		return session_;
	}



	public String generateHashKey(){
		//TODO apenas necessário se quiserem gerar uma keyhash para o vosso projecto. A que está a ser usada na configuração do Facebook App é a minham, podem adicionar lá a vossa
		PackageInfo info = null;
		String hash = null;
		try {
			info = activity_.getPackageManager().getPackageInfo("pt.techzebra.winit",  PackageManager.GET_SIGNATURES);
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


	public void publishStory(String name, String caption, String description, String link, String picture, String privacy) {
		Session session = Session.getActiveSession();

		if (session != null){

			// Check for publish permissions    
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(activity_, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			/*postParams.putString("name", "Facebook SDK for Android");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");*/
			postParams.putString("name", name);
			postParams.putString("caption", caption);
			postParams.putString("description", description);
			postParams.putString("link", link);
			postParams.putString("picture", picture);
			postParams.putString("privacy", privacy);

			Request.Callback callback= new Request.Callback() {
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
		}

	}

	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
	
}
