package com.parkinglot.dao;

import com.parkinglot.beans.Configuration;
import com.parkinglot.beans.Spot;
import com.parkinglot.context.ParkingLotContext;

import java.util.List;

public class SpotsDAO {
    public static Spot getSpotById(Long id){
        Configuration configuration = ParkingLotContext.getContext().getAppliedConfiguration();
        List<Spot> spots = configuration.getSpots();
        for(Spot spot : spots){
            if(id.equals(spot.getId())){
                return spot;
            }
        }
        return null;
    }
}
