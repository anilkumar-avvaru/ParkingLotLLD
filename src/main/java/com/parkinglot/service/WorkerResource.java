package com.parkinglot.service;

import com.parkinglot.beans.BroadcastAction;
import com.parkinglot.beans.BroadcastMessage;
import com.parkinglot.beans.ParkingLot;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorkerResource {
    private AtomicBoolean lock;
    private int optimalParkingLotIndex;
    private List<ParkingLot> selectionOrder;
    private BitSet resourceConfig;
    private HashMap<Long, Integer> orderToIndices;
    public Queue<BroadcastMessage> messageQueue;

    public WorkerResource(List<ParkingLot> selectionOrder){
        this.selectionOrder = selectionOrder;
        resourceConfig = new BitSet(selectionOrder.size()); //false --> the parking lot is free.    //true --> it is occupied
        orderToIndices = new HashMap<Long, Integer>();
        messageQueue = new LinkedList<BroadcastMessage>();
        lock = new AtomicBoolean(false);
        int counter = 0;
        for(ParkingLot parkingLot : selectionOrder){
            orderToIndices.put(parkingLot.getId(), counter++);
        }
        for(ParkingLot parkingLot : selectionOrder){
            int index = orderToIndices.get(parkingLot.getId());
            resourceConfig.set(index, parkingLot.isOccupied());
        }
        optimalParkingLotIndex = resourceConfig.nextClearBit(0);
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

    public void occupyParkingLot(ParkingLot parkingLot){
        int idx = orderToIndices.get(parkingLot.getId());
        resourceConfig.set(idx, true);
        if(idx == optimalParkingLotIndex){
            optimalParkingLotIndex = resourceConfig.nextClearBit(idx);
        }
    }

    public void vacateParkingLot(ParkingLot parkingLot){
        int idx = orderToIndices.get(parkingLot.getId());
        resourceConfig.set(idx, false);
        if((idx < optimalParkingLotIndex) || (optimalParkingLotIndex == -1)){
            optimalParkingLotIndex = idx;
        }
    }

    private void readMessages(){
        while(!messageQueue.isEmpty()){
            BroadcastMessage broadcastMessage = messageQueue.poll();
            if(broadcastMessage.getBroadcastAction() == BroadcastAction.OCCUPY){
                occupyParkingLot(broadcastMessage.getParkingLot());
            } else if (broadcastMessage.getBroadcastAction() == BroadcastAction.VACATE){
                vacateParkingLot(broadcastMessage.getParkingLot());
            }
        }
    }

    public ParkingLot getOptimalParkingLot(){
        readMessages();
        ParkingLot optimalParkingLot = selectionOrder.get(optimalParkingLotIndex);
        return optimalParkingLot;
    }

    public List<ParkingLot> getSelectionOrder() {
        return selectionOrder;
    }

    public void setSelectionOrder(List<ParkingLot> selectionOrder) {
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
