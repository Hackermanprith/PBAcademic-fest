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
    public void AddPatientToSchedule(String date,Patient patient){
        try {
            if (Patient_Registry.containsKey(date) && Patient_Registry.get(date).size() < doctordailyLimit) {
                Patient_Registry.get(date).add(patient);
            } else {
                if (!(Patient_Registry.containsKey(date))) {
                    // System.out.println("Doctor is not available on this day");
                }
            }
            System.out.println("Patient Added to the schedule");
            System.out.println("Your appointment is on " + date + " with Dr." + name);
            patient.UpcomingAppointments.put(date, this.doctorid);
        }
        catch (Exception e){
            System.out.println("There is a error ");
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
        System.out.printf("%-15s%-25s%-20s%-15s%-15s%-15s%-15s%n",
                "Doctor ID", "Name", "Specialty", "Phone Number",
                "Per Patient Charge", "Clinic Share", "Amount Earned");

        System.out.printf("%-15s%-25s%-20s%-15s%-15.2f%-15.2f%-15.2f%n",
                doctorid, name, speaclity, emphno,perpatientcharge ,clinicshare, earned);
    }
    public void setOffdays(String offday){
        this.offdays.add(offday);
    }

}
