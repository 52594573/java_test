package com.ktp.project.po;

public class DonateActSearchPojo {
    private Integer id;
    private  Integer page;
    private  Integer pageSize;
    private String donAddress;
    private String donWay;
    private String donGoodsSort;
    private String donNaneOrDescribe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getDonAddress() {
        return donAddress;
    }

    public void setDonAddress(String donAddress) {
        this.donAddress = donAddress;
    }

    public String getDonWay() {
        return donWay;
    }

    public void setDonWay(String donWay) {
        this.donWay = donWay;
    }

    public String getDonGoodsSort() {
        return donGoodsSort;
    }

    public void setDonGoodsSort(String donGoodsSort) {
        this.donGoodsSort = donGoodsSort;
    }

    public String getDonNaneOrDescribe() {
        return donNaneOrDescribe;
    }

    public void setDonNaneOrDescribe(String donNaneOrDescribe) {
        this.donNaneOrDescribe = donNaneOrDescribe;
    }
}
