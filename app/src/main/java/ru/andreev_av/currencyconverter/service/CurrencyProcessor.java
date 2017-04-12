package ru.andreev_av.currencyconverter.service;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.currencyconverter.db.dao.ICurrencyDAO;
import ru.andreev_av.currencyconverter.net.http.CbrApiData;
import ru.andreev_av.currencyconverter.net.http.HttpClient;
import ru.andreev_av.currencyconverter.net.http.IHttpClient;
import ru.andreev_av.currencyconverter.net.model.ValCurs;
import ru.andreev_av.currencyconverter.net.model.Valute;
import ru.andreev_av.currencyconverter.net.parser.CurrencyXmlParser;
import ru.andreev_av.currencyconverter.pojo.Currency;

public class CurrencyProcessor {

    private ICurrencyDAO currencySync;

    public CurrencyProcessor(ICurrencyDAO currencySync) {
        this.currencySync = currencySync;
    }

    public boolean loadValCurs() {
        IHttpClient client = new HttpClient();
        String content = client.getContent(CbrApiData.URL);
        if (!TextUtils.isEmpty(content)) {
            ValCurs valCurs = new CurrencyXmlParser(content).parse();
            if (valCurs != null)
                fillDb(valCurs);
            return true;
        } else {
            return false;
        }
    }

    private void fillDb(@NonNull ValCurs valCurs) {
        if (valCurs.getValuteList() != null && !valCurs.getValuteList().isEmpty()) {
            List<Currency> currencyList = new ArrayList<>();
            for (Valute valute : valCurs.getValuteList()) {
                Currency currency = new Currency();
                currency.setId(valute.getId());
                currency.setNumCode(valute.getNumCode());
                currency.setCharCode(valute.getCharCode());
                currency.setNominal(valute.getNominal());
                currency.setName(valute.getName());
                currency.setValue(BigDecimal.valueOf(Double.valueOf(valute.getValue().replace(",", "."))));
                switch (currency.getCharCode()) {
                    case "USD":
                        currency.setOrderShow(2);
                        break;
                    case "EUR":
                        currency.setOrderShow(3);
                        break;
                    default:
                        currency.setOrderShow(4);
                        break;
                }
                currencyList.add(currency);
            }
            currencySync.addOrUpdate(currencyList);
        }
    }
}
