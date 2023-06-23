package com.parkinglot.service;

import com.parkinglot.beans.BroadcastMessage;
import com.parkinglot.beans.Configuration;
import com.parkinglot.beans.Gate;
import com.parkinglot.context.ParkingLotContext;

import java.util.List;

public class BroadcastService {
    public static void broadcast(BroadcastMessage broadcastMessage){
        Configuration configuration = ParkingLotContext.getContext().getAppliedConfiguration();
        List<Gate> entryGates = configuration.getEntryGates();
        for(Gate entryGate : entryGates){
            //Add the broadcastMessage into each WorkerResources messageQueue
            WorkerResource workerResource = ParkingLotContext.getContext().getWorkerResource(entryGate.getId());
            workerResource.addBroadcastMessage(broadcastMessage);
        }
    }
}
