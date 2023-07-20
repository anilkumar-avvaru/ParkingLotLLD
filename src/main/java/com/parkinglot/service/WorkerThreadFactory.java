package com.parkinglot.service;

import com.parkinglot.context.ParkingLotContext;
import com.parkinglot.threads.WorkerThread;

public class WorkerThreadFactory {
    public static WorkerThread newThread(){
        WorkerThread workerThread = null;
        try{
            workerThread = ParkingLotContext.getContext().newThread();
            if(workerThread == null){
                //All the threads are occupied. Sleep and try again
                new Thread().sleep(100);
                return newThread();
            }
        }catch (Exception ex){

        }
        return workerThread;
    }
}
