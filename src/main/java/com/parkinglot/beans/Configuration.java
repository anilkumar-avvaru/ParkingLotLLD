package com.parkinglot.beans;

import java.util.List;

public class Configuration {
    public LotPlan lotPlan;
    public List<Gate> entryGates;
    public List<Gate> exitGates;
    public List<Spot> spots;

    public LotPlan getLotPlan() {
        return lotPlan;
    }

    public void setLotPlan(LotPlan lotPlan) {
        this.lotPlan = lotPlan;
    }

    public List<Gate> getEntryGates() {
        return entryGates;
    }

    public void setEntryGates(List<Gate> entryGates) {
        this.entryGates = entryGates;
    }

    public List<Gate> getExitGates() {
        return exitGates;
    }

    public void setExitGates(List<Gate> exitGates) {
        this.exitGates = exitGates;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "\nlotPlan=" + lotPlan +
                ",\n entryGates=" + entryGates +
                ",\n exitGates=" + exitGates +
                ",\n spots=" + spots +
                "\n}";
    }
}
