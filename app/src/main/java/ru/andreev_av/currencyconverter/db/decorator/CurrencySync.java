package ru.andreev_av.currencyconverter.db.decorator;

import java.util.List;

import ru.andreev_av.currencyconverter.db.dao.CurrencyDAO;
import ru.andreev_av.currencyconverter.db.dao.ICurrencyDAO;
import ru.andreev_av.currencyconverter.pojo.Currency;

public class CurrencySync implements ICurrencyDAO {

    private CurrencyDAO currencyDAO;
    private List<Currency> currencyList;

    public CurrencySync(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
        init();
    }

    private void init() {
        currencyList = currencyDAO.getList();// запрос в БД происходит только один раз, чтобы заполнить коллекцию currencyList
    }

    @Override
    public List<Currency> getList() {
        return currencyList;
    }

    @Override
    public void addOrUpdate(List<Currency> currencyList) {
        currencyDAO.addOrUpdate(currencyList);
        this.currencyList = currencyDAO.getList();
    }
}
