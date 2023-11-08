package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

public class Doctor {
    String doctorid;
    String name;
    String speaclity;
    String emphno;
    HashMap<String,String> timings;
    double billed;
    double perpatientcharge = 1000;
    int doctordailyLimit = 10;

    ArrayList<String> data;

    HashMap<String, ArrayList<Patient>> Patient_Registry;


    Doctor(String doctorid,String name,String speaclity,String emphno,ArrayList<String> data,double perpatientcharge,int dlimit){
        this.doctorid = doctorid;
        this.name = name;
        data = new ArrayList<String>();
        this.speaclity = speaclity;
        this.emphno = emphno;
        this.data = data;
        this.doctordailyLimit = dlimit;
        this.perpatientcharge = perpatientcharge;
        Patient_Registry = new HashMap<>();
    }
    public void AddPatientToSchedule(String date,Patient patient){
        if(Patient_Registry.containsKey(date)&&Patient_Registry.get(date).size()<doctordailyLimit){
            Patient_Registry.get(date).add(patient);
        }
        else{
            if(!(Patient_Registry.containsKey(date))){
                System.out.println("Doctor is not available on this day");
            }
            if(Patient_Registry.get(date).size()>doctordailyLimit){
                System.out.println("Doctor is fully booked on this day");
            }
        }
        System.out.println("Patient Added to the schedule");
        System.out.println("Your appointment is on "+date+" at "+timings.get(date)+" with Dr."+name+"after"+Patient_Registry.get(date).size()+" patients");
        patient.UpcomingAppointments.put(date,this.doctorid);

    }
    public void RemovePatientReg(String date,Patient patient){
        if(Patient_Registry.containsKey(date)){
            Patient_Registry.get(date).remove(patient);
            patient.UpcomingAppointments.remove(date);
        }
        else{
            System.out.println("Doctor is not available on this day");
        }
    }

}