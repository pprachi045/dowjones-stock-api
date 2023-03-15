package com.rbc.dowjones.data;

import java.util.Objects;


public class StockPK {
    public StockPK(String stock, String date) {
        this.stock = stock;
        this.date = date;
    }

    public String getStock() {
        return stock;
    }

    public String getDate() {
        return date;
    }

    private String stock;
    private String date;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockPK)) return false;
        StockPK stockPK = (StockPK) o;
        return Objects.equals(getStock(), stockPK.getStock()) && Objects.equals(getDate(), stockPK.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStock(), getDate());
    }
}
