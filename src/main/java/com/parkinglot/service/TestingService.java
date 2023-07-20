package com.parkinglot.service;

import com.parkinglot.context.ParkingLotContext;

public class TestingService {

    private static Long doOccupy(Integer entryGateId){
        String command = "parkinglot-cli --data {\"entry-gates\":[{\"id\":"+entryGateId.toString()+"}]} occupy";
        String response = CommandLineInterpreter.runCommand(command);
        return Long.parseLong(response);
    }

    private static String doVacate(Long parkingLotId){
        String command = "parkinglot-cli --data {\"parkinglots\":[{\"id\":"+parkingLotId+"}]} vacate";
        String response = CommandLineInterpreter.runCommand(command);
        return response;
    }
    public static void runTests(){
        System.out.println("======================== TestCases Begin =============================");
        System.out.println("----- INITIATE THE APPLICATION -----");
        CommandLineInterpreter.runCommand("parkinglot-cli initiate");
        System.out.println("TestCase 1: A vehicle should be able to park when entering from a gate");
        Long parkingLotId1 = doOccupy(11);
        System.out.println("TestCase 1: END===============");

        System.out.println("TestCase 2: When three vehicles enter at the same time, all three should be able to park");
        Long parkingLotId21 = doOccupy(11);
        Long parkingLotId22 = doOccupy(12);
        Long parkingLotId23 = doOccupy(13);
        System.out.println("TestCase 2: END===============");

        System.out.println("TestCase 3: When a vehicle leaves a parking lot, the state must be updated");
        doVacate(parkingLotId1);
        System.out.println("TestCase 3: END===============");

        System.out.println("TestCase 4: When all three vehicles leave at a time, the state must be updated");
        doVacate(parkingLotId21);
        doVacate(parkingLotId22);
        doVacate(parkingLotId23);
        System.out.println("TestCase 4: END===============");

        System.out.println("TestCase 5: When the all the parking lots are occupied, an exception must be thrown when a new vehicle is requesting for a lot");
        for(int i=0; i<16; i++){
            doOccupy(11);
        }
        doOccupy(11);
        System.out.println("TestCase 5: END===============");

        System.out.println("TestCase 6: When only one parkinglot is available and two vehicles are entering at a time, only one vehicle should get allotted a parkinglot and alert must be thrown to other");
        doVacate(parkingLotId21);
        doOccupy(11);
        doOccupy(12);
        System.out.println("----- TERMINATE THE APPLICATION -----");
        CommandLineInterpreter.runCommand("parkinglot-cli terminate");
        System.out.println("TestCase 6: END===============");
        System.out.println("======================== TestCases End =============================");
    }
}
