package com.ktp.project.dto.BenefitDto;

import com.ktp.project.dto.DonateSeccssListDto;

import java.util.List;

public class DonateActAndDonRec {
    private List<DonateSeccssListDto> actRecipientList;
    private List<DonateSeccssListDto> donRecipientList;

    public List<DonateSeccssListDto> getActRecipientList() {
        return actRecipientList;
    }

    public void setActRecipientList(List<DonateSeccssListDto> actRecipientList) {
        this.actRecipientList = actRecipientList;
    }

    public List<DonateSeccssListDto> getDonRecipientList() {
        return donRecipientList;
    }

    public void setDonRecipientList(List<DonateSeccssListDto> donRecipientList) {
        this.donRecipientList = donRecipientList;
    }
}
