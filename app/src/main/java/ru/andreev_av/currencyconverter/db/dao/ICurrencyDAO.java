package ru.andreev_av.currencyconverter.db.dao;

import java.util.List;

import ru.andreev_av.currencyconverter.pojo.Currency;

public interface ICurrencyDAO {

    List<Currency> getList();

    void addOrUpdate(List<Currency> currencyList);
}
