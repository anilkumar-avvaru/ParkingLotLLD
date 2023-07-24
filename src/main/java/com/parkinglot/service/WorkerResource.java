package com.parkinglot.service;

import com.parkinglot.beans.BroadcastAction;
import com.parkinglot.beans.BroadcastMessage;
import com.parkinglot.beans.Spot;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerResource {
    private AtomicBoolean lock;
    private int optimalSpotIndex;
    private List<Spot> selectionOrder;
    private BitSet resourceConfig;
    private HashMap<Long, Integer> orderToIndices;
    public Queue<BroadcastMessage> messageQueue;

    public WorkerResource(List<Spot> selectionOrder){
        this.selectionOrder = selectionOrder;
        resourceConfig = new BitSet(selectionOrder.size()); //false --> the parking lot is free.    //true --> it is occupied
        orderToIndices = new HashMap<Long, Integer>();
        messageQueue = new LinkedList<BroadcastMessage>();
        lock = new AtomicBoolean(false);
        int counter = 0;
        for(Spot spot : selectionOrder){
            orderToIndices.put(spot.getId(), counter++);
        }
        for(Spot spot : selectionOrder){
            int index = orderToIndices.get(spot.getId());
            resourceConfig.set(index, spot.isOccupied());
        }
        optimalSpotIndex = resourceConfig.nextClearBit(0);
    }

    public void setLock() {
        lock.set(true);
    }

    public void unsetLock() {
        lock.set(false);
    }

    public boolean isLocked() {
        return lock.get();
    }

    public void occupyParkingLot(Spot spot){
        int idx = orderToIndices.get(spot.getId());
        resourceConfig.set(idx, true);
        if(idx == optimalSpotIndex){
            optimalSpotIndex = resourceConfig.nextClearBit(idx);
        }
    }

    public void vacateParkingLot(Spot spot){
        int idx = orderToIndices.get(spot.getId());
        resourceConfig.set(idx, false);
        if((idx < optimalSpotIndex) || (optimalSpotIndex == -1)){
            optimalSpotIndex = idx;
        }
    }

    private void readMessages(){
        while(!messageQueue.isEmpty()){
            BroadcastMessage broadcastMessage = messageQueue.poll();
            if(broadcastMessage.getBroadcastAction() == BroadcastAction.OCCUPY){
                occupyParkingLot(broadcastMessage.getSpot());
            } else if (broadcastMessage.getBroadcastAction() == BroadcastAction.VACATE){
                vacateParkingLot(broadcastMessage.getSpot());
            }
        }
    }

    public Spot getOptimalSpot(){
        readMessages();
        if(optimalSpotIndex >= selectionOrder.size()){ //Return NULL if all parkinglots are occupied
            return null;
        }
        Spot optimalSpot = selectionOrder.get(optimalSpotIndex);
        return optimalSpot;
    }

    public List<Spot> getSelectionOrder() {
        return selectionOrder;
    }

    public void setSelectionOrder(List<Spot> selectionOrder) {
        this.selectionOrder = selectionOrder;
    }

    public void setResourceConfigAt(int index, boolean value){
        resourceConfig.set(index, value);
    }

    public HashMap<Long, Integer> getOrderToIndices() {
        return orderToIndices;
    }

    public void setOrderToIndices(HashMap<Long, Integer> orderToIndices) {
        this.orderToIndices = orderToIndices;
    }

    public Queue<BroadcastMessage> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(Queue<BroadcastMessage> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public void addBroadcastMessage(BroadcastMessage broadcastMessage){
        this.messageQueue.add(broadcastMessage);
    }
}
