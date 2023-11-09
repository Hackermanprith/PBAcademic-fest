package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pmdev.pmacademic.java.Hospital.Takeintinp;

public class OperationTheatre extends Rooms {
    String theatreID;
    int capacity;
    ArrayList<Operation> operations;
    HashMap<String, Boolean> availability; // Date,Time -> Availability

    OperationTheatre(String theatreID, int capacity) {
        super(theatreID, "theatre", capacity);
        this.theatreID = theatreID;
        this.capacity = capacity;
        this.operations = new ArrayList<>();
        this.availability = new HashMap<>();
    }

    public String getTheatreID() {
        return theatreID;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isAvailable(String date) {
        return availability.getOrDefault(date, true);
    }

    public void setAvailability(String date, String time,boolean isAvailable) {
        availability.put(date+"_"+time, isAvailable);
    }

    public void scheduleOperation(Operation operation,String time) {
        if (isAvailable(operation.getDate())) {
            operations.add(operation);
            setAvailability(String.valueOf(operation.getDate()+"_"+operation.getTime()),time,false);
        } else {
            System.out.println("The theatre is not available on " + operation.getDate());
        }
    }

    public void printOperations() {
        if (operations.isEmpty()) {
            System.out.println("No operations scheduled in this theatre.");
        } else {
            System.out.println("Scheduled Operations in Theatre " + theatreID + ":");
            for (Operation operation : operations) {
                System.out.println(operation);
            }
        }
    }


}


