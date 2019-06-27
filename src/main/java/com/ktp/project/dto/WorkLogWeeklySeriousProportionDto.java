package com.ktp.project.dto;


import java.math.BigDecimal;

public class WorkLogWeeklySeriousProportionDto {

    private String serious;//严重
    private String caution;//警示
    private String ordinary;//普通

    public Double getSerious() {
        if (serious == null) {
            serious = "0";
        }
        return Double.valueOf(serious);
    }

    public void setSerious(String serious) {
        this.serious = serious;
    }

    public Double getCaution() {
        if (caution == null) {
            caution = "0";
        }
        return Double.valueOf(caution);
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public Double getOrdinary() {
        if (ordinary == null) {
            ordinary = "0";
        }
        return Double.valueOf(ordinary);
    }

    public void setOrdinary(String ordinary) {
        this.ordinary = ordinary;
    }
}
