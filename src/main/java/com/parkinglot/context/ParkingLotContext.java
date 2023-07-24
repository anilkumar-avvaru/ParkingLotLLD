package com.parkinglot.context;

import com.parkinglot.beans.*;
import com.parkinglot.service.BroadcastService;
import com.parkinglot.service.WorkerResource;
import com.parkinglot.threads.WorkerThread;
import com.parkinglot.util.CommonUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingLotContext { //This will be shared across the application
    private static Queue<WorkerThread> workerThreadQueue;                       //Thread pool of worker-threads for picking the optimal spot for an entering vehicle
    private static ConcurrentHashMap<Long, WorkerResource> resources;           //entryGate to spot selection order mappings
    private static ConcurrentHashMap<Long, AtomicBoolean> spotsConfigMap;       //The current configuration of all spots at any point of time
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
        workerThreadQueue = new LinkedList<WorkerThread>();
        resources = new ConcurrentHashMap<Long, WorkerResource>();
        spotsConfigMap = new ConcurrentHashMap<Long, AtomicBoolean>();
    }

    public static void destroy(){
        //terminate all the running threads
        while(!workerThreadQueue.isEmpty()){
            workerThreadQueue.poll().interrupt();
        }

        //set the single-ton instance to null
        parkingLotContext = null;
    }

    public static Configuration getAppliedConfiguration() {
        return appliedConfiguration;
    }

    public static void setAppliedConfiguration(Configuration appliedConfiguration) {
        ParkingLotContext.appliedConfiguration = appliedConfiguration;
    }

    public WorkerResource getWorkerResource(long resourceId){
        return resources.get(resourceId);
    }

    public void loadConfiguration(Configuration configuration){
        init();
        setAppliedConfiguration(configuration);

        //For each entryGate
        //  - sort the spots by distance from this Gate
        //  - Create a WorkerResource, and add it to resources map with entryGateId
        List<Spot> spots = configuration.getSpots();
        for(Gate entryGate : configuration.getEntryGates()){
            List<Spot> selectionOrder = CommonUtils.orderParkingLotsByDistances(spots, entryGate.getxDistance(), entryGate.getyDistance());
            resources.put(entryGate.getId(), new WorkerResource(selectionOrder));
        }
        //Feed the parkingLots statuses to parkingLotConfigMap
        for(Spot spot : spots){
            spotsConfigMap.put(spot.getId(), new AtomicBoolean(spot.isOccupied()));
        }

        //Create WorkerThreads and load them into the thread-pool
        int totalGates = configuration.getEntryGates().size() + configuration.getExitGates().size();
        for(int i=0; i<totalGates; i++){
            workerThreadQueue.add(new WorkerThread());
        }
    }

    public synchronized void broadcast(BroadcastMessage broadcastMessage) {
        BroadcastService.broadcast(broadcastMessage);
    }

    public void setSpotConfig(Long parkingLotId, boolean isOccupied){
        AtomicBoolean currentState = spotsConfigMap.get(parkingLotId);
        currentState.set(isOccupied);
    }

    public synchronized WorkerThread newThread(){
        if(!workerThreadQueue.isEmpty()){
            return workerThreadQueue.poll();
        }
        return null;
    }

    public synchronized void addIntoWorkerThreadQueue(WorkerThread workerThread){
        workerThreadQueue.add(workerThread);
    }

}
