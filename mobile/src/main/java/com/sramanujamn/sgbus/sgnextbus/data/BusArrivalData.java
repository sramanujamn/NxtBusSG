package com.sramanujamn.sgbus.sgnextbus.data;

/**
 * Created by raja on 3/27/2018.
 */

public class BusArrivalData {

    public static final String SEATS_AVAILABLE = "SEA";
    public static final String STANDING_AVAILABLE = "SDA";
    public static final String LIMITED_STANDING = "LSD";

    // Bus Number
    private String serviceNo;

    // Estimated arrival time
    private String estimatedArrival;

    private String load;

    private boolean isWheelChairAccessible;

    private String estimatedArrivalSecondBus;

    private String estimatedArrivalThirdBus;

    private boolean isWheelChairAccessibleSecondBus;

    private boolean isWheelChairAccessibleThirdBus;

    private String loadSecondBus;

    private String loadThirdBus;

    private double latitude;

    private double longitude;

    public boolean isWheelChairAccessibleSecondBus() {
        return isWheelChairAccessibleSecondBus;
    }

    public void setWheelChairAccessibleSecondBus(boolean wheelChairAccessibleSecondBus) {
        isWheelChairAccessibleSecondBus = wheelChairAccessibleSecondBus;
    }

    public boolean isWheelChairAccessibleThirdBus() {
        return isWheelChairAccessibleThirdBus;
    }

    public void setWheelChairAccessibleThirdBus(boolean wheelChairAccessibleThirdBus) {
        isWheelChairAccessibleThirdBus = wheelChairAccessibleThirdBus;
    }

    public String getLoadSecondBus() {
        return loadSecondBus;
    }

    public void setLoadSecondBus(String loadSecondBus) {
        this.loadSecondBus = loadSecondBus;
    }

    public String getLoadThirdBus() {
        return loadThirdBus;
    }

    public void setLoadThirdBus(String loadThirdBus) {
        this.loadThirdBus = loadThirdBus;
    }

    public String getEstimatedArrivalSecondBus() {
        return estimatedArrivalSecondBus;
    }

    public void setEstimatedArrivalSecondBus(String estimatedArrivalSecondBus) {
        this.estimatedArrivalSecondBus = estimatedArrivalSecondBus;
    }

    public String getEstimatedArrivalThirdBus() {
        return estimatedArrivalThirdBus;
    }

    public void setEstimatedArrivalThirdBus(String estimatedArrivalThirdBus) {
        this.estimatedArrivalThirdBus = estimatedArrivalThirdBus;
    }


    public boolean isWheelChairAccessible() {
        return isWheelChairAccessible;
    }

    public void setWheelChairAccessible(boolean wheelChairAccessible) {
        isWheelChairAccessible = wheelChairAccessible;
    }


    public BusArrivalData() {
        isWheelChairAccessible = false;
    }


    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public String getEstimatedArrival() {
        return estimatedArrival;
    }

    public void setEstimatedArrival(String estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
