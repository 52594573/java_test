package com.ktp.project.dto.examineWarn;

import java.math.BigInteger;

public class TotalNumDto {
    private Integer tableId;
    private BigInteger total;

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }
}
