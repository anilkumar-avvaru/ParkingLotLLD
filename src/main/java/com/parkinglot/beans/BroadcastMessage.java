package com.parkinglot.beans;

import org.json.JSONObject;

public class BroadcastMessage {
    private ParkingLot parkingLot;
    private BroadcastAction broadcastAction;

    public BroadcastMessage(ParkingLot parkingLot, BroadcastAction broadcastAction){
        this.parkingLot = parkingLot;
        this.broadcastAction = broadcastAction;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public BroadcastAction getBroadcastAction() {
        return broadcastAction;
    }

    public void setBroadcastAction(BroadcastAction broadcastAction) {
        this.broadcastAction = broadcastAction;
    }

}
