package com.pmdev.pmacademic.java;

import java.util.Hashtable;

class Rooms{
    String roomid;
    String roomtype;
    int roomsidecapacity = 10;
    Hashtable<String,Beds> occupant;//bed id, occupantid


    Rooms(String roomid,String roomtype, int roomsidecapacity){
        occupant = new Hashtable<String,Beds>();
        this.roomid = roomid;
        this.roomtype = roomtype;
        this.roomsidecapacity = roomsidecapacity;
    }
}