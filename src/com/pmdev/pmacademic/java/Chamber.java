package com.pmdev.pmacademic.java;

public class Chamber extends Rooms {
    private String chamberID;
    private int capacity;
    private boolean available;
    String currentDoctorID;

    public Chamber(String chamberID, int capacity) {
        super(chamberID, "chamber", capacity);
        this.chamberID = chamberID;
        this.capacity = capacity;
        this.available = true;
    }

    public String getChamberID() {
        return chamberID;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available, String currentDoctorID) {
        this.available = available;
        this.currentDoctorID = currentDoctorID;
    }

}
