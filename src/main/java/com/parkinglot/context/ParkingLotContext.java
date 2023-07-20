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
    private static Queue<WorkerThread> workerThreadQueue;                       //Thread pool of worker-threads for picking the optimal parking lot for an entering vehicle
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
        workerThreadQueue = new LinkedList<WorkerThread>();
        resources = new ConcurrentHashMap<Long, WorkerResource>();
        parkingLotConfigMap = new ConcurrentHashMap<Long, AtomicBoolean>();
    }

    public static void destroy(){
        //terminate all the running threads
        while(!workerThreadQueue.isEmpty()){
            workerThreadQueue.poll().interrupt();
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

    public WorkerResource getWorkerResource(long resourceId){
        return resources.get(resourceId);
    }

    public void loadConfiguration(Configuration configuration){
        init();
        setAppliedConfiguration(configuration);

        //For each entryGate
        //  - sort the parkingLots by distance from this Gate
        //  - Create a WorkerResource, and add it to resources map with entryGateId
        List<ParkingLot> parkingLots = configuration.getParkingLots();
        for(Gate entryGate : configuration.getEntryGates()){
            List<ParkingLot> selectionOrder = CommonUtils.orderParkingLotsByDistances(parkingLots, entryGate.getxDistance(), entryGate.getyDistance());
            resources.put(entryGate.getId(), new WorkerResource(selectionOrder));
        }
        //Feed the parkingLots statuses to parkingLotConfigMap
        for(ParkingLot parkingLot : parkingLots){
            parkingLotConfigMap.put(parkingLot.getId(), new AtomicBoolean(parkingLot.isOccupied()));
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

    public void setConfig(Long parkingLotId, boolean isOccupied){
        AtomicBoolean currentState = parkingLotConfigMap.get(parkingLotId);
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
