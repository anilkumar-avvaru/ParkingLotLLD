package com.parkinglot.util;

import com.parkinglot.beans.ParkingLot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommonUtils {
    private static double getDistance(double x1, double y1, double x2, double y2){
        double left = Math.abs(x1-y1);
        double right = Math.abs(x2-y2);
        return Math.sqrt((left*left)+(right*right));
    }
    public static List<ParkingLot> orderParkingLotsByDistances(List<ParkingLot> parkingLots, double x, double y){
        //sort the parkingLots by distances from (x,y)
        class Pair{
            ParkingLot parkingLot;
            double distance;
            Pair(ParkingLot parkingLot, double distance){
                this.parkingLot = parkingLot;
                this.distance = distance;
            }

            public ParkingLot getParkingLot(){
                return parkingLot;
            }

            public double getDistance(){
                return distance;
            }
        }

        List<Pair> pairs = new ArrayList<Pair>();
        for(ParkingLot parkingLot : parkingLots){
            double distance = getDistance(parkingLot.getxDistance(), parkingLot.getyDistance(), x, y);
            Pair pair = new Pair(parkingLot, distance);
            pairs.add(pair);
        }

        Collections.sort(pairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair firstPair, Pair secondPair) {
                return Double.compare(firstPair.getDistance(), secondPair.getDistance());
            }
        });

        List<ParkingLot> selectionOrder = new ArrayList<ParkingLot>();
        for(Pair pair : pairs){
            selectionOrder.add(pair.getParkingLot());
        }

        return selectionOrder;
    }
}
