package com.parkinglot.util;
import com.parkinglot.beans.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils {
    public static Configuration transformConfiguration(JSONObject configurationJson){
        Configuration configuration = new Configuration();

        JSONObject lotPlanJson = configurationJson.getJSONObject("lotPlan");
        LotPlan lotPlan = new LotPlan();
        lotPlan.setId(lotPlanJson.getLong("id"));
        lotPlan.setName(lotPlanJson.getString("name"));
        lotPlan.setDisplayName(lotPlanJson.getString("display_name"));
        configuration.setLotPlan(lotPlan);

        JSONArray entryGatesArr = configurationJson.getJSONArray("entry_gates");
        List<Gate> entryGates = new ArrayList<Gate>();
        for(int i=0; i<entryGatesArr.length(); i++){
            JSONObject entryGateJson = entryGatesArr.getJSONObject(i);
            Gate entryGate = new Gate();
            entryGate.setId(entryGateJson.getLong("id"));
            entryGate.setName(entryGateJson.getString("name"));
            entryGate.setDisplayName(entryGateJson.getString("display_name"));
            entryGate.setOpen(entryGateJson.getBoolean("is_open"));
            entryGate.setxDistance(entryGateJson.getDouble("x_distance"));
            entryGate.setyDistance(entryGateJson.getDouble("y_distance"));
            entryGate.setGateType(GateType.ENTRY);
            entryGate.setLotPlanId(lotPlan.getId());
            entryGates.add(entryGate);
        }
        configuration.setEntryGates(entryGates);

        JSONArray exitGatesArr = configurationJson.getJSONArray("exit_gates");
        List<Gate> exitGates = new ArrayList<Gate>();
        for(int i=0; i<exitGatesArr.length(); i++){
            JSONObject exitGateJson = exitGatesArr.getJSONObject(i);
            Gate exitGate = new Gate();
            exitGate.setId(exitGateJson.getLong("id"));
            exitGate.setName(exitGateJson.getString("name"));
            exitGate.setDisplayName(exitGateJson.getString("display_name"));
            exitGate.setOpen(exitGateJson.getBoolean("is_open"));
            exitGate.setxDistance(exitGateJson.getDouble("x_distance"));
            exitGate.setyDistance(exitGateJson.getDouble("y_distance"));
            exitGate.setGateType(GateType.EXIT);
            exitGate.setLotPlanId(lotPlan.getId());
            exitGates.add(exitGate);
        }
        configuration.setExitGates(exitGates);

        JSONArray parkingLotsArr = configurationJson.getJSONArray("parking_lots");
        List<Spot> spots = new ArrayList<Spot>();
        for(int i=0; i<parkingLotsArr.length(); i++){
            JSONObject parkingLotJson = parkingLotsArr.getJSONObject(i);
            Spot spot = new Spot();
            spot.setId(parkingLotJson.getLong("id"));
            spot.setName(parkingLotJson.getString("name"));
            spot.setDisplayName(parkingLotJson.getString("display_name"));
            spot.setOccupied(parkingLotJson.getBoolean("is_occupied"));
            spot.setxDistance(parkingLotJson.getDouble("x_distance"));
            spot.setyDistance(parkingLotJson.getDouble("y_distance"));
            String spotType = parkingLotJson.getString("spot_type");
            if("two_wheeler".equals(spotType)){
                spot.setSpotType(SpotType.TWO_WHEELER);
            } else if ("four_wheeler".equals(spotType)) {
                spot.setSpotType(SpotType.FOUR_WHEELER);
            } else if ("truck".equals(spotType)) {
                spot.setSpotType(SpotType.TRUCK);
            } else {
                continue;
            }
            spot.setLotPlanId(lotPlan.getId());
            spots.add(spot);
        }
        configuration.setSpots(spots);

        return configuration;
    }

    public static JSONObject parseJSON(String jsonString){
        JSONObject parsedJson = null;
        try{
            parsedJson = new JSONObject(jsonString);
        }catch (Exception ex){

        }
        return parsedJson;
    }
}
