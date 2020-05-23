package com.uottawa.cloud.model;

/**
 * This class defines the variables to calculate active percentage of a patient.
 */
public class ActivityState {
    String deviceID;
    String startdate;
    String enddate;
    int activecount;
    int totalcount;
    double percentage;

    public ActivityState(String deviceID, String startdate, String enddate, int activecount, int totalcount, double pct) {
        this.deviceID = deviceID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.activecount = activecount;
        this.totalcount = totalcount;
        this.percentage = pct;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getActivecount() {
        return activecount;
    }

    public void setActivecount(int activecount) {
        this.activecount = activecount;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    @Override
    public String toString() {
        return "ActivityState{" +
                "deviceID='" + deviceID +
                ", startdate=" + startdate +
                ", enddate=" + enddate +
                ", activecount=" + activecount +
                ", totalcount=" + totalcount +
                ", percentage=" + percentage +
                '}';
    }
}
