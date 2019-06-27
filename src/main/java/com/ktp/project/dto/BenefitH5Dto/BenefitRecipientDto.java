package com.ktp.project.dto.BenefitH5Dto;

import com.ktp.project.dto.DonateSeccssListDto;
import com.ktp.project.entity.BenefitActivity;
import com.ktp.project.entity.BenefitDonate;
import com.ktp.project.entity.BenefitEvaluate;

import java.util.List;

public class BenefitRecipientDto {
    private BenefitActivity activity;//活动

    private List<BenefitDonate> donates;//捐赠列表

    private List<BenefitEvaluate> evaList;//查询评论列表

    private DonateSeccssListDto recipientDetail;//领取详情

    public BenefitActivity getActivity() {
        return activity;
    }

    public void setActivity(BenefitActivity activity) {
        this.activity = activity;
    }

    public List<BenefitEvaluate> getEvaList() {
        return evaList;
    }

    public void setEvaList(List<BenefitEvaluate> evaList) {
        this.evaList = evaList;
    }

    public List<BenefitDonate> getDonates() {
        return donates;
    }

    public void setDonates(List<BenefitDonate> donates) {
        this.donates = donates;
    }

    public DonateSeccssListDto getRecipientDetail() {
        return recipientDetail;
    }

    public void setRecipientDetail(DonateSeccssListDto recipientDetail) {
        this.recipientDetail = recipientDetail;
    }
}
