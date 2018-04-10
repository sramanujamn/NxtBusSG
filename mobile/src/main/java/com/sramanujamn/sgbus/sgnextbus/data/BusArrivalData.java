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
}
