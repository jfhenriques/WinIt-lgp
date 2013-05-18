package pt.techzebra.winit.syncadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service to handle Account sync. This is invoked with an intent with action
 * ACTION_AUTHENTICATOR_INTENT. It instantiates the SyncAdapter and returns its
 * IBinder.
 */
public class SyncService extends Service {
    private static final Object sync_adapter_lock_ = new Object();
    private static SyncAdapter sync_adapter_ = null;

    @Override
    public void onCreate() {
        synchronized (sync_adapter_lock_) {
            if (sync_adapter_ == null) {
                sync_adapter_ = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sync_adapter_.getSyncAdapterBinder();
    }
}
