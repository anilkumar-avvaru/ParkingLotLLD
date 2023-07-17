package com.parkinglot.context;

import com.parkinglot.beans.*;
import com.parkinglot.service.BroadcastService;
import com.parkinglot.service.WorkerResource;
import com.parkinglot.threads.WorkerThread;
import com.parkinglot.util.CommonUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingLotContext { //This will be shared across the application
    private static ConcurrentHashMap<Long, WorkerThread> workerThreadPool;      //Thread pool of worker-threads for picking the optimal parking lot for an entering vehicle
    private static ConcurrentHashMap<Long, WorkerResource> resources;           //entryGate to parking lot selection order mappings
    private static ConcurrentHashMap<Long, AtomicBoolean> parkingLotConfigMap;  //The current configuration of all parkingLots at any point of time
    private static Configuration appliedConfiguration;
    private static ParkingLotContext parkingLotContext = null;
    public static ParkingLotContext getContext(){
        if(parkingLotContext == null){
            parkingLotContext = new ParkingLotContext();
        }
        return parkingLotContext;
    }

    private static void init(){
        //create thread-pools and shared resources
        workerThreadPool = new ConcurrentHashMap<Long, WorkerThread>();
        resources = new ConcurrentHashMap<Long, WorkerResource>();
        parkingLotConfigMap = new ConcurrentHashMap<Long, AtomicBoolean>();
    }

    public static void destroy(){
        //terminate all the running threads
        for(Long entryGateId : workerThreadPool.keySet()){
            workerThreadPool.get(entryGateId).stop();
        }

        //set the single-ton instance to null
        parkingLotContext = null;
    }

    public static ConcurrentHashMap<Long, WorkerResource> getResources() {
        return resources;
    }

    public static void setResources(ConcurrentHashMap<Long, WorkerResource> resources) {
        ParkingLotContext.resources = resources;
    }

    public static Configuration getAppliedConfiguration() {
        return appliedConfiguration;
    }

    public static void setAppliedConfiguration(Configuration appliedConfiguration) {
        ParkingLotContext.appliedConfiguration = appliedConfiguration;
    }

    public static WorkerResource getWorkerResource(long resourceId){
        return resources.get(resourceId);
    }

    public static void loadConfiguration(Configuration configuration){
        init();
        appliedConfiguration = configuration;

        //For each entryGate
        //  - sort the parkingLots by distance from this Gate
        //  - Create a WorkerResource, and add it to resources map with entryGateId
        //  - create a WorkerThread, and add it to workerThreadPool map with entryGateId
        List<ParkingLot> parkingLots = configuration.getParkingLots();
        for(Gate entryGate : configuration.getEntryGates()){
            List<ParkingLot> selectionOrder = CommonUtils.orderParkingLotsByDistances(parkingLots, entryGate.getxDistance(), entryGate.getyDistance());
            resources.put(entryGate.getId(), new WorkerResource(selectionOrder));
            workerThreadPool.put(entryGate.getId(), new WorkerThread(entryGate.getId()));
        }
        //Feed the parkingLots statuses to parkingLotConfigMap
        for(ParkingLot parkingLot : parkingLots){
            parkingLotConfigMap.put(parkingLot.getId(), new AtomicBoolean(parkingLot.isOccupied()));
        }
    }

    public synchronized void broadcast(BroadcastMessage broadcastMessage) {
        BroadcastService.broadcast(broadcastMessage);
    }

    public void setConfig(Long parkingLotId, boolean isOccupied){
        AtomicBoolean currentState = parkingLotConfigMap.get(parkingLotId);
        currentState.set(isOccupied);
    }

}
