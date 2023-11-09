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
    HashMap<String,String> timings;
    HashMap<String,String> ot;
    double earned = 0.0;
    double clinicshare = 0.0;

    double perpatientcharge = 1000.0;
    int doctordailyLimit = 10;
    ArrayList<String> data;
    HashMap<String, ArrayList<Patient>> Patient_Registry;
    HashMap<String,ArrayList<Patient>> patient_ot;


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
    public void AddOT(Patient patient, String date, String otDetails){
        if(Patient_Registry.containsKey(date)){
            if(patient_ot.containsKey(date)){
                patient_ot.get(date).add(patient);
                ot.put(date, otDetails);
                System.out.println("OT details added for patient on "+date);
            } else {
                ArrayList<Patient> patientsList = new ArrayList<>();
                patientsList.add(patient);
                patient_ot.put(date, patientsList);
                ot.put(date, otDetails);
                System.out.println("OT details added for patient on "+date);
            }
        }
        else{
            System.out.println("Doctor is not available on this day");
        }
    }
    public void setPerpatientcharge(){
        perpatientcharge = Takeintinp("Set the per patient charge: ");
        clinicshare = Takeintinp("Set the clinic share: ");

    }

}
