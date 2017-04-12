package ru.andreev_av.currencyconverter.convertor;

import android.content.Context;
import android.support.annotation.NonNull;

import org.simpleframework.xml.convert.ConvertException;

import java.math.BigDecimal;

import ru.andreev_av.currencyconverter.R;
import ru.andreev_av.currencyconverter.pojo.Currency;

public class CurrencyConverter {

    private Context context;

    public CurrencyConverter(Context context) {
        this.context = context;
    }

    public BigDecimal performConvert(@NonNull Currency fromCurrency, @NonNull Currency toCurrency, BigDecimal fromAmount) throws ConvertException {
        if (fromCurrency.getId().equalsIgnoreCase(toCurrency.getId())) {
            throw new ConvertException(context.getString(R.string.select_different_currencies));
        } else if (fromAmount.doubleValue() <= 0) {
            throw new ConvertException(context.getString(R.string.enter_from_currency_greater_than_zero));
        }

        BigDecimal fromValueOneNominal = fromCurrency.getValue().divide(BigDecimal.valueOf(fromCurrency.getNominal()), BigDecimal.ROUND_HALF_EVEN);
        BigDecimal fromAmountInOneNominal = fromValueOneNominal.multiply(fromAmount);
        BigDecimal toValueOneNominal = toCurrency.getValue().divide(BigDecimal.valueOf(toCurrency.getNominal()), BigDecimal.ROUND_HALF_EVEN);

        return fromAmountInOneNominal.divide(toValueOneNominal, BigDecimal.ROUND_HALF_EVEN);
    }
}
