package ru.spb.itmo.asashina.lab3;

public class RequestQuery {

    private String query;
    private Integer resultAmount;

    public RequestQuery(String query, Integer resultAmount) {
        this.query = query;
        this.resultAmount = resultAmount;
    }

    public RequestQuery() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(Integer resultAmount) {
        this.resultAmount = resultAmount;
    }

}
