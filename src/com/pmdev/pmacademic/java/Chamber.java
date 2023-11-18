package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

public class Chamber extends Rooms {
    private String chamberID;
    int capacity;

    public Chamber(String chamberID, int capacity) {
        super(chamberID, "chamber", capacity);
        this.chamberID = chamberID;
        this.capacity = capacity;
    }

    public String getChamberID() {
        return chamberID;
    }

}
