package com.example.SaveMon.enums;

public enum PeriodPayments {
    allTime("For the all time","allTime"),
    day("For 1 day","day"),
    week("For 1 week","week"),
    month("For 1 month","month"),
    year("For 1 year","year");

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    private String name;
    private String value;

    PeriodPayments(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
