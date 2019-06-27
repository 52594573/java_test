package com.ktp.project.dto;


import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkLogTeamCountDto {


    private BigInteger countNum;

    public BigInteger getCountNum() {
        return countNum;
    }

    public void setCountNum(BigInteger countNum) {
        this.countNum = countNum;
    }
}
