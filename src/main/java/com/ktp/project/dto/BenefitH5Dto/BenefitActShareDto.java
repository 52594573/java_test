package com.ktp.project.dto.BenefitH5Dto;

import com.ktp.project.dto.DonateSeccssListDto;
import com.ktp.project.entity.BenefitActivity;
import com.ktp.project.entity.BenefitDonate;

import java.util.List;

public class BenefitActShareDto {
    private BenefitActivity activity;//活动

    private List<DonateSeccssListDto> recipients;//捐赠成功评论等详情

    private List<BenefitDonate> donates;//捐赠列表

    public BenefitActivity getActivity() {
        return activity;
    }

    public void setActivity(BenefitActivity activity) {
        this.activity = activity;
    }

    public List<DonateSeccssListDto> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<DonateSeccssListDto> recipients) {
        this.recipients = recipients;
    }

    public List<BenefitDonate> getDonates() {
        return donates;
    }

    public void setDonates(List<BenefitDonate> donates) {
        this.donates = donates;
    }
}
