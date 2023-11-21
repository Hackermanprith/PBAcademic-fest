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
    HashMap<String,ArrayList<String>>MedicalHistory;
    HashMap<String,Integer> billable_services;
    public static String Takestrinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();

    }
    Patient(String addmitno,String admissiondate,String name,String phoneno,String bedid,String Adress,String Gurdian,String GurdianPhoneno,String email,ArrayList<String>MD){
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
        if(MD == null){
            return;
        }
        MedicalHistory.put("OLD",MD);
    }
    Patient(String addmitno,String admissiondate,String name,String phoneno,String bedid,String Adress,String Gurdian,String GurdianPhoneno,ArrayList<String>Complaint,String docid){
        this.addmitno = addmitno;
        this.admissiondate = admissiondate;
        this.bedid = null;
        patientdata = new ArrayList<String>();
        patientdata.add(name);
        patientdata.add(phoneno);
        patientdata.add(bedid);
        patientdata.add(Adress);
        patientdata.add(Gurdian);
        patientdata.add(GurdianPhoneno);
        UpcomingAppointments = new HashMap<>();
        MedicalHistory = new HashMap<>();
        billable_services = new HashMap<>();
        MedicalHistory.put("Complaints",Complaint);
        UpcomingAppointments.put("Emergency Call",docid);

    }
    public void printPatientDetails() {
        System.out.println("Patient Details");

        String format = "| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %n";

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
    public void addMedicalHistory(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the date of the medical history");
        String date = sc.nextLine();
        System.out.println("Enter the medical diagnosis");
        String history = sc.nextLine();
        System.out.println("Enter the medicines prescribed");
        String medicines = sc.nextLine();
        System.out.println("For any other details regarding the history just type it and press enter for the next line,for quitting type exit");
        ArrayList<String> temp = new ArrayList<>();
        temp.add(date);
        temp.add(history);
        temp.add(medicines);
        String type = "";
        while(type.trim().toLowerCase().equals("exit")){
            type = sc.nextLine();
            temp.add(type);
        }
        if(MedicalHistory.containsKey(date)){
            System.out.println("Medical history for this date already exists");
            System.out.println("Adding this to the existing history");
            MedicalHistory.get(date).get(0).concat(" & "+history);
            MedicalHistory.get(date).get(2).concat(medicines);
            MedicalHistory.get(date).addAll(temp.subList(3,temp.size()));

        }
        MedicalHistory.put(date,temp);
    }
    public void printMedicalHistory(){
        System.out.println("Medical History");
        if(MedicalHistory.isEmpty()){
            System.out.println("No medical history");
            return;
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-20s% | %-20s |","Date","Diagnosis");
        System.out.println("------------------------------------------");
        for(String i : MedicalHistory.keySet()){
            System.out.printf("%-20s% | ",i,MedicalHistory.get(i).get(1));
            for(int j = 3;j<MedicalHistory.get(i).size();j++){
                System.out.print(MedicalHistory.get(i).get(j)+" ");
            }
            System.out.print(" | ");
            System.out.println("------------------------------------------");
        }
    }

    public void modifyMedicalHistory() {
        printMedicalHistory();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the date of the medical history to be modified");
        String date = sc.nextLine();
        String ent = Takestrinp("Enter the field to be modified: ");
        switch (ent.trim().toLowerCase()){
            case "date":
                System.out.println("Enter the new date");
                String newdate = sc.nextLine();
                MedicalHistory.get(date).set(0,newdate);
                break;
            case "diagnosis":
                System.out.println("Enter the new diagnosis");
                String newdiagnosis = sc.nextLine();
                MedicalHistory.get(date).set(1,newdiagnosis);
                break;
            case "medicines":
                System.out.println("Enter the new medicines");
                String newmedicines = sc.nextLine();
                MedicalHistory.get(date).set(2,newmedicines);
                break;
            default:
                System.out.println("Enter the new details(old details are overwritten)");
                String newdetails = sc.nextLine();
                MedicalHistory.get(date).add(newdetails);
                break;
        }
    }
    public void removeMedicalHistory() {
        printMedicalHistory();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the date of the medical history to be removed");
        String date = sc.nextLine();
        MedicalHistory.remove(date);
    }
    public void setBedid(){

    }
}