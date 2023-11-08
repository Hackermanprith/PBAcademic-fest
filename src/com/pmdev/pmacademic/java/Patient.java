package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

class Patient{
    String addmitno;
    String admissiondate;
    ArrayList<String>patientdata;
    String bedid;
    HashMap<String, String> UpcomingAppointments;
    HashMap<String,Integer> Medicines;
    HashMap<String,Integer>LabTests;
    HashMap<String,String>MedicalHistory;
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
        Medicines = new HashMap<String,Integer>();
        LabTests = new HashMap<String,Integer>();
        MedicalHistory = new HashMap<String,String>();
        billable_services = new HashMap<String,Integer>();

    }
    public void addMedication(String medicationName, int quantity) {
        Medicines.put(medicationName, quantity);
    }

    public void addLabTest(String testName, int cost) {
        LabTests.put(testName, cost);
    }

    public void addMedicalHistory(String date, String history) {
        MedicalHistory.put(date, history);
    }

    public void addBillableService(String serviceName, int cost) {
        billable_services.put(serviceName, cost);
    }
    public void printPatientDetails(){
        System.out.println("Patient Details");
        System.out.println("Name: "+patientdata.get(0));
        System.out.println("Phone No: "+patientdata.get(1));
        System.out.println("Bed No: "+patientdata.get(2));
        System.out.println("Address: "+patientdata.get(3));
        System.out.println("Email: "+patientdata.get(4));
        System.out.println("Gurdian: "+patientdata.get(5));
        System.out.println("Gurdian Phone No: "+patientdata.get(6));
        System.out.println("Medicines: "+Medicines);
        System.out.println("Lab Tests: "+LabTests);
        System.out.println("Medical History: "+MedicalHistory);
        System.out.println("Billable Services: "+billable_services);
    }
    public void Bill(){
        double total = 0;
        for (String key : Medicines.keySet()) {
            total += Medicines.get(key);
        }
        for (String key : LabTests.keySet()) {
            total += LabTests.get(key);
        }
        for (String key : billable_services.keySet()) {
            total += billable_services.get(key);
        }
        System.out.println("Total Bill: "+total);
    }
    public void AssignABed(String bedid,Rooms room){
        this.bedid = bedid;
        room.occupant.get(bedid).occupant = this.addmitno;
        room.occupant.get(bedid).bedid = bedid;
        //change the value of bed

    }

}