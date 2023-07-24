package com.parkinglot.threads;

import com.parkinglot.beans.*;
import com.parkinglot.dao.SpotsDAO;
import com.parkinglot.service.WorkerResource;
import com.parkinglot.context.ParkingLotContext;

public class WorkerThread extends Thread implements Runnable{
    private long resourceId = -1; //entry gate ID
    private long parkingLotId = -1; //vacating parking Lot

    public WorkerThread(){
        super();
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public void reset(){
        this.resourceId = -1;
        this.parkingLotId = -1;
        ParkingLotContext.getContext().addIntoWorkerThreadQueue(this);
    }

    private Spot getOptimalSpot() throws Exception{
        WorkerResource workerResource = ParkingLotContext.getContext().getWorkerResource(resourceId);
        if(workerResource.isLocked()){
            //sleep for 100 milliseconds and wait till the lock is unset
            //TODO: this might lead to StackOverflowError. Need to optimise this later
            this.sleep(100);
            getOptimalSpot();
        }
        workerResource.setLock();
        Spot optimalSpot = workerResource.getOptimalSpot();
        workerResource.unsetLock();
        if(optimalSpot == null){
            throw new Exception("THE PARKING LOT IS FULL!!!");
        }
        return optimalSpot;
    }

    @Override public void run() {
        // 1) start
        // 2) Fetch the optimal spot for the incoming vehicle
        // 3) Broadcast the message that the spot is reserved
        // 4) Update the spots config map
        // 5) Create a ticket for this {User:Spot} pair  [TODO: Need to handle this when ticket flow is implemented]
        // 6) wait for next vehicle to enter
        try{
            if(parkingLotId == -1){
                Spot optimalSpot = getOptimalSpot();
                ParkingLotContext.getContext().broadcast(new BroadcastMessage(optimalSpot, BroadcastAction.OCCUPY));
                ParkingLotContext.getContext().setSpotConfig(optimalSpot.getId(), true);
                setParkingLotId(optimalSpot.getId());
                System.out.println("[OK] ::: The vehicle can be parked at "+optimalSpot.getDisplayName());
            }else{
                Spot vacatingSpot = SpotsDAO.getSpotById(parkingLotId);
                ParkingLotContext.getContext().broadcast(new BroadcastMessage(vacatingSpot, BroadcastAction.VACATE));
                ParkingLotContext.getContext().setSpotConfig(vacatingSpot.getId(), false);
                System.out.println("[OK] ::: The vehicle parked at "+vacatingSpot.getDisplayName()+" has left and the spot is now vacant");
            }
        }catch (Exception ex){
            System.out.println("[ERROR] ::: All spots are occupied");
        }
    }
}
