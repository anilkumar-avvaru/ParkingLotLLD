package com.parkinglot.beans;

public class Gate {
    long Id;
    String name;
    String displayName;
    boolean isOpen;
    GateType gateType;
    long lotPlanId;
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

}
