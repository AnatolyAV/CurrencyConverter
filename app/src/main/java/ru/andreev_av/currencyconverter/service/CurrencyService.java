package ru.andreev_av.currencyconverter.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.andreev_av.currencyconverter.R;
import ru.andreev_av.currencyconverter.db.decorator.Initializer;

public class CurrencyService extends Service {

    private final IBinder binder = new LocalBinder();
    private CurrencyDownloadTask currencyDownloadTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void runTask(CurrencyDownloadListener listener) {
        currencyDownloadTask = new CurrencyDownloadTask(getBaseContext(), listener);
        currencyDownloadTask.execute();
    }

    public void cancelTask() {
        if (currencyDownloadTask != null && currencyDownloadTask.getStatus().equals(AsyncTask.Status.RUNNING))
            currencyDownloadTask.cancel(false);
    }

    public interface CurrencyDownloadListener {
        void onComplete();

        void error(String message);
    }

    class LocalBinder extends Binder {
        CurrencyService getService() {
            return CurrencyService.this;
        }
    }

    private class CurrencyDownloadTask extends AsyncTask<Void, Void, Boolean> {

        private static final String TAG = "CurrencyDownloadTask";
        private Context context;
        private CurrencyService.CurrencyDownloadListener listener;

        private CurrencyDownloadTask(Context context, CurrencyService.CurrencyDownloadListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "CurrencyDownloadTask start");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Initializer.load(context);
            if (isCancelled())
                return null;
            return new CurrencyProcessor(Initializer.getCurrencySync()).loadValCurs();
        }

        @Override
        protected void onPostExecute(Boolean isDownloaded) {
            super.onPostExecute(isDownloaded);
            Log.d(TAG, "CurrencyDownloadTask end");
            if (isDownloaded != null && isDownloaded.equals(Boolean.TRUE)) {
                listener.onComplete();
            } else {
                listener.error(context.getString(R.string.currency_updated_failed));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "CurrencyDownloadTask cancel");
        }
    }
}
