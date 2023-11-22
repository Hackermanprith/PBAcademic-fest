package com.pmdev.pmacademic.java;

import javax.crypto.SecretKey;
import java.util.*;

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
    }

    Patient(String addmitno, String admissiondate, String name, String phoneno, String bedid, Beds bed, String Adress, String Gurdian, String GurdianPhoneno, ArrayList<String> Complaint, String docid) {
        this.addmitno = addmitno;
        this.admissiondate = admissiondate;
        this.rm = bed;
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
        MedicalHistory.put("Complaints", Complaint);
        UpcomingAppointments.put("Emergency Call", docid);

    }

    public void printPatientDetails() {
        System.out.println("Patient Details");

        String format = "| %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s | %n";

        System.out.format(format, "Admission no", "Name", "Phone No", "Bed No", "Address", "Email", "Guardian", "Guardian Phone No", "Medicines", "Medical History", "Billable Services", "Billed amount");

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
        System.out.println("%-200-");
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

    public void exportDataToFile(String filename) {
        try {
            // Prepare patient data as a string
            StringBuilder patientDataString = new StringBuilder();
            patientDataString.append("Admission No: ").append(addmitno).append("\n");
            patientDataString.append("Admission Date: ").append(admissiondate).append("\n");
            patientDataString.append("Name: ").append(patientdata.get(0)).append("\n");
            patientDataString.append("Phone No: ").append(patientdata.get(1)).append("\n");
            patientDataString.append("Address: ").append(patientdata.get(2)).append("\n");
            patientDataString.append("Email: ").append(patientdata.get(3)).append("\n");
            patientDataString.append("bedid: ").append(patientdata.get(4)).append("\n");
            patientDataString.append("Gurdian: ").append(patientdata.get(5)).append("\n");
            patientDataString.append("Gurdianphoneno: ").append(patientdata.get(6)).append("\n");

            // Export Medical History
            patientDataString.append("Medical History:\n");
            for (String date : MedicalHistory.keySet()) {
                patientDataString.append("\tDate: ").append(date)
                        .append(", Diagnosis: ").append(MedicalHistory.get(date).get(1)).append("\n");
                // Add more details if needed
            }
            // Export Billable Services
            patientDataString.append("Billable Services:\n");
            for (Map.Entry<String, Integer> entry : billable_services.entrySet()) {
                patientDataString.append("\tService: ").append(entry.getKey())
                        .append(", Cost: ").append(entry.getValue()).append("\n");
            }

            // Export Upcoming Appointments
            patientDataString.append("Upcoming Appointments:\n");
            for (Map.Entry<String, String> entry : UpcomingAppointments.entrySet()) {
                patientDataString.append("\tDate: ").append(entry.getKey())
                        .append(", Doctor: ").append(entry.getValue()).append("\n");
            }

            // Export Hospital Expenses
            patientDataString.append("Hospital Expenses:\n");
            for (Map.Entry<String, Integer> entry : HospitalExpenses.entrySet()) {
                patientDataString.append("\tExpense: ").append(entry.getKey())
                        .append(", Cost: ").append(entry.getValue()).append("\n");
            }

            // Convert patient data to bytes
            byte[] patientDataBytes = patientDataString.toString().getBytes();

            // Generate secret key
            SecretKey secretKey = DataExporter.generateSecretKey();

            // Encrypt patient data
            byte[] encryptedData = DataExporter.encrypt(Base64.getEncoder().encodeToString(patientDataBytes), secretKey);

            // Save encrypted data to a file
            DataExporter.saveToFile(encryptedData, filename);

            System.out.println("Data exported and encrypted successfully to file: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    public static Patient importPatientFromData(HashMap<String, ArrayList<String>> importedData) {
        try {
            // Extract relevant information from the imported data
            String admissionNo = importedData.get("Admission No").get(0);
            String admissionDate = importedData.get("Admission Date").get(0);
            String name = importedData.get("Name").get(0);
            String phoneNo = importedData.get("Phone No").get(0);
            String address = importedData.get("Address").get(0);
            String email = importedData.get("Email").get(0);
            String gurdian = importedData.get("Gurdian").get(0);
            String gurdianphno = importedData.get("Gurdianphoneno").get(0);
            String bedid = importedData.get("bedid").get(0);
            HashMap<String,ArrayList<String>> medicalHistoryData = new HashMap<>();
            // Medical History
            ArrayList <String>arr = new ArrayList<>();
            ArrayList<String> dummyarraylist = new ArrayList<>();
            int counter = 0;
            String previoustext = "";
            for(String text : arr){
                if(counter == 2){

                }

                previoustext = text;
                medicalHistoryData.put(previoustext,dummyarraylist);
            }


            Patient importedPatient = new Patient(admissionNo, admissionDate, name,phoneNo, bedid,address,gurdian,gurdianphno,email,medicalHistoryData);

            // Billable Services
            if (importedData.containsKey("Billable Services")) {
                // Assuming that "Billable Services" is a serialized HashMap in the imported data
                HashMap<String, String> billableServicesData = DataLoader.reconstructHashMap(importedData.get("Billable Services"));
                // Process and set billable services data in the importedPatient
                // ...

                // Example: importedPatient.billable_services = billableServicesData;
            }

            // Upcoming Appointments
            if (importedData.containsKey("Upcoming Appointments")) {
                // Assuming that "Upcoming Appointments" is a serialized HashMap in the imported data
                HashMap<String, String> upcomingAppointmentsData = DataLoader.reconstructHashMap(importedData.get("Upcoming Appointments"));
                // Process and set upcoming appointments data in the importedPatient
                // ...

                // Example: importedPatient.UpcomingAppointments = upcomingAppointmentsData;
            }

            // Hospital Expenses
            if (importedData.containsKey("Hospital Expenses")) {
                // Assuming that "Hospital Expenses" is a serialized HashMap in the imported data
                HashMap<String, String> hospitalExpensesData = DataLoader.reconstructHashMap(importedData.get("Hospital Expenses"));
                // Process and set hospital expenses data in the importedPatient
                // ...

                // Example: importedPatient.HospitalExpenses = hospitalExpensesData;
            }

            return importedPatient;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String getValue(HashMap<String, ArrayList<String>> data, String key) {
        ArrayList<String> dataList = data.get(key);
        return (dataList != null && dataList.size() > 0) ? dataList.get(0) : null;
    }

     */
}
