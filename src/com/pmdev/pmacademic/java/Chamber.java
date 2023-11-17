package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

public class Chamber extends Rooms {
    private String chamberID;
    HashMap<String, ArrayList<Schedule>> SCHEDULE;

    public Chamber(String chamberID, int capacity) {
        super(chamberID, "chamber", capacity);
        this.chamberID = chamberID;
        this.SCHEDULE = new HashMap<>();
    }

    public String getChamberID() {
        return chamberID;
    }

    public void Addtoschedule(String dayoftheweek, String time, String docid) {
        if (SCHEDULE.containsKey(dayoftheweek)) {
            SCHEDULE.get(dayoftheweek).add(new Schedule(time, docid));
        } else {
            ArrayList<Schedule> temp = new ArrayList<>();
            temp.add(new Schedule(time, docid));
            SCHEDULE.put(dayoftheweek, temp);
        }
    }

    public void ChangeSchedule(String dayoftheweek, String oldTime, String newTime, String docid) {
        if (SCHEDULE.containsKey(dayoftheweek)) {
            ArrayList<Schedule> scheduleList = SCHEDULE.get(dayoftheweek);
            for (Schedule schedule : scheduleList) {
                if (schedule.time.equals(oldTime) && schedule.docid.equals(docid)) {
                    schedule.time = newTime;
                    System.out.println("Schedule changed successfully.");
                    return;
                }
            }
            System.out.println("Schedule not found for the specified time and doctor.");
        } else {
            System.out.println("No schedule available for the specified day.");
        }
    }

    public void Checkiffree(String dayoftheweek, String time) {
        if (SCHEDULE.containsKey(dayoftheweek)) {
            ArrayList<Schedule> scheduleList = SCHEDULE.get(dayoftheweek);
            for (Schedule schedule : scheduleList) {
                if (schedule.time.equals(time)) {
                    System.out.println("The chamber is not free at the specified time.");
                    return;
                }
            }
            System.out.println("The chamber is free at the specified time.");
        } else {
            System.out.println("No schedule available for the specified day.");
        }
    }
}

class Schedule {
    String time;
    String docid;

    Schedule(String time, String docid) {
        this.time = time;
        this.docid = docid;
    }
}
