package com.pmdev.pmacademic.java;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.*;

import static com.pmdev.pmacademic.java.Hospital.EPatient_Registry;

class Patient {
    String addmitno;
    String admissiondate;
    ArrayList<String> patientdata;
    Beds rm;
    String bedid;
    HashMap<String, String> UpcomingAppointments;
    HashMap<String, ArrayList<String>> MedicalHistory;
    HashMap<String, Integer> billable_services;
    HashMap<String, Integer> HospitalExpenses;
    boolean isEmergency;

    public Patient() {
        patientdata = new ArrayList<>();
        UpcomingAppointments = new HashMap<>();
        MedicalHistory = new HashMap<>();
        billable_services = new HashMap<>();
        HospitalExpenses = new HashMap<>();

    }
    public static String Takestrinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();

    }

    Patient(String addmitno, String admissiondate, String name, String phoneno, String bedid, String Adress, String Gurdian, String GurdianPhoneno, String email, ArrayList<String> MD) {
        this.addmitno = addmitno;
        this.admissiondate = admissiondate;
        this.rm = null;
        this.bedid = bedid;
        patientdata = new ArrayList<String>();
        patientdata.add(name);
        patientdata.add(phoneno);
        patientdata.add(Adress);
        patientdata.add(email);
        patientdata.add(Gurdian);
        patientdata.add(GurdianPhoneno);
        UpcomingAppointments = new HashMap<>();
        MedicalHistory = new HashMap<>();
        billable_services = new HashMap<>();
        if (MD == null) {
            return;
        }
        MedicalHistory.put("OLD", MD);
        this.isEmergency = false;
    }

    Patient(String addmitno, String admissiondate, String name, String phoneno, String bedid, Beds bed, String Adress, String Gurdian, String GurdianPhoneno, ArrayList<String> Complaint, String docid) {
        this.addmitno = addmitno;
        this.admissiondate = admissiondate;
        this.rm = bed;
        patientdata = new ArrayList<String>();
        patientdata.add(name);
        patientdata.add(phoneno);
        patientdata.add(Adress);
        patientdata.add("emergencycase@hospital.com");
        patientdata.add(Gurdian);
        patientdata.add(GurdianPhoneno);
        UpcomingAppointments = new HashMap<>();
        MedicalHistory = new HashMap<>();
        billable_services = new HashMap<>();
        MedicalHistory.put("Complaints", Complaint);
        UpcomingAppointments.put("Emergency Call", docid);
        this.isEmergency = true;

    }

    public void printPatientDetails() {
        System.out.println("Patient Details");
        for(int i = 0;i<= 200;i++){
            System.out.print("-");
        }
        String format = "| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %n";
        System.out.println();
        System.out.println();
        System.out.format(format,
                addmitno,
                patientdata.get(0),
                patientdata.get(1),
                patientdata.get(2),
                this.bedid,
                patientdata.get(3),
                patientdata.get(4),
                patientdata.get(5),
                MedicalHistory,
                billable_services);
        for(int i = 0;i<= 200;i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public void removeAppointment(String date) {
        UpcomingAppointments.remove(date);
    }

    public String getAppointment(String date) {
        return UpcomingAppointments.get(date);
    }
    public void printBillableServices() {
        System.out.println("Billable Services");
        String format = "| %-20s | %-20s |";
        System.out.println("------------------------------------------");
        System.out.format(format, "Service ID", "Service Name", "Service Cost");
        System.out.println("------------------------------------------");
        for (String i : billable_services.keySet()) {
            System.out.format(format, i, i.split("_")[0], billable_services.get(i));
            System.out.println("------------------------------------------");
        }
    }

    public void addMedicalHistory() {
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
        while (type.trim().toLowerCase().equals("exit")) {
            type = sc.nextLine();
            temp.add(type);
        }
        if (MedicalHistory.containsKey(date)) {
            System.out.println("Medical history for this date already exists");
            System.out.println("Adding this to the existing history");
            MedicalHistory.get(date).get(0).concat(" & " + history);
            MedicalHistory.get(date).get(2).concat(medicines);
            MedicalHistory.get(date).addAll(temp.subList(3, temp.size()));

        }
        MedicalHistory.put(date, temp);
    }

    public void printMedicalHistory() {
        System.out.println("Medical History");
        if (MedicalHistory.isEmpty()) {
            System.out.println("No medical history");
            return;
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-20s% | %-20s |", "Date", "Diagnosis");
        System.out.println("------------------------------------------");
        for (String i : MedicalHistory.keySet()) {
            System.out.printf("%-20s% | ", i, MedicalHistory.get(i).get(1));
            for (int j = 3; j < MedicalHistory.get(i).size(); j++) {
                System.out.print(MedicalHistory.get(i).get(j) + " ");
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
        switch (ent.trim().toLowerCase()) {
            case "date":
                System.out.println("Enter the new date");
                String newdate = sc.nextLine();
                MedicalHistory.get(date).set(0, newdate);
                break;
            case "diagnosis":
                System.out.println("Enter the new diagnosis");
                String newdiagnosis = sc.nextLine();
                MedicalHistory.get(date).set(1, newdiagnosis);
                break;
            case "medicines":
                System.out.println("Enter the new medicines");
                String newmedicines = sc.nextLine();
                MedicalHistory.get(date).set(2, newmedicines);
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

    public void TransferBed(String bedid, Beds bed) {
        rm.occupant = "none";
        AddHalfDaycharge();
        bed.occupant = addmitno;
        this.bedid = bedid;
        AddHalfDaycharge();


    }

    public void Billoutpatient() {
        printPatientDetails();
        printBillableServices();
        int bill = getBill();
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Total Bill: " + bill + "and the hospital cost will be " + getHospitalCost());
        System.out.println("-------------------------------------------------------------------------------");
    }

    public int getBill() {
        int bill = 0;
        for (int i : billable_services.values()) {
            bill += i;
        }
        return bill;
    }
    public int getHospitalCost() {
        int cost = 0;
        for (int i : HospitalExpenses.values()) {
            cost += i;
        }
        return cost;
    }
    void AddDailyBedCharge() {
        billable_services.put("Bed Cost for bed id" + bedid, (rm.bed_cost));

    }
    void AddHalfDaycharge() {
        billable_services.put("Bed Cost for bed id" + bedid, (rm.bed_cost / 2));
    }

}
