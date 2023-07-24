package com.parkinglot.beans;

public class BroadcastMessage {
    private Spot spot;
    private BroadcastAction broadcastAction;

    public BroadcastMessage(Spot spot, BroadcastAction broadcastAction){
        this.spot = spot;
        this.broadcastAction = broadcastAction;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public BroadcastAction getBroadcastAction() {
        return broadcastAction;
    }

    public void setBroadcastAction(BroadcastAction broadcastAction) {
        this.broadcastAction = broadcastAction;
    }

}
