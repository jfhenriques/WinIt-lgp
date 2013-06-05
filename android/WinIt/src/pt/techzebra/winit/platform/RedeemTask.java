package pt.techzebra.winit.platform;

import android.content.Intent;
import pt.techzebra.winit.R;
import pt.techzebra.winit.Utilities;
import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.MyPromotionsActivity;
import pt.techzebra.winit.ui.PromotionActivity;
import pt.techzebra.winit.ui.PromotionCodeActivity;

public class RedeemTask extends ServerTask<Object, Void, Boolean> {
    private String code_;
    
    @Override
    protected Boolean doInBackground(Object... params) {
        int pcid = (Integer) params[0];
        code_ = (String) params[1];
        
        return NetworkUtilities.redeemPromotion(pcid);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result == true) {
            MyPromotionsActivity.need_update = true;
            
            Intent intent = new Intent(context_, PromotionCodeActivity.class);
            intent.putExtra(PromotionCodeActivity.KEY_EXTRA_CODE, code_);
            ((PromotionActivity) context_).startActivity(intent);
            ((PromotionActivity) context_).finish();
        } else {
            Utilities.showToast(context_, R.string.an_error_has_occured);
        }
    }
    
}
