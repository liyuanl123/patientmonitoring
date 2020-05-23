package com.uottawa.cloud.model;

/**
 * This class defines real time patient's info.
 */
public class PatientInfo {
    String deviceID;
    Long epoch;
    Long interval;
    String time;
    Double heartRate;
    Double breathRate;
    Boolean fallDetected;
    Integer detected;

    public PatientInfo(String deviceID, Long epoch, Long interval, String time, Double heartRate, Double breathRate, Boolean fallDetected, Integer detected) {
        this.deviceID = deviceID;
        this.epoch = epoch;
        this.interval = interval;
        this.time = time;
        this.heartRate = heartRate;
        this.breathRate = breathRate;
        this.fallDetected = fallDetected;
        this.detected = detected;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long time) {
        this.epoch = time;
    }

    public Double getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Double heartRate) {
        this.heartRate = heartRate;
    }

    public Double getBreathRate() {
        return breathRate;
    }

    public void setBreathRate(Double breathRate) {
        this.breathRate = breathRate;
    }

    public Boolean getFallDetected() {
        return fallDetected;
    }

    public void setFallDetected(Boolean fallDetected) {
        this.fallDetected = fallDetected;
    }

    public Integer getDetected() {
        return detected;
    }

    public void setDetected(Integer detected) {
        this.detected = detected;
    }

    // timezone could be "Etc/UTC", "Australia/Sydney", "US/Eastern".
//    public static ZonedDateTime convertEpochToTime(long epoch, String timezone) {
//        Instant instant = Instant.ofEpochSecond(1222334444);
//        ZoneId zoneId = ZoneId.of("US/Eastern");
//        ZonedDateTime result = ZonedDateTime.ofInstant(instant, zoneId);
//        System.out.println(result.format());
//        return result;
//    }

    @Override
    public String toString() {
        return "PatientInfo{" +
                "deviceID='" + deviceID + '\'' +
                ", time=" + epoch +
                ", interval=" + interval +
                ", time=" + time +
                ", heartRate=" + heartRate +
                ", breathRate=" + breathRate +
                ", fallDetected=" + fallDetected +
                ", detected='" + detected + '\'' +
                '}';
    }
}
