package com.pmdev.pmacademic.java;

import java.util.ArrayList;

class Beds{
    String bedid;
    String bedtype;
    String occupant;
    ArrayList<Integer> roomsoneachfloor;
    Beds(String bedid, Double price,String bedtype){
        this.bedid = bedid;
        this.occupant = null;
        this.bedtype = bedtype;

    }
    public void UpgradeBed(String bedid, Double price,String bedtype){
        this.bedid = bedid;
        this.occupant = null;
        this.bedtype = bedtype;
    }

    }

