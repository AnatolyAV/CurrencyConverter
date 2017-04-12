package ru.andreev_av.currencyconverter.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class CurrencyServiceHelper {

    private static final String TAG = "CurrencyServiceHelper";
    private CurrencyService service;
    private CurrencyService.CurrencyDownloadListener listener;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            Log.d(TAG, "onServiceConnected");
            CurrencyService.LocalBinder binder = (CurrencyService.LocalBinder) serviceBinder;
            service = binder.getService();
            service.runTask(listener);

        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "onServiceDisconnected");
            if (service != null)
                service.cancelTask();
        }
    };

    public CurrencyServiceHelper(CurrencyService.CurrencyDownloadListener listener) {
        this.listener = listener;
    }

    public void bindService(Context context) {
        Intent serviceIntent = new Intent(context, CurrencyService.class);
        context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        if (service != null)
            service.cancelTask();
        context.unbindService(serviceConnection);
    }

}
