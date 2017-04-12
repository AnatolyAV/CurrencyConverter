package ru.andreev_av.currencyconverter.net.parser;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;

import ru.andreev_av.currencyconverter.net.model.ValCurs;

public class CurrencyXmlParser {

    private String xml;

    public CurrencyXmlParser(String xml) {
        this.xml = xml;
    }

    @Nullable
    public ValCurs parse() {
        if (!TextUtils.isEmpty(xml)) {
            Reader reader = new StringReader(xml);
            Serializer serializer = new Persister();
            try {
                return serializer.read(ValCurs.class, reader, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
