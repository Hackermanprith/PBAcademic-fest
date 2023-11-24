package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;
import com.pmdev.pmacademic.java.Hospital.*;

import static com.pmdev.pmacademic.java.Hospital.Takeintinp;

public class Doctor {
    String doctorid;
    String name;
    String speaclity;
    String emphno;
    HashMap<Integer,String> timings;
    ArrayList<String> offdays;
    double earned = 0.0;
    double clinicshare = 0.0;
    boolean isMedicOnStandby = false;
    HashMap<String, String> chamberSchedule;
    boolean isMedicAvaliable = false;
    double perpatientcharge = 1000.0;
    int doctordailyLimit = 10;
    ArrayList<String> data;
    HashMap<String, ArrayList<Patient>> Patient_Registry;


    Doctor(String doctorid,String name,String speaclity,String emphno,ArrayList<String> data,double perpatientcharge,int dlimit,double clinicshare,ArrayList<String> timings,boolean isMedicOnStandby){
        this.doctorid = doctorid;
        this.name = name;
        data = new ArrayList<>();
        this.speaclity = speaclity;
        this.emphno = emphno;
        this.data = data;
        this.doctordailyLimit = dlimit;
        this.perpatientcharge = perpatientcharge;
        this.clinicshare = clinicshare;
        Patient_Registry = new HashMap<>();
        this.timings = new HashMap<>();
        for(int i = 0;i<timings.size();i++){
            this.timings.put(i,timings.get(i));
            i++;
        }
        offdays = new ArrayList<>();
        this.isMedicOnStandby = isMedicOnStandby;

    }

    public Doctor() {
        data = new ArrayList<>();
        this.timings = new HashMap<>();
        this.offdays = new ArrayList<>();
        Patient_Registry = new HashMap<>();

    }

    public void AddPatientToSchedule(String date,Patient patient) {
        try {
            if (Patient_Registry.containsKey(date) && Patient_Registry.get(date).size() < doctordailyLimit) {
                Patient_Registry.get(date).add(patient);
            }
            else {
                if (offdays.contains(date) || Patient_Registry.get(date).size() >= doctordailyLimit) {
                    System.out.println("Doctor is not available on this day");
                    return;
                } else {
                    Patient_Registry.put(date, new ArrayList<>());
                    Patient_Registry.get(date).add(patient);
                }
            }
            System.out.println("Patient Added to the schedule");
            System.out.println("Your appointment is on " + date + " with Dr." + name + "after " + timings.get(Patient_Registry.get(date).size() - 1) + "patients ");
            patient.UpcomingAppointments.put(date, this.doctorid);
        } catch (Exception e) {
        }
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

    public void printData() {
        // Print Doctor Details in a table-like format
        for(int i = 0;i<120;i++){
            System.out.print("-");
        }
        System.out.println();
        System.out.printf("| %-15s | %-25s | %-20s | %-15s | %-15s | %-15s | %-15s |%n",
                "Doctor ID", "Name", "Specialty", "Phone Number",
                "Per Patient Charge", "Clinic Share", "Amount Earned");
        for(int i = 0;i<120;i++){
            System.out.print("-");
        }
        System.out.println();
        System.out.printf("| %-15s | %-25s | %-20s | %-15s | %-15.2f | %-15.2f | %-15.2f |%n",
                doctorid, name, speaclity, emphno, perpatientcharge, clinicshare, earned);
        for(int i = 0;i<120;i++){
            System.out.print("-");
        }
    }

    public void setOffdays(String offday){
        this.offdays.add(offday);
    }
    public void Login(){
        this.isMedicOnStandby= false;
        this.isMedicAvaliable = true;
        System.out.println("Doctor has been logged in,");

    }
    public void Logout(){
        this.isMedicAvaliable = false;
        this.isMedicOnStandby = false;
        System.out.println("Logged out");
    }
    public void setChamberSchedule(HashMap<String, String> chamberSchedule) {
        this.chamberSchedule = chamberSchedule;
    }
}
