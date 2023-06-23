package com.parkinglot.threads;

import com.parkinglot.beans.BroadcastAction;
import com.parkinglot.beans.BroadcastMessage;
import com.parkinglot.beans.ParkingLot;
import com.parkinglot.service.WorkerResource;
import com.parkinglot.context.ParkingLotContext;

public class WorkerThread extends Thread implements Runnable{
    private boolean lock = false;
    private long resourceId; //entry gate ID
    public WorkerThread(long resourceId)
    {
        this.resourceId = resourceId;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public void setLock() {
        lock = true;
    }

    public void unsetLock() {
        lock = false;
    }

    public boolean isLocked() {
        return lock;
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
            //no solution
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
        setLock();
        try{
            ParkingLot optimalParkingLot = getOptimalParkingLot();
            ParkingLotContext.getContext().broadcast(new BroadcastMessage(optimalParkingLot, BroadcastAction.OCCUPY));
            ParkingLotContext.getContext().setConfig(optimalParkingLot.getId(), true);
        }catch (Exception ex){

        }
        unsetLock();
    }
}
