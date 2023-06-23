package com.parkinglot.beans;
public class ParkingLot {
    long Id;
    String name;
    String displayName;
    boolean isOccupied = false;
    ParkingLotType parkingLotType;
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

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public ParkingLotType getParkingLotType() {
        return parkingLotType;
    }

    public void setParkingLotType(ParkingLotType parkingLotType) {
        this.parkingLotType = parkingLotType;
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
        return "ParkingLot{" +
                "Id=" + Id +
                ", isOccupied=" + isOccupied +
                ", xDistance=" + xDistance +
                ", yDistance=" + yDistance +
                '}';
    }
}
