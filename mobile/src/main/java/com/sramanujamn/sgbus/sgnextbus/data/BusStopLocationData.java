package com.sramanujamn.sgbus.sgnextbus.data;

public class BusStopLocationData {
    private String busStopCode;
    private String busStopDescription;
    private float distance;

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getBusStopDescription() {
        return busStopDescription;
    }

    public void setBusStopDescription(String busStopDescription) {
        this.busStopDescription = busStopDescription;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
