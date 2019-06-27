package com.ktp.project.dto.BenefitDto;

import java.math.BigDecimal;

public class CommentAndApply {
    private BigDecimal donApplySum;
    private BigDecimal donCommentSum;

    public BigDecimal getDonApplySum() {
        return donApplySum;
    }

    public void setDonApplySum(BigDecimal donApplySum) {
        this.donApplySum = donApplySum;
    }

    public BigDecimal getDonCommentSum() {
        return donCommentSum;
    }

    public void setDonCommentSum(BigDecimal donCommentSum) {
        this.donCommentSum = donCommentSum;
    }
}
