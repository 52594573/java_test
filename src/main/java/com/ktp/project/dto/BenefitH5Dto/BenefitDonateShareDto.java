package com.ktp.project.dto.BenefitH5Dto;

import com.ktp.project.dto.DonateApplyDetailDto;
import com.ktp.project.dto.DonateDetailDto;
import com.ktp.project.entity.BenefitActivity;
import com.ktp.project.entity.BenefitDonate;

import java.util.List;

public class BenefitDonateShareDto {
    private BenefitActivity activity;//活动详情
    private DonateDetailDto donateDetailDto;//捐赠详情
    private List<DonateApplyDetailDto> recipientDetails;//该捐赠的详情

    public BenefitActivity getActivity() {
        return activity;
    }

    public void setActivity(BenefitActivity activity) {
        this.activity = activity;
    }

    public DonateDetailDto getDonateDetailDto() {
        return donateDetailDto;
    }

    public void setDonateDetailDto(DonateDetailDto donateDetailDto) {
        this.donateDetailDto = donateDetailDto;
    }

    public List<DonateApplyDetailDto> getRecipientDetails() {
        return recipientDetails;
    }

    public void setRecipientDetails(List<DonateApplyDetailDto> recipientDetails) {
        this.recipientDetails = recipientDetails;
    }
}
