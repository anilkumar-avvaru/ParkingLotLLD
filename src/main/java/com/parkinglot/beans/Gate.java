package com.parkinglot.beans;

public class Gate {
    long Id;
    String name;
    String displayName;
    boolean isOpen;
    GateType gateType;
    long lotPlanId;
    double xDistance;
    double yDistance;
    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public GateType getGateType() {
        return gateType;
    }

    public void setGateType(GateType gateType) {
        this.gateType = gateType;
    }

    public long getLotPlanId() {
        return lotPlanId;
    }

    public void setLotPlanId(long lotPlanId) {
        this.lotPlanId = lotPlanId;
    }

    public double getxDistance() {
        return xDistance;
    }

    public void setxDistance(double xDistance) {
        this.xDistance = xDistance;
    }

    public double getyDistance() {
        return yDistance;
    }

    public void setyDistance(double yDistance) {
        this.yDistance = yDistance;
    }

    @Override
    public String toString() {
        return "Gate{" +
                "Id=" + Id +
                ", displayName='" + displayName + '\'' +
                ", isOpen=" + isOpen +
                ", gateType=" + gateType +
                ", xDistance=" + xDistance +
                ", yDistance=" + yDistance +
                '}';
    }
}
