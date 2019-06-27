package com.ktp.project.dto;


import java.math.BigDecimal;
import java.util.List;

public class WorkLogWeeklyDetailDto {

    private String onestarp;
    private String twostarp;
    private String threestarp;
    private String fourstarp;
    private String fivestarp;

    private List<WorkLogWeeklyDetailTeamListDto> teamLists;


    public List<WorkLogWeeklyDetailTeamListDto> getTeamLists() {
        return teamLists;
    }

    public void setTeamLists(List<WorkLogWeeklyDetailTeamListDto> teamLists) {
        this.teamLists = teamLists;
    }

    public double getOnestarp() {
        if(onestarp==null){
            onestarp="0";
        }
        return Double.valueOf(onestarp);
    }

    public void setOnestarp(String onestarp) {
        this.onestarp = onestarp;
    }

    public double getTwostarp() {
        if(twostarp==null){
            twostarp="0";
        }
        return Double.valueOf(twostarp);
    }

    public void setTwostarp(String twostarp) {
        this.twostarp = twostarp;
    }

    public double getThreestarp() {
        if(threestarp==null){
            threestarp="0";
        }
        return Double.valueOf(threestarp);
    }

    public void setThreestarp(String threestarp) {
        this.threestarp = threestarp;
    }

    public double getFourstarp() {
        if(fourstarp==null){
            fourstarp="0";
        }
        return Double.valueOf(fourstarp);
    }

    public void setFourstarp(String fourstarp) {
        this.fourstarp = fourstarp;
    }

    public double getFivestarp() {
        if(fivestarp==null){
            fivestarp="0";
        }
        return Double.valueOf(fivestarp);
    }

    public void setFivestarp(String fivestarp) {
        this.fivestarp = fivestarp;
    }

    @Override
    public String toString() {
        return "WorkLogWeeklyDetailDto{" +
                "onestarp=" + onestarp +
                ", twostarp=" + twostarp +
                ", threestarp=" + threestarp +
                ", fourstarp=" + fourstarp +
                ", fivestarp=" + fivestarp +
                '}';
    }
}
