package ru.andreev_av.currencyconverter.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.currencyconverter.db.connector.CurrencyDbConnector;
import ru.andreev_av.currencyconverter.db.entry.CurrencyEntry;
import ru.andreev_av.currencyconverter.pojo.Currency;

public class CurrencyDAO implements ICurrencyDAO {

    private CurrencyDbConnector currencyDbConnector;

    public CurrencyDAO(CurrencyDbConnector currencyDbConnector) {
        this.currencyDbConnector = currencyDbConnector;
    }

    @Override
    public List<Currency> getList() {
        List<Currency> currencyList = new ArrayList<>();
        Cursor c = null;
        SQLiteDatabase db = currencyDbConnector.getDatabase();
        try {
            c = db.query(CurrencyDbConnector.DB_TABLE_CURRENCY,
                    null, null,
                    null, null, null, CurrencyEntry.COLUMN_ORDER_SHOW + " ASC, " + CurrencyEntry.COLUMN_CHAR_CODE + " ASC");

            if (c.getCount() > 0) {
                int columnIndexId = c.getColumnIndexOrThrow(CurrencyEntry.COLUMN_ID);
                int columnIndexCharCode = c.getColumnIndexOrThrow(CurrencyEntry.COLUMN_CHAR_CODE);
                int columnIndexNumCode = c.getColumnIndexOrThrow(CurrencyEntry.COLUMN_NUM_CODE);
                int columnIndexNominal = c.getColumnIndexOrThrow(CurrencyEntry.COLUMN_NOMINAL);
                int columnIndexName = c.getColumnIndexOrThrow(CurrencyEntry.COLUMN_NAME);
                int columnIndexValue = c.getColumnIndexOrThrow(CurrencyEntry.COLUMN_VALUE);

                c.moveToFirst();
                do {
                    Currency currency = new Currency();
                    currency.setId(c.getString(columnIndexId));
                    currency.setCharCode(c.getString(columnIndexCharCode));
                    currency.setNumCode(c.getString(columnIndexNumCode));
                    currency.setNominal(c.getInt(columnIndexNominal));
                    currency.setName(c.getString(columnIndexName));
                    currency.setValue(new BigDecimal(c.getDouble(columnIndexValue)));

                    currencyList.add(currency);
                } while (c.moveToNext());
            }

            return currencyList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
            if (db != null)
                db.close();
        }

        return null;
    }

    @Override
    public void addOrUpdate(List<Currency> currencyList) {
        SQLiteDatabase db = currencyDbConnector.getDatabase();
        db.beginTransaction();
        if (currencyList != null && !currencyList.isEmpty()) {
            for (Currency currency : currencyList) {
                ContentValues cv = new ContentValues();
                cv.put(CurrencyEntry.COLUMN_ID, currency.getId());
                cv.put(CurrencyEntry.COLUMN_CHAR_CODE, currency.getCharCode());
                cv.put(CurrencyEntry.COLUMN_NUM_CODE, currency.getNumCode());
                cv.put(CurrencyEntry.COLUMN_NOMINAL, currency.getNominal());
                cv.put(CurrencyEntry.COLUMN_NAME, currency.getName());
                cv.put(CurrencyEntry.COLUMN_VALUE, currency.getValue().doubleValue());
                cv.put(CurrencyEntry.COLUMN_ORDER_SHOW, currency.getOrderShow());
                try {
                    db.insertOrThrow(CurrencyDbConnector.DB_TABLE_CURRENCY, null, cv);
                } catch (SQLException e) {
                    db.update(CurrencyDbConnector.DB_TABLE_CURRENCY, cv, CurrencyEntry.COLUMN_ID + " = ?", new String[]{String.valueOf(currency.getId())});
                }
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}
