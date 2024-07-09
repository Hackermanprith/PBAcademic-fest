package com.pmdev.pmacademic.java;

import java.util.*;

class Rooms{
    String roomid;
    String roomtype;
    int roomsidecapacity = 10;
    public HashMap<String, Beds> Bedsinroom;
    public Rooms(){}
    public static int Takeintinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        int number;
        try {
            number = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid Input, Try again");
            number = Takeintinp(msg);
        }
        System.out.println();
        return number;
    }

    Rooms(String roomid,String roomtype, int roomsidecapacity){
        Bedsinroom = new HashMap<>();
        this.roomid = roomid;
        this.roomtype = roomtype;
        this.roomsidecapacity = roomsidecapacity;
    }
    public void addBed(String bedId,int cost,String bedtype) {
        if (Bedsinroom.size() < roomsidecapacity * 2) {
            Beds newBed = new Beds(bedId,cost,bedtype);
            Bedsinroom.put(bedId, newBed);
            System.out.println("Bed " + bedId + " added to Room " + roomid);
        } else {
            System.out.println("Room " + roomid + " is at full capacity.");
        }
    }
    public void removebed(String bedid){
        Bedsinroom.remove(bedid);

    }

    public void printRoom() {
        boolean roomFound = false;
        roomFound = true;
        System.out.println("Room Details:");
        System.out.println("Room ID: " + this.roomid);
        System.out.println("Room Type: " + this.roomtype);
        System.out.println("Room Capacity: " + this.roomsidecapacity);
        System.out.println("Occupants:");
        for (Beds bed : this.Bedsinroom.values()) {
            System.out.println("  Bed ID: " + bed.bedid);
            System.out.println("  Bed Type: " + bed.bedtype);
            System.out.println("  Occupant: " + bed.occupant);
            System.out.println("-------------------------");
        }
    }

}
class   Beds{
    String bedid;
    String bedtype;
    int bed_cost;
    String occupant;
    Beds(String bedid, int price,String bedtype){
        this.bedid = bedid;
        this.occupant = null;
        this.bedtype = bedtype;
        this.bed_cost = price;
    }
    public Beds() {

    }
    public void changeBedType(String newBedType) {
        this.bedtype = newBedType;
    }

}
 class Chamber extends Rooms {
     private String chamberID;
     ArrayList<String> offtimes;
     HashMap<String, String> bookings;

    public Chamber(String chamberID, int capacity) {
        super(chamberID, "chamber", capacity);
        this.chamberID = chamberID;
        offtimes = new ArrayList<>();
        bookings = new HashMap<>();
    }
     public String getChamberID() {
         return chamberID;
     }

 }
