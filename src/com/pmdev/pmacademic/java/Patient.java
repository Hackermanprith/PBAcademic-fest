package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Patient{
    String addmitno;
    String admissiondate;
    ArrayList<String>patientdata;
    String bedid;
    HashMap<String,String> UpcomingAppointments;
    HashMap<String,Integer> Medicines;
    HashMap<String,ArrayList<String>>MedicalHistory;
    HashMap<String,Integer> billable_services;
    Patient(String addmitno,String admissiondate,String name,String phoneno,String bedid,String Adress,String Gurdian,String GurdianPhoneno,String email){
        this.addmitno = addmitno;
        this.admissiondate = admissiondate;
        this.bedid = null;
        patientdata = new ArrayList<String>();
        patientdata.add(name);
        patientdata.add(phoneno);
        patientdata.add(bedid);
        patientdata.add(Adress);
        patientdata.add(email);
        patientdata.add(Gurdian);
        patientdata.add(GurdianPhoneno);
        UpcomingAppointments = new HashMap<>();
        MedicalHistory = new HashMap<>();
        billable_services = new HashMap<>();

    }

    public void printPatientDetails() {
        System.out.println("Patient Details");

        String format = "| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %n";

        System.out.format(format,"Admission no", "Name", "Phone No", "Bed No", "Address", "Email", "Guardian", "Guardian Phone No", "Medicines", "Medical History", "Billable Services","Billed amount");

        System.out.format(format,
                addmitno,
                patientdata.get(0),
                patientdata.get(1),
                patientdata.get(2),
                patientdata.get(3),
                patientdata.get(4),
                patientdata.get(5),
                patientdata.get(6),
                Medicines,
                MedicalHistory,
                billable_services);
    }


    public void removeAppointment(String date){
        UpcomingAppointments.remove(date);
    }
    public String getAppointment(String date){
        return UpcomingAppointments.get(date);
    }
    public int getBill(){
        int bill = 0;
        for(int i : billable_services.values()){
            bill+=i;
        }
        return bill;
    }
    public void printBillableServices(){
        System.out.println("Billable Services");
        String format = "| %-20s | %-20s |";
        System.out.println("------------------------------------------");
        System.out.format(format,"Service ID","Service Name","Service Cost");
        System.out.println("------------------------------------------");
        for(String i : billable_services.keySet()){
            System.out.format(format,i,i.split("_")[0],billable_services.get(i));
            System.out.println("------------------------------------------");
        }
    }


}