package com.parkinglot.service;
import com.parkinglot.beans.*;
import com.parkinglot.context.ParkingLotContext;
import com.parkinglot.util.JSONUtils;
import org.json.JSONObject;

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

    private static Spot createSpot(long id, String name, String displayName, boolean occupied, SpotType spotType, long lotPlanId, double xDistance, double yDistance){
        Spot spot = new Spot();
        spot.setId(id);
        spot.setName(name);
        spot.setDisplayName(displayName);
        spot.setOccupied(occupied);
        spot.setSpotType(spotType);
        spot.setLotPlanId(lotPlanId);
        spot.setxDistance(xDistance);
        spot.setyDistance(yDistance);
        return spot;
    }

    public static Configuration getDefaultPlan(){
        //TODO: The code base currently works on the assumption that all parking lots are of same type
        //      Need to make changes later to work for all types of vehicles
        Configuration configuration = new Configuration();

        List<Gate> entryGates = new ArrayList<Gate>();
        List<Gate> exitGates = new ArrayList<Gate>();
        List<Spot> spots = new ArrayList<>();

        LotPlan lotPlan = createLotPlan(1, "lot-plan-1", "Anil Parking Lot");
        configuration.setLotPlan(lotPlan);

        Gate entryGate1 = createGate(11, "entry-gate-1", "Entry Gate 1", true, GateType.ENTRY, 1, 0, 0.5);
        Gate entryGate2 = createGate(12, "entry-gate-2", "Entry Gate 2", true, GateType.ENTRY, 1, 0, 4.5);
        Gate entryGate3 = createGate(13, "entry-gate-3", "Entry Gate 3", true, GateType.ENTRY, 1, 2.5, 5);
        entryGates.add(entryGate1);
        entryGates.add(entryGate2);
        entryGates.add(entryGate3);
        configuration.setEntryGates(entryGates);

        Gate exitGate1 = createGate(31, "exit-gate-1", "Exit Gate 1", true, GateType.EXIT, 1, 0, 2.5);
        Gate exitGate2 = createGate(32, "exit-gate-2", "Exit Gate 2", true, GateType.EXIT, 1, 5, 2.5);
        exitGates.add(exitGate1);
        exitGates.add(exitGate2);
        configuration.setExitGates(exitGates);

        int idCounter = 101;
        for(int i=1; i<5; i++){
            for(int j=1; j<5; j++){
                String nameSuffix = Integer.toString(idCounter);
                Spot spot = createSpot(idCounter, "parking-lot-"+nameSuffix, "Parking Lot "+nameSuffix, false, SpotType.TWO_WHEELER, 1, j, i);
                spots.add(spot);
                idCounter++;
            }
        }
        configuration.setSpots(spots);

        return configuration;
    }

    public static void startApplication(){
        Configuration configuration = getDefaultPlan();
        System.out.println(configuration);
        startApplication(configuration);
    }

    public static void startApplication(JSONObject configurationJSON){
        if(configurationJSON == null){
            Configuration configuration = JSONUtils.transformConfiguration(configurationJSON);
            startApplication(configuration);
        }else{
            startApplication();
        }
    }

    public static void startApplication(Configuration configuration){
        ParkingLotContext.getContext().loadConfiguration(configuration);
    }

    public static void stopApplication(){
        ParkingLotContext.getContext().destroy();
    }
}
