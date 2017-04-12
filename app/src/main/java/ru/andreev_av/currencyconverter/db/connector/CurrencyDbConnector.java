package ru.andreev_av.currencyconverter.db.connector;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.andreev_av.currencyconverter.db.entry.CurrencyEntry;

public class CurrencyDbConnector {

    public static final String DB_TABLE_CURRENCY = "currency";
    private static final String DB_NAME = "currency_converter.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_CURRENCY_CREATE = "CREATE TABLE currency (" +
            CurrencyEntry.COLUMN_ID + " VARCHAR(10) PRIMARY KEY, " +
            CurrencyEntry.COLUMN_NUM_CODE + " VARCHAR(3) NOT NULL, " +
            CurrencyEntry.COLUMN_CHAR_CODE + " VARCHAR(3) NOT NULL, " +
            CurrencyEntry.COLUMN_NOMINAL + " INTEGER NOT NULL, " +
            CurrencyEntry.COLUMN_NAME + " VARCHAR(50) NOT NULL, " +
            CurrencyEntry.COLUMN_VALUE + " DECIMAL NOT NULL, " +
            CurrencyEntry.COLUMN_ORDER_SHOW + " INTEGER NOT NULL" +
            ")";
    private static final String DB_TABLE_CURRENCY_RUB_INSERT = "INSERT INTO currency (" +
            CurrencyEntry.COLUMN_ID + ", " +
            CurrencyEntry.COLUMN_NUM_CODE + ", " +
            CurrencyEntry.COLUMN_CHAR_CODE + ", " +
            CurrencyEntry.COLUMN_NOMINAL + ", " +
            CurrencyEntry.COLUMN_NAME + ", " +
            CurrencyEntry.COLUMN_VALUE + ", " +
            CurrencyEntry.COLUMN_ORDER_SHOW + ") VALUES ('R00000', '000', 'RUB', 1, 'Российский рубль', 1, 1)";

    private final Context context;
    private DBHelper dbHelper;

    public CurrencyDbConnector(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context, DB_NAME, null, DB_VERSION);
    }

    public SQLiteDatabase getDatabase() {
        return dbHelper.getReadableDatabase();
    }

    public void close() {
        if (dbHelper != null) dbHelper.close();
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_TABLE_CURRENCY_CREATE);
            db.execSQL(DB_TABLE_CURRENCY_RUB_INSERT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
