package ru.andreev_av.currencyconverter.pojo;

import java.math.BigDecimal;

public class Currency {

    private String id;
    private String numCode;
    private String charCode;
    private int nominal;
    private String name;
    private BigDecimal value;
    private int orderShow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public int getOrderShow() {
        return orderShow;
    }

    public void setOrderShow(int orderShow) {
        this.orderShow = orderShow;
    }

    @Override
    public String toString() {
        return charCode;
    }
}
