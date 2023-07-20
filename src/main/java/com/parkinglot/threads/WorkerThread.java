package com.parkinglot.threads;

import com.parkinglot.beans.*;
import com.parkinglot.dao.ParkingLotsDAO;
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

    private ParkingLot getOptimalParkingLot() throws Exception{
        WorkerResource workerResource = ParkingLotContext.getContext().getWorkerResource(resourceId);
        if(workerResource.isLocked()){
            //sleep for 100 milliseconds and wait till the lock is unset
            //TODO: this might lead to StackOverflowError. Need to optimise this later
            this.sleep(100);
            getOptimalParkingLot();
        }
        workerResource.setLock();
        ParkingLot optimalParkingLot = workerResource.getOptimalParkingLot();
        workerResource.unsetLock();
        if(optimalParkingLot == null){
            throw new Exception("THE PARKING LOT IS FULL!!!");
        }
        return optimalParkingLot;
    }

    @Override public void run() {
        // 1) start
        // 2) Fetch the optimal parkingLot for the incoming vehicle
        // 3) Broadcast the message that the parkingLot is reserved
        // 4) Update the current state map
        // 5) Create a ticket for this {User:ParkingLot} pair  [TODO: Need to handle this when ticket flow is implemented]
        // 6) wait for next vehicle to enter
        try{
            if(parkingLotId == -1){
                ParkingLot optimalParkingLot = getOptimalParkingLot();
                ParkingLotContext.getContext().broadcast(new BroadcastMessage(optimalParkingLot, BroadcastAction.OCCUPY));
                ParkingLotContext.getContext().setConfig(optimalParkingLot.getId(), true);
                setParkingLotId(optimalParkingLot.getId());
                System.out.println("[OK] ::: The vehicle can be parked at "+optimalParkingLot.getDisplayName());
            }else{
                ParkingLot vacatingParkingLot = ParkingLotsDAO.getParkingLotById(parkingLotId);
                ParkingLotContext.getContext().broadcast(new BroadcastMessage(vacatingParkingLot, BroadcastAction.VACATE));
                ParkingLotContext.getContext().setConfig(vacatingParkingLot.getId(), false);
                System.out.println("[OK] ::: The vehicle parked at "+vacatingParkingLot.getDisplayName()+" has left and the parkinglot is now vacant");
            }
        }catch (Exception ex){
            System.out.println("[ERROR] ::: All parkinglots are occupied");
        }
    }
}
