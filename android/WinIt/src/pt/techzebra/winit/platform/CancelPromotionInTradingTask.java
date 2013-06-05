package pt.techzebra.winit.platform;

import pt.techzebra.winit.client.NetworkUtilities;
import pt.techzebra.winit.ui.MyPromotionsActivity;
import pt.techzebra.winit.ui.PromotionActivity;

public class CancelPromotionInTradingTask extends ServerTask<Integer, Void, Void> {
    
    @Override
    protected Void doInBackground(Integer... params) {
        int pcid =  params[0];
        NetworkUtilities.deletePromotionInTrading(pcid);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        
        MyPromotionsActivity.need_update = true;
        
        ((PromotionActivity) context_).finish();
    }
}
