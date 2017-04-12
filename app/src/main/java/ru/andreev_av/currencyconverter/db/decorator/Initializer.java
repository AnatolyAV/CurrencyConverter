package ru.andreev_av.currencyconverter.db.decorator;

import android.content.Context;

import ru.andreev_av.currencyconverter.db.connector.CurrencyDbConnector;
import ru.andreev_av.currencyconverter.db.dao.CurrencyDAO;

public class Initializer {

    private static CurrencySync currencySync;

    public static CurrencySync getCurrencySync() {
        return currencySync;
    }

    public static void load(Context context) {
        currencySync = new CurrencySync(new CurrencyDAO(new CurrencyDbConnector(context)));
    }

}
