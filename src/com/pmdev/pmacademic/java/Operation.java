package com.pmdev.pmacademic.java;

public class Operation {
    String patientName;
    String date;
    String procedure;
    String theatreid;
    String time;

    Operation(String patientName, String date, String procedure,String time,String doctorID,String theatreid){
        this.patientName = patientName;
        this.date = date;
        this.procedure = procedure;
        this.theatreid = theatreid;
    }

    @Override
    public String toString() {
        return "Patient: " + patientName + " | Date: " + date + " | Procedure: " + procedure;
    }

    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getTheatreid() {
        return theatreid;
    }

}
