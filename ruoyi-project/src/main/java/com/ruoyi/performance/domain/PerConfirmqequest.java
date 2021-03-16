package com.ruoyi.performance.domain;

public class PerConfirmqequest {

    private Integer requestid;

    private Integer teamid;

    private Integer year;

    private String status;

    private Integer confirmuserid;

    private String confirmtime;

    public Integer getRequestid() {
        return requestid;
    }

    public void setRequestid(Integer requestid) {
        this.requestid = requestid;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getConfirmuserid() {
        return confirmuserid;
    }

    public void setConfirmuserid(Integer confirmuserid) {
        this.confirmuserid = confirmuserid;
    }

    public String getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(String confirmtime) {
        this.confirmtime = confirmtime;
    }

    @Override
    public String toString() {
        return "PerConfirmqequest{" +
                "requestid=" + requestid +
                ", teamid=" + teamid +
                ", year=" + year +
                ", status='" + status + '\'' +
                ", confirmuserid=" + confirmuserid +
                ", confirmtime='" + confirmtime + '\'' +
                '}';
    }
}