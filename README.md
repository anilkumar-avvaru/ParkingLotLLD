# ParkingLotLLD

## Problem Statement
- Design a software which efficiently allots a parkinglot for the entering vehicles
- The admin should be able to configure anything related to the parkinglot via the web-portal 

## Example Configuration
> The below is the default configuration used for testing the functionality of the parking lot
![something](src/main/images/default-configuration.png)

## Architecture
> TODO ::: Add a high-level architecture involving all the components

## Working
### Assumptions
- All the vehicles will follow a queue approach and enter or exit one after another.
- When a vehicle is allotted with a parkinglot, it is always parked in it's allotted spot.
- The parkinglot is made available only after the vehicle exits via an exit-gate
- When all the parkinglots are occupied, the entering vehicles have to wait till any parked vehicle exits
- As part of phase-1, the system assumes all vehicles are of same type. Support for other vehicles types will be added as part of phase-2.
### Algorithm
#### Occupy
- Fetch a WorkerThread(T) from the ThreadPool and save the EntryGateId(EG1) to the thread
- When the WorkerThread runs, it picks the WorkerResource for the EntryGateId(EG1)
- If the WorkerResource is locked, wait till the resource is available
- Acquire the lock on the WorkerResource
- This WorkerResource has the following
- - lock 
- - optimalParkingLot [The optimal parkinglot as per the current configuration]
- - selectionOrder [A bitset for the parkinglots sorted based on euclidean distance from the entry-gate(EG1)]
- - messageQueue
- The WorkerThread first checks for any previous messages from the queue and updates the selectionOrder
- Before returning the optimalParkingLot, we update it with the next OptimalParkingLot.
- Notify the BroadcastService [A message will be broadcasted to all other WorkerResources stating that the parkinglot is occupied. [These will be added it their respective messageQueues]]
- Notify the TicketingService [To punch a ticket and this will be used at the Exit gate to get the details of parkinglot this vehicle is vacating]
- Release the lock and update the current state of the parkinglots map
- Reset the thread and add it back to the ThreadPool
#### Vacate
### Time-complexity analysis
### Limitations


