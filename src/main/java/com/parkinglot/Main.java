package com.parkinglot;

import com.parkinglot.beans.ParkingLot;
import com.parkinglot.service.DefaultsLoader;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        List<ParkingLot> parkingLots = DefaultsLoader.load();
        System.out.println(parkingLots.size());
    }
}