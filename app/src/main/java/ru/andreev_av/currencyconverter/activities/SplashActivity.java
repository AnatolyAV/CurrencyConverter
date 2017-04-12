package ru.andreev_av.currencyconverter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import ru.andreev_av.currencyconverter.R;
import ru.andreev_av.currencyconverter.service.CurrencyService;
import ru.andreev_av.currencyconverter.service.CurrencyServiceHelper;

public class SplashActivity extends AppCompatActivity implements CurrencyService.CurrencyDownloadListener {

    private ProgressBar pbDownload;
    private CurrencyServiceHelper serviceConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findComponents();

        serviceConnector = new CurrencyServiceHelper(this);
    }

    private void findComponents() {
        pbDownload = (ProgressBar) findViewById(R.id.pb_download);
    }

    @Override
    public void onComplete() {
        Toast.makeText(SplashActivity.this, getString(R.string.currency_updated_success), Toast.LENGTH_SHORT).show();
        startActivity();
    }

    @Override
    public void error(String message) {
        Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
        startActivity();
    }

    private void startActivity() {
        pbDownload.setVisibility(View.GONE);
        Intent intent = new Intent(this, ConverterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceConnector.bindService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        serviceConnector.unbindService(this);
    }

    @Override
    public void onBackPressed() {
    }
}
