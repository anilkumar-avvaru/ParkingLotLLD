package com.parkinglot.util;

import com.parkinglot.beans.Spot;

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
    public static List<Spot> orderParkingLotsByDistances(List<Spot> spots, double x, double y){
        //sort the parkingLots by distances from (x,y)
        class Pair{
            Spot spot;
            double distance;
            Pair(Spot spot, double distance){
                this.spot = spot;
                this.distance = distance;
            }

            public Spot getSpot(){
                return spot;
            }

            public double getDistance(){
                return distance;
            }
        }

        List<Pair> pairs = new ArrayList<Pair>();
        for(Spot spot : spots){
            double distance = getDistance(spot.getxDistance(), spot.getyDistance(), x, y);
            Pair pair = new Pair(spot, distance);
            pairs.add(pair);
        }

        Collections.sort(pairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair firstPair, Pair secondPair) {
                return Double.compare(firstPair.getDistance(), secondPair.getDistance());
            }
        });

        List<Spot> selectionOrder = new ArrayList<Spot>();
        for(Pair pair : pairs){
            selectionOrder.add(pair.getSpot());
        }

        return selectionOrder;
    }
}
