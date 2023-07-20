package com.parkinglot.service;

import com.parkinglot.threads.WorkerThread;
import com.parkinglot.util.JSONUtils;
import org.json.JSONObject;

import java.util.HashMap;

public class CommandLineInterpreter {
    //Usage: parkinglot-cli [options...] <command>
    // parkinglot-cli initiate
    // parkinglot-cli --data <configuration_json> initiate
    // parkinglot-cli --data <input_data> occupy
    // parkinglot-cli --data <input_data> vacate
    // parkinglot-cli --data <input_data> get
    // parkinglot-cli terminate

    public static String runCommand(String command){
        HashMap<String, String> commandMap = parseCmd(command);
        if(commandMap == null){
            return null;
        }
        String cmd = commandMap.get("command");
        if(cmd == null){
            return null;
        }
        String response = null;
        JSONObject dataJSON = JSONUtils.parseJSON(commandMap.get("data"));
        switch (cmd){
            case "initiate"  : response = initiate(dataJSON); break;
            case "occupy"    : response = occupy(dataJSON); break;
            case "vacate"    : response = vacate(dataJSON); break;
            case "get"       : response = get(dataJSON); break;
            case "terminate" : response = terminate(); break;
        }
        return response;
    }

    private static HashMap<String, String> parseCmd(String command){
        HashMap<String, String> commandMap = new HashMap<String, String>();
        String[] commandSplitted = command.split(" ");
        if(!"parkinglot-cli".equals(commandSplitted[0])){
            System.out.println("INVALID COMMAND");
        }
        for(int i=1; i<commandSplitted.length; i++){
            if(commandSplitted[i].equals("-d") || commandSplitted[i].equals("--data")){
                if(i == commandSplitted.length-1){
                    System.out.println("INVALID COMMAND");
                    return null;
                }
                commandMap.put("data", commandSplitted[i+1]);
                i++;
            } else {
                commandMap.put("command", commandSplitted[i]);
            }
        }
        return commandMap;
    }

    private static String initiate(JSONObject dataJSON){
        if(dataJSON == null){
            ParkingLotApplication.startApplication();
        }else{
            ParkingLotApplication.startApplication(dataJSON);
        }
        return "initiated";
    }

    private static String occupy(JSONObject dataJSON){
        Long entryGateId = dataJSON.getJSONArray("entry-gates").getJSONObject(0).getLong("id");
        //{"entry-gates":[{"id":11}]}
        WorkerThread T = WorkerThreadFactory.newThread();
        T.setResourceId(entryGateId);
        T.run();
        Long parkingLotId1 = T.getParkingLotId();
        T.reset();
        return parkingLotId1.toString();
    }

    private static String vacate(JSONObject dataJSON){
        Long parkingLotId = dataJSON.getJSONArray("parkinglots").getJSONObject(0).getLong("id");
        //{"parkinglots":[{"id":101}]}
        WorkerThread T = WorkerThreadFactory.newThread();
        T.setParkingLotId(parkingLotId);
        T.run();
        T.reset();
        return "vacated";
    }

    private static String get(JSONObject dataJSON){
        return dataJSON.toString();
    }

    private static String terminate(){
        ParkingLotApplication.stopApplication();
        return "terminated";
    }

}
