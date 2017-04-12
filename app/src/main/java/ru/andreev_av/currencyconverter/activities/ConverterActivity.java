package ru.andreev_av.currencyconverter.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.simpleframework.xml.convert.ConvertException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import ru.andreev_av.currencyconverter.R;
import ru.andreev_av.currencyconverter.convertor.CurrencyConverter;
import ru.andreev_av.currencyconverter.db.decorator.CurrencySync;
import ru.andreev_av.currencyconverter.db.decorator.Initializer;
import ru.andreev_av.currencyconverter.pojo.Currency;

public class ConverterActivity extends AppCompatActivity {

    private LinearLayout layoutConverter;
    private EditText etFromAmount;
    private Spinner spnFromCurrency;
    private Spinner spnToCurrency;
    private Button btnConvert;
    private TextView tvToAmount;
    private List<Currency> currencyList;
    private CurrencyConverter currencyConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        CurrencySync currencySync = Initializer.getCurrencySync();
        currencyConverter = new CurrencyConverter(this);

        if (currencySync != null)
            currencyList = currencySync.getList();
        if (currencyList == null || currencyList.isEmpty() || currencyList.size() == 1) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.data_not_loaded)
                    .setMessage(R.string.try_again)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            findComponents();
            initListeners();
            initSpinners();
        }
    }

    private void findComponents() {
        layoutConverter = (LinearLayout) findViewById(R.id.layout_converter);
        layoutConverter.setVisibility(View.VISIBLE);

        spnFromCurrency = (Spinner) findViewById(R.id.spn_from_currency);
        etFromAmount = (EditText) findViewById(R.id.et_from_amount);
        spnToCurrency = (Spinner) findViewById(R.id.spn_to_currency);
        tvToAmount = (TextView) findViewById(R.id.tv_to_amount);

        btnConvert = (Button) findViewById(R.id.btn_convert);
    }

    private void initListeners() {
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvToAmount.setText(String.format(Locale.getDefault(), "%.2f"
                            , currencyConverter.performConvert((Currency) spnFromCurrency.getSelectedItem(), (Currency) spnToCurrency.getSelectedItem(), parseFromAmount())));
                } catch (ConvertException e) {
                    tvToAmount.setText("");
                    Toast.makeText(ConverterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initSpinners() {
        ArrayAdapter<Currency> fromCurrencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                currencyList);
        spnFromCurrency.setAdapter(fromCurrencyAdapter);
        spnFromCurrency.setSelection(1);

        ArrayAdapter<Currency> toCurrencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                currencyList);
        spnToCurrency.setAdapter(toCurrencyAdapter);
    }

    private BigDecimal parseFromAmount() {

        try {
            return BigDecimal.valueOf(Double.parseDouble(etFromAmount.getText().toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tvToAmount.getText() != null)
            outState.putString("tvToAmount", tvToAmount.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvToAmount.setText(savedInstanceState.getString("tvToAmount"));
    }
}
