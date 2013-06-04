package pt.techzebra.winit.platform;

import pt.techzebra.winit.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class ServerTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected Context context_;
    protected ProgressDialog progress_dialog_;
    
    public ServerTask<Params, Progress, Result> setContext(Context context) {
        context_ = context;
        return this;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress_dialog_ = new ProgressDialog(context_);
        progress_dialog_.setIndeterminate(true);
        progress_dialog_.setCancelable(false);
        progress_dialog_.setCanceledOnTouchOutside(false);
        progress_dialog_.setMessage(context_.getString(R.string.loading));
        progress_dialog_.show();
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        progress_dialog_.dismiss();
    };
}
