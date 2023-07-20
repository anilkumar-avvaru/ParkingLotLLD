package com.parkinglot.dao;

import com.parkinglot.beans.Configuration;
import com.parkinglot.beans.ParkingLot;
import com.parkinglot.context.ParkingLotContext;

import java.util.List;

public class ParkingLotsDAO {
    public static ParkingLot getParkingLotById(Long id){
        Configuration configuration = ParkingLotContext.getContext().getAppliedConfiguration();
        List<ParkingLot> parkingLots = configuration.getParkingLots();
        for(ParkingLot parkingLot : parkingLots){
            if(id.equals(parkingLot.getId())){
                return parkingLot;
            }
        }
        return null;
    }
}
