package com.uottawa.cloud.model;

/**
 * This class defines historical statistical data.
 */
public class StatisticalHistoryData {
    private String deviceID;
    private String date;
    double avgBreathrate;
    double stddevBreathrate;

    public StatisticalHistoryData(String deviceID,String date, double avgBreathrate, double stddevBreathrate) {
        this.deviceID = deviceID;
        this.date = date;
        this.avgBreathrate = avgBreathrate;
        this.stddevBreathrate = stddevBreathrate;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAvgBreathrate() {
        return avgBreathrate;
    }

    public void setAvgBreathrate(double avgBreathrate) {
        this.avgBreathrate = avgBreathrate;
    }

    public double getStddevBreathrate() {
        return stddevBreathrate;
    }

    public void setStddevBreathrate(double stddevBreathrate) {
        this.stddevBreathrate = stddevBreathrate;
    }

    @Override
    public String toString() {
        return "ActivityState{" +
                "deviceID='" + deviceID + '\'' +
                ", date=" + date +
                ", avgBreathrate=" + avgBreathrate +
                ", stddevBreathrate=" + stddevBreathrate +
                '}';
    }
}
