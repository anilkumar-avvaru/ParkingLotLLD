package com.parkinglot.service;
import com.parkinglot.beans.*;
import com.parkinglot.context.ParkingLotContext;

import java.util.List;
import java.util.ArrayList;

public class ParkingLotApplication {
    private static LotPlan createLotPlan(long id, String name, String displayName){
        LotPlan lotPlan = new LotPlan();
        lotPlan.setId(id);
        lotPlan.setName(name);
        lotPlan.setDisplayName(displayName);
        return lotPlan;
    }

    private static Gate createGate(long id, String name, String displayName, boolean open, GateType gateType, long lotPlanId, double xDistance, double yDistance){
        Gate gate = new Gate();
        gate.setId(id);
        gate.setName(name);
        gate.setDisplayName(displayName);
        gate.setOpen(open);
        gate.setGateType(gateType);
        gate.setLotPlanId(lotPlanId);
        gate.setxDistance(xDistance);
        gate.setyDistance(yDistance);
        return gate;
    }

    private static ParkingLot createParkingLot(long id, String name, String displayName, boolean occupied, ParkingLotType parkingLotType, long lotPlanId, double xDistance, double yDistance){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(id);
        parkingLot.setName(name);
        parkingLot.setDisplayName(displayName);
        parkingLot.setOccupied(occupied);
        parkingLot.setParkingLotType(parkingLotType);
        parkingLot.setLotPlanId(lotPlanId);
        parkingLot.setxDistance(xDistance);
        parkingLot.setyDistance(yDistance);
        return parkingLot;
    }

    public static Configuration getDefaultPlan(){
        Configuration configuration = new Configuration();

        List<Gate> entryGates = new ArrayList<Gate>();
        List<Gate> exitGates = new ArrayList<Gate>();
        List<ParkingLot> parkingLots = new ArrayList<>();

        LotPlan lotPlan = createLotPlan(1, "lot-plan-1", "Anil Parking Lot");
        configuration.setLotPlan(lotPlan);

        Gate entryGate1 = createGate(11, "entry-gate-1", "Entry Gate 1", true, GateType.ENTRY, 1, 0, 0);
        Gate entryGate2 = createGate(12, "entry-gate-2", "Entry Gate 2", true, GateType.ENTRY, 1, 0, 6);
        Gate entryGate3 = createGate(13, "entry-gate-3", "Entry Gate 3", true, GateType.ENTRY, 1, 5, 8);
        entryGates.add(entryGate1);
        entryGates.add(entryGate2);
        entryGates.add(entryGate3);
        configuration.setEntryGates(entryGates);

        Gate exitGate1 = createGate(31, "exit-gate-1", "Exit Gate 1", true, GateType.EXIT, 1, 7, 5);
        Gate exitGate2 = createGate(32, "exit-gate-2", "Exit Gate 2", true, GateType.EXIT, 1, 7, 0);
        exitGates.add(exitGate1);
        exitGates.add(exitGate2);
        configuration.setExitGates(exitGates);

        int idCounter = 101;
        for(int i=2; i<=5; i++){
            for(int j=3; j<=6; j++){
                String nameSuffix = Integer.toString(idCounter);
                ParkingLot parkingLot = createParkingLot(idCounter, "parking-lot-"+nameSuffix, "Parking Lot "+nameSuffix, false, ParkingLotType.TWO_WHEELER, 1, i, j);
                parkingLots.add(parkingLot);
                idCounter++;
            }
        }
        configuration.setParkingLots(parkingLots);

        return configuration;
    }

    public static void startApplication(){
        Configuration configuration = getDefaultPlan();
        System.out.println(configuration);

        ParkingLotContext.getContext().loadConfiguration(configuration);

    }
}
