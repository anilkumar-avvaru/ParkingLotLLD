package com.parkinglot.service;
import com.parkinglot.beans.*;

import java.util.List;
import java.util.ArrayList;

public class DefaultsLoader {
    private static LotPlan createLotPlan(long id, String name, String displayName){
        LotPlan lotPlan = new LotPlan();
        lotPlan.setId(id);
        lotPlan.setName(name);
        lotPlan.setDisplayName(displayName);
        return lotPlan;
    }

    private static Gate createGate(long id, String name, String displayName, boolean open, GateType gateType, long lotPlanId){
        Gate entryGate = new Gate();
        entryGate.setId(id);
        entryGate.setName(name);
        entryGate.setDisplayName(displayName);
        entryGate.setOpen(open);
        entryGate.setGateType(gateType);
        entryGate.setLotPlanId(lotPlanId);
        return entryGate;
    }

    private static ParkingLot createParkingLot(long id, String name, String displayName, boolean occupied, ParkingLotType parkingLotType, long lotPlanId){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(id);
        parkingLot.setName(name);
        parkingLot.setDisplayName(displayName);
        parkingLot.setOccupied(occupied);
        parkingLot.setParkingLotType(parkingLotType);
        parkingLot.setLotPlanId(lotPlanId);
        return parkingLot;
    }

    public static List<ParkingLot> load(){
        LotPlan lotPlan = createLotPlan(1, "lot-plan-1", "Anil Parking Lot");
        Gate entryGate = createGate(101, "entry-gate-1", "Entry Gate 1", true, GateType.ENTRY, 1);
        Gate exitGate = createGate(301, "exit-gate-1", "Exit Gate 1", true, GateType.EXIT, 1);

        List<ParkingLot> parkingLots = new ArrayList<>();
        for(int i=1001; i<=1009; i++){
            String nameSuffix = Integer.toString(i);
            ParkingLot parkingLot = createParkingLot(i, "parking-lot-"+nameSuffix, "Parking Lot "+nameSuffix, false, ParkingLotType.TWO_WHEELER, 1);
            parkingLots.add(parkingLot);
        }
        for(int i=2001; i<=2007; i++){
            String nameSuffix = Integer.toString(i);
            ParkingLot parkingLot = createParkingLot(i, "parking-lot-"+nameSuffix, "Parking Lot "+nameSuffix, false, ParkingLotType.FOUR_WHEELER, 1);
            parkingLots.add(parkingLot);
        }
        for(int i=3001; i<=3005; i++){
            String nameSuffix = Integer.toString(i);
            ParkingLot parkingLot = createParkingLot(i, "parking-lot-"+nameSuffix, "Parking Lot "+nameSuffix, false, ParkingLotType.TRUCK, 1);
            parkingLots.add(parkingLot);
        }

        return parkingLots;
    }
}
