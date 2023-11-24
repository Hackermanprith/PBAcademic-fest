package com.pmdev.pmacademic.java;

import org.jetbrains.annotations.Nullable;

import javax.print.Doc;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SignedObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.pmdev.pmacademic.java.DataLoader.generateSecretKey;

public class Hospital {
    static HashMap<String, ArrayList<Rooms>> Floorplan;
    static HashMap<String, Patient> Patient_Registry;
    static HashMap<String, Patient> EPatient_Registry;
    static HashMap<String, Doctor> EMT_Registry;
    HashMap<String, Doctor> Doctor_Registry;
    static Map<String, ArrayList<String>> doctorSchedules;
    ArrayList<Chamber> Chambers;

    HashMap<String, Staff> Staff_Registry;
    String clinic_managementSystem = "New Clinic";
    Financials financials;
    Databasedb databaseE;
    DataExporter dex;
    DataLoader dll;
    int totalchambersbooked = 0;
    public void printPatientDataHeader() {
        System.out.println("=== Patient Data Header ===");
        System.out.printf("%-15s | %-15s | %-25s | %-15s | %-25s | %-25s | %-25s | %-20s | %-10s | %-13s\n",
                "Admission No", "Admission Date", "Name", "Phone No", "Address", "Email", "Guardian", "Guardian Phone No", "Bed ID", "Is Emergency");
        System.out.println("===========================");
    }

Hospital() throws NoSuchAlgorithmException {
        databaseE = new Databasedb();
        databaseE.loadUserData();
        Patient_Registry = new HashMap<>();
        Doctor_Registry = new HashMap<>();
        Staff_Registry = new HashMap<>();
        Chambers = new ArrayList<>();
        Floorplan = new HashMap<>();
        EPatient_Registry = new HashMap<>();
        doctorSchedules = new HashMap<>();
        EMT_Registry = new HashMap<>();
        financials = new Financials();
        dll = new DataLoader();
        dex = new DataExporter();
        String str = Takestrinp("Do you want to load from previous session: ");
        if (str.trim().toLowerCase().equals("y")&& Files.exists(Paths.get("Data"+ File.separator+"all_doctors_data.txt"))){
            header("Log in");
            String username = Takestrinp("Enter the admin username: ");
            String password = Takestrinp("Enter the password: ");
            if (databaseE.loginUser(username, password)) {
                System.out.println("Successfully logged in");
            } else {
                System.out.println("Wrong info entered , please try again(last try)");
                username = Takestrinp("Enter the admin username: ");
                password = Takestrinp("Enter the password: ");
                if (databaseE.loginUser(username, password)) {
                    System.out.println("Successfully logged in");
                } else {
                    System.out.println("Exiting for wrong password input.");
                    return;
                }

            }
            clinic_managementSystem = dll.loadClinicName();
            ArrayList<Doctor> doc = dll.importAllDoctorsData("Data"+ File.separator+"all_doctors_data.txt",generateSecretKey());
            for (Doctor doctor : doc) {
                if(doctor.doctorid == null){
                    continue;
                }
                Doctor_Registry.put(doctor.doctorid, doctor);
                if(doctor.isMedicOnStandby){
                    EMT_Registry.put(doctor.doctorid,doctor);
                }
            }
           ArrayList<Patient> pat = dll.importAllPatients("Data"+ File.separator+"Patients.txt");
            for (Patient patient : pat) {
                if(patient.isEmergency){
                    EPatient_Registry.put(patient.addmitno,patient);
                    continue;
                }
                Patient_Registry.put(patient.addmitno, patient);
            }
            ArrayList<Staff>staff = dll.importAllStaffs();
            for(Staff staffu : staff){
                Staff_Registry.put(staffu.staffid,staffu);
            }
            financials.Ins = dll.decryptHmapIncome();
            financials.Expenses = dll.decryptHmapExpenses();
            Floorplan = dll.importFloorplanData("Floorplan");
            doctorSchedules = dll.importDoctorSchedules("DoctorSchedules.txt");

            mainMeu();
        } else {
            if(!Files.exists(Paths.get("Data"+ File.separator+"all_doctors_data.txt"))){
                System.out.println("No previous data found");
            }
            header("Clinic Management System ");
            String username = Takestrinp("Please enter a username(remember you will login first time using this): ");
            String password = Takestrinp("Please enter a password for " + username + " :");
            databaseE.registerUser(username, password);
            databaseE.saveUserData();
            clinic_managementSystem = Takestrinp("Enter the name of the clinic: ");
            //save the clinic name to a file
            dll.saveClinicName(clinic_managementSystem);
            int nooffloors = Takeintinp("Enter the number of floors(G+1): ");
            floorPlannerMenu(nooffloors, false);
            if(totalchambersbooked==0){
                System.out.println("Atleast add 1 chamber");
                int nooffloors1 = Takeintinp("Enter the floor on which you want to add Chamber (g+1): ");
                if(nooffloors1>nooffloors){
                    System.out.println("Invalid floor");
                    nooffloors1 = Takeintinp("Please enter the floor on which you want to add Chamber: ");
                    if(nooffloors1 > nooffloors){
                        System.out.println("Sorry invalid floor");
                        return;
                    }
                }
                addChamber(nooffloors1);
            }
            System.out.print("\u000c");
            System.out.println("Please enter at least 1 doc");
            RegisterDoctor();
            System.out.println("Please enter at least 1 staff");
            RegisterStaff();
            mainMeu();
        }

    }
    public static int Takeintinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        int number;
        try {
            number = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid Input, Try again");
            number = Takeintinp(msg);
        }
        System.out.println();
        return number;
    }
    public static String Takestrinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();

    }
    public String takesphinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        String number;
        number = sc.next();
        System.out.println();
        if(number.trim().equals("none")){
            return number;
        }
        if (number.length() != 10) {
            number = takesphinp("Invalid Input, Try again, please enter a 11 digit number: ");
        }
        return number;
    }
    private void printCalendar(Doctor docid) {
        int year = Takeintinp("Enter the year: ");
        int month = Takeintinp("Enter the month: ");

        do {
            int day = 1;
            int currentMonth = 1;
            int currentYear = 1;
            int dayOfWeek = 1;

            String[] daysOfWeek = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
            String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

            int[] daysInMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            while (true) {
                if (day == 1 && currentMonth == month && currentYear == year) {
                    break;
                }

                if (currentYear % 4 == 0 && currentYear % 100 != 0 || currentYear % 100 == 0) {
                    daysInMonth[1] = 29;
                } else {
                    daysInMonth[1] = 28;
                }
                dayOfWeek++;
                day++;

                if (day > daysInMonth[currentMonth - 1]) {
                    currentMonth++;
                    day = 1;
                }

                if (currentMonth > 12) {
                    currentMonth = 1;
                    currentYear++;
                }

                if (dayOfWeek == 7) {
                    dayOfWeek = 0;
                }
            }

            int spaces = dayOfWeek;
            if (spaces < 0) {
                spaces = 6;
            }

            System.out.println("MONTH:" + months[month - 1]);

            for (int k = 0; k < 7; k++) {
                System.out.print("   " + daysOfWeek[k]);
            }

            System.out.println();

            for (int j = 1; j <= (daysInMonth[month - 1] + dayOfWeek); j++) {
                if (j > 6) {
                    dayOfWeek = dayOfWeek % 6;
                }
            }

            // Printing the calendar
            for (int i = 0; i < spaces; i++) {
                System.out.print("      ");
            }

            for (int i = 1; i <= daysInMonth[month - 1]; i++) {
                if (i == day && month == currentMonth && year == currentYear) {
                    System.out.printf("\u001B[31m   %2d", i);
                } else {
                    try {
                        if (docid.doctordailyLimit + 1 > docid.Patient_Registry.get(i).size() || docid.doctordailyLimit == 0 || docid.offdays.contains(i + "/" + month + "/" + year) || doctorSchedules.get(docid.doctorid).get(i - 1).equals("0,0")) {
                            System.out.printf("\u001B[32m    %2d", i);
                        } else {
                            System.out.printf("\u001B[31m    %2d", i);
                        }
                    } catch (Exception e) {
                        System.out.printf("\u001B[31m        %2d", i);
                    }
                }

                if (((i + spaces) % 7 == 0) || (i == daysInMonth[month - 1])) {
                    System.out.println();
                }
            }
            System.out.printf("\u001B[0m");
            System.out.print("Do you want to change the month or year (Y/N): ");
            String response = Takestrinp("-> ");
            if (response.trim().toLowerCase().equals("n")) {
                break;
            }

            year = Takeintinp("Enter the year: ");
            month = Takeintinp("Enter the month: ");
        } while (true);
    }
    private void PrintSchedule(String docid) {

        Doctor doc = findDoctorById(docid);
        if (doc == null) {
            System.out.println("Doctor not found");
            return;
        }
        System.out.println("Printing Calendar for Dr." + doc.name);
        printCalendar(doc);
        System.out.println("Showing timings for Dr." + doc.name);

        System.out.println("----------------------------------------------------------------");
        String formatSpecifier = "| %-9s | %-20s | %-20s |";
        System.out.printf(formatSpecifier, "Day", "Shift 1", "Shift 2");
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        for (int i = 1; i <= 7; i++) {
            switch (i) {
                case 1:
                    System.out.println();
                    System.out.printf(formatSpecifier, "Monday ", getShift(doctorSchedules.get(docid).get(0).split(",")[0]), getShift(doctorSchedules.get(docid).get(0).split(",")[1]));
                    System.out.println();
                    break;
                case 2:
                    System.out.printf(formatSpecifier, "Tuesday", getShift(doctorSchedules.get(docid).get(1).split(",")[0]), getShift(doctorSchedules.get(docid).get(1).split(",")[1]));
                    System.out.println();
                    break;
                case 3:
                    System.out.printf(formatSpecifier, "Wednesday", getShift(doctorSchedules.get(docid).get(2).split(",")[0]), getShift(doctorSchedules.get(docid).get(2).split(",")[1]));
                    System.out.println();
                    break;
                case 4:
                    System.out.printf(formatSpecifier, "Thursday", getShift(doctorSchedules.get(docid).get(3).split(",")[0]), getShift(doctorSchedules.get(docid).get(3).split(",")[1]));
                    System.out.println();
                    break;
                case 5:
                    System.out.printf(formatSpecifier, "Friday", getShift(doctorSchedules.get(docid).get(4).split(",")[0]), getShift(doctorSchedules.get(docid).get(4).split(",")[1]));
                    System.out.println();
                    break;
                case 6:
                    System.out.printf(formatSpecifier, "Saturday", getShift(doctorSchedules.get(docid).get(5).split(",")[0]), getShift(doctorSchedules.get(docid).get(5).split(",")[1]));
                    System.out.println();
                    break;
                case 7:
                    System.out.printf(formatSpecifier, "Sunday: ", getShift(doctorSchedules.get(docid).get(6).split(",")[0]), getShift(doctorSchedules.get(docid).get(6).split(",")[1]));
                    System.out.println();
                    break;
            }
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println("Do you want to see the calendar again or just select the date (Y/N): ");
        String n = Takestrinp("-> ");
        if (n.trim().toLowerCase().equals("y")) {
            printCalendar(doc);
        }
    }
    private void EmergencyMenu() {
        int n = 0;
        do {
            System.out.print("\u000c");
            System.out.flush();
            header(" Emergency Patient Functions(Experimental) ");
            System.out.println("1. Emergency Patient Admission");
            System.out.println("2. Emergency Patient Diagnosis");
            System.out.println("4. Emergency Bill Out Patient");
            System.out.println("5. Shift Patient from Emergency to Normal");
            System.out.println("6. Change patient bed");
            System.out.println("7. Transfer Patient to another hospital");
            System.out.println("8. Exit");
            n = Takeintinp("-> ");
            switch (n) {
                case 1:
                    EmergencyAdmission();
                    break;
                case 2:
                    EmergencyPatientDiagnosis();
                    break;
                case 3:
                    System.out.println("\u000c");
                    System.out.println("Patient details");
                    for (Patient pat : EPatient_Registry.values()) {
                        pat.printPatientDetails();
                    }
                    break;
                case 4:
                    System.out.println("\u000c");
                    System.out.println("Billing out patient: ");
                    for(int i = 1;i<=200;i++){
                        System.out.print("-");
                    }
                    for (Patient pat : EPatient_Registry.values()) {
                        pat.printPatientDetails();
                    }
                    for(int i = 1;i<=200;i++){
                        System.out.print("-");
                    }
                    String name = Takestrinp("Enter the name of the patient: ");
                    String phoneno = takesphinp("Enter the phone no of the patient: ");
                    String addmitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
                    if (validatePatient(name, phoneno)) {
                        System.out.println("No such patient found");
                        return;
                    }
                    Patient pat = EPatient_Registry.get(addmitno);
                    System.out.println("Billing out patient: " + pat.patientdata.get(0));
                    System.out.println("Do you want to bill out the patient Y/N: ");
                    String yn = Takestrinp("-> ");
                    if (yn.trim().toLowerCase().equals("y")) {
                        pat.Billoutpatient();
                        financials.addIncome(String.valueOf("Doctors visit by" + pat.addmitno), pat.getBill(), (pat.getBill() - pat.getHospitalCost()), true);
                    } else {
                        System.out.println("Patient not billed out");
                    }
                    break;
                case 5:
                    System.out.println("\u000c");
                    System.out.println("Shifting patient from emergency to normal: ");
                    for(int i =0;i<200;i++){
                        System.out.print("-");
                    }
                    System.out.println();
                    for (Patient pat5 : EPatient_Registry.values()) {
                        pat5.printPatientDetails();
                    }
                    for(int i =0;i<200;i++){
                        System.out.print("-");
                    }
                    System.out.println();
                    String name1 = Takestrinp("Enter the name of the patient: ");
                    String phoneno1 = takesphinp("Enter the phone no of the patient: ");
                    String addmitno1 = name1.substring(0).trim() + "_" + phoneno1.substring(6, 9).trim();
                    if (validatePatient(name1, phoneno1)) {
                        System.out.println("No such patient found");
                        return;
                    }
                    Patient pat1 = EPatient_Registry.get(addmitno1);
                    System.out.println("Shifting patient from emergency to normal: " + pat1.patientdata.get(0));
                    System.out.println("Do you want to shift the patient Y/N: ");
                    String yn1 = Takestrinp("-> ");
                    if (yn1.trim().toLowerCase().equals("y")) {
                        System.out.println("Assigning doctor");
                        printDocChart();
                        String docid = Takestrinp("Enter the doctor id: ");
                        if (!Doctor_Registry.containsKey(docid)) {
                            System.out.println("Doctor not found");
                            return;
                        }
                        pat1.patientdata.set(2, docid);
                        ScheduleAppointment("Emergency", pat1, docid);
                        EPatient_Registry.remove(addmitno1);
                        Patient_Registry.put(addmitno1, pat1);
                    } else {
                        System.out.println("Patient not shifted");
                    }
                    break;
                case 6:
                    System.out.println("\u000c");
                    System.out.println("Changing patient bed: ");
                    for(int i =0;i<200;i++){
                        System.out.print("-");
                    }
                    System.out.println();
                    for (Patient pat8 : EPatient_Registry.values()) {
                        pat8.printPatientDetails();
                    }
                    for(int i =0;i<200;i++){
                        System.out.print("-");
                    }
                    System.out.println();
                    name = Takestrinp("Enter the name of the patient: ");
                    phoneno = takesphinp("Enter the phone no of the patient: ");
                    addmitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
                    if (validatePatient(name, phoneno)) {
                        System.out.println("No such patient found");
                        return;
                    }
                    pat = EPatient_Registry.get(addmitno);
                    if(pat.bedid == null){
                        System.out.println("Patient is not in bed, cannot change bed");
                        return;
                    }
                    System.out.println("Changing patient bed: " + pat.patientdata.get(0));
                    System.out.println("Do you want to change the patient bed Y/N: ");
                    yn = Takestrinp("-> ");
                    if (yn.trim().toLowerCase().equals("y")) {
                        System.out.println("Assigning bed");
                        printFreeBeds();
                        String bedid = Takestrinp("Enter the bed id: ");
                        if (!bedid.contains("bed")) {
                            System.out.println("Invalid bed id");
                            return;
                        }
                        String roomid = bedid.split("_")[0];
                        roomid = roomid.substring(0,roomid.length()-1);
                        String bedtype = bedid.split("_")[2];
                        bedtype = bedtype.substring(1,bedtype.length()-1);


                        Rooms room = null;
                        for (String floorKey : Floorplan.keySet()) {
                            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
                            for (Rooms room1 : roomsOnFloor) {
                                if (room1.roomid.equals(roomid)) {
                                    room = room1;
                                    break;
                                }
                            }
                        }
                        if (room == null) {
                            System.out.println("Room not found");
                            return;
                        }
                        Beds bed = room.Bedsinroom.get(bedtype);
                        if (bed == null) {
                            System.out.println("Bed not found");
                            return;
                        }
                        if (bed.occupant != null) {
                            System.out.println("Bed already occupied");
                            return;
                        }
                        pat.TransferBed(bedid,bed);
                    } else {
                        System.out.println("Patient bed not changed");
                    }
                    break;
                case 7:
                    System.out.println("\u000c");
                    System.out.println("Transfering patient to another hospital: ");
                    System.out.println("%-200-");
                    for (Patient pat9 : EPatient_Registry.values()) {
                        pat9.printPatientDetails();
                    }
                    System.out.println("%-200-");
                    name = Takestrinp("Enter the name of the patient: ");
                    phoneno = takesphinp("Enter the phone no of the patient: ");
                    addmitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
                    if (validatePatient(name, phoneno)) {
                        System.out.println("No such patient found");
                        return;
                    }
                    pat = EPatient_Registry.get(addmitno);
                    System.out.println("Transfering patient to another hospital: " + pat.patientdata.get(0));
                    System.out.println("Do you want to transfer the patient Y/N: ");
                    yn = Takestrinp("-> ");
                    if (yn.trim().toLowerCase().equals("y")) {
                        System.out.println("Do you want to immedietly bill out patient Y/N: ");
                        String yn4 = Takestrinp("-> ");
                        if (yn4.trim().toLowerCase().equals("y")) {
                            System.out.println("Billing out patient: ");
                            pat.Billoutpatient();
                            financials.addIncome(String.valueOf("Doctors visit by" + pat.addmitno), pat.getBill(), (pat.getBill() - pat.getHospitalCost()), true);
                        } else {
                            System.out.println("Patient not billed out");
                        }
                        pat.printPatientDetails();
                        pat.printMedicalHistory();
                        EPatient_Registry.remove(addmitno);
                    } else {
                        System.out.println("Patient not transferred");
                    }

                    break;
                case 8:
                    return;
                default:
                    System.out.println("Enter a number between 1-8");

            }

        }
        while (n != 8);

    }
    private String getShift(String scheduleValue) {
        return scheduleValue.equals("0") ? "Off" : scheduleValue;
    }
    public void header(String menuname) {
        String text = "Welcome to the " + menuname + "of " + clinic_managementSystem + " \n by Methodist School Dankuni";

        try {
            int terminalWidth = 16;

            int leftPadding = (terminalWidth - text.length()) / 2;
            String centeredText = String.format("%" + (leftPadding + text.length()) + "s", text);

            System.out.println(centeredText);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void printFloorPlan() {
        for (String floorKey : Floorplan.keySet()) {
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            String[] floorInfo = floorKey.split("_");
            int floor = Integer.parseInt(floorInfo[0]);
            printFreeBeds();
            System.out.println();
            System.out.println("Rooms on floor");
            for (Rooms room : roomsOnFloor) {
                System.out.println("Room " + room.roomid +" - " + room.roomtype + " - Capacity: " + room.roomsidecapacity);
            }
            System.out.println();
            System.out.println("Chambers on floor: ");
            for (Rooms room : roomsOnFloor) {
                if (room.roomtype.equals("Chamber")) {
                    Chamber chamber = (Chamber) room;
                    System.out.println("Chamber " + chamber.getChamberID());
                }
            }
            System.out.println();
        }
    }
    public static void printFreeBeds() {
    int counter = 0;
        for (String floorKey : Floorplan.keySet()) {
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            String[] floorInfo = floorKey.split("_");
            int floor = Integer.parseInt(floorInfo[0]);

            System.out.println("Empty beds on Floor " + floor + ":");
            for (Rooms room : roomsOnFloor) {
                for (String bedType : room.Bedsinroom.keySet()) {
                    Beds bed = room.Bedsinroom.get(bedType);
                    if (bed.occupant == null) {
                        System.out.println("Room " + floorKey + " - Bed Type: " + bedType);
                        counter++;
                    }
                }
            }
            System.out.println();
        }
        if(counter == 0){
            System.out.println("No beds found");
        }
    }
    public void EmergencyAdmission() {
        String bedid = null;
        Beds bed = null;
        System.out.println("\u000c");
        header("Emergency Admission Menu");
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
        String adress = Takestrinp("Enter the address of the patient: ");
        String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
        String GurdianPhoneno = takesphinp("Enter the Gurdian Phone no of the patient: ");
        String yn = Takestrinp("Do you want to add the patients medical history/complaints Y/N: ");
        ArrayList<String> MedicalHistory = new ArrayList<>();
        if (yn.trim().toLowerCase().equals("y")) {
            String k = "";
            System.out.println("To exit type out and for new lines just press enter: ");
            while (!k.trim().toLowerCase().equals("out")) {
                k = Takestrinp("Enter the complaints: ");
                if(k.trim().toLowerCase().equals("out")){
                    break;
                }
                MedicalHistory.add(k);
            }
        }
        printEmtDocchart();
        String docid = Takestrinp("Enter the doctor id(if none is appearning on screen type none to exit this is because no doctor is logged in): ");
        if (!EMT_Registry.containsKey(docid)) {
            System.out.println("Doctor not found");
            return;
        }
        System.out.println("Patient added");
        System.out.println("Assigning doctor");
        String yns = Takestrinp("Do you want to assign a bed Y/N: ");
        if(yns.trim().toLowerCase().equals("y")){
            printFreeBeds();
            bedid = Takestrinp("Enter the bed id: ");
            String roomid = bedid.split("_")[0];
            roomid = roomid.substring(0,roomid.length()-1);
            Rooms room = null;
            for (String floorKey : Floorplan.keySet()) {
                ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
                for (Rooms room1 : roomsOnFloor) {
                    if (room1.roomid.equals(roomid)) {
                        room = room1;
                        break;
                    }
                }
            }
            assert room != null;
            Patient pat = new Patient(addmitno, Calendar.getInstance().DATE + "/" + Calendar.getInstance().MONTH + "/" + Calendar.getInstance().YEAR, name, phoneno, bedid, bed, adress, Gurdian, GurdianPhoneno, MedicalHistory, docid);
            ScheduleAppointment("Emergency", pat, docid);
            EPatient_Registry.put(addmitno, pat);

            if (room == null) {
                System.out.println("Room not found");
                return;
            }
            bed = room.Bedsinroom.get(bedid);
            if (bed == null) {
                System.out.println("Bed not found");
                return;
            }
            if (bed.occupant != null) {
                System.out.println("Bed already occupied");
                return;
            }
            bed.occupant = pat.addmitno;
            pat.rm = bed;
            pat.patientdata.set(2, bedid);
        }
        else{
            Patient pat = new Patient(addmitno, Calendar.getInstance().DATE + "/" + Calendar.getInstance().MONTH + "/" + Calendar.getInstance().YEAR, name, phoneno, null, null, adress, Gurdian, GurdianPhoneno, MedicalHistory, docid);
            ScheduleAppointment("Emergency", pat, docid);
            EPatient_Registry.put(addmitno, pat);

        }


    }
    public void EmergencyPatientDiagnosis() {
        System.out.println("\u000c");
        header("Emergency Patient Diagnosis Menu");
        String addmitno = Takestrinp("Enter patient admit number: ");
        if(!EPatient_Registry.containsKey(addmitno)){
            System.out.println("There is no such user");
            return;
        }
        Patient pat = EPatient_Registry.get(addmitno);
        String yn = Takestrinp("Do you want to add the patients medical history Y/N: ");
        ArrayList<String> MedicalHistory = new ArrayList<>();
        if (yn.trim().toLowerCase().equals("y")) {
            String k = "";
            System.out.println("To exit type out and for new lines just press enter: ");
            while (!k.trim().toLowerCase().equals("out")) {
                k = Takestrinp("Enter the complaints: ");
                if(k.trim().toLowerCase().equals("out")){
                    break;
                }
                MedicalHistory.add(k);
            }
        }
        pat.MedicalHistory.put("Emergency", MedicalHistory);
        System.out.println("Patient Diagnosis added");
        System.out.println("Is the  Patient needed to be transferred to another hospital Y/N: ");
        String n = Takestrinp("-> ");
        if (n.trim().toLowerCase().equals("y")) {
            System.out.println("Do you want to immedietly bill out patient Y/N: ");
            String yn1 = Takestrinp("-> ");
            if (yn1.trim().toLowerCase().equals("y")) {
                System.out.println("Billing out patient: ");
                EPatient_Registry.remove(addmitno);
            } else {
                System.out.println("Patient not billed out");
            }
        }
    }
    private void printEmtDocchart() {
        System.out.println("\u000c");
        System.out.println("For backing at any point just type exit");
        header("Doctor View Menu ");
        System.out.println("Doctors");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("| %-30s | %-20s | %-20s |\n", "ID", "Doctor name", "Doctor Speciality");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (String doctorId : EMT_Registry.keySet()) {
            Doctor doc = EMT_Registry.get(doctorId);
            if (doc.isMedicAvaliable) {
                System.out.printf("| %-30s | %-20s | %-20s |\n", doc.doctorid, doc.name, doc.speaclity);
            }
        }
        System.out.println("--------------------------------------------------------------------------------------------------");

    }
    public void floorPlannerMenu(int floorNo, boolean refferenced) {
        int n;
        if (refferenced) {
            printFloorPlan();
        }
        for (int i = Floorplan.size(); i <= floorNo; i++) {
            System.out.println("Floor Planner Menu for Floor " + i);
            do {
                System.out.println("1. Add Rooms");
                System.out.println("2. Add Chamber");
                System.out.println("3. Remove Room ");
                System.out.println("4. Remove Chamber");
                System.out.println("5. Add Beds to a room");
                System.out.println("6. Remove Bed from a room");
                System.out.println("7. Change Room Capacity");
                System.out.println("8. Change Bed Type");
                System.out.println("9. Exit");
                n = Takeintinp("Enter your choice: ");
                switch (n) {
                    case 1:
                        String roomType = Takestrinp("Enter the type of room: ");
                        roomPlanner(i, roomType);
                        break;
                    case 2:
                        addChamber(i);
                        totalchambersbooked++;
                        break;
                    case 3:
                        printFloorPlan();
                        removeRoom(i);
                        break;
                    case 4:
                        printFloorPlan();
                        removeChamber(i);
                        break;
                    case 5:
                        printFloorPlan();

                        addBedsToRoom(i);
                        break;
                    case 6:
                        System.out.println("You can only remove free beds: ");
                        printFreeBeds();
                        removeBedFromRoom(i);
                        break;
                    case 7:
                        printFloorPlan();
                        changeRoomCapacity(i);
                        break;
                    case 8:
                        changeBedType(i);
                        break;
                    case 9:
                        continue;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (n != 9);
        }
    }
    public void roomPlanner(int floorNo, String roomType) {
        ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorNo + "_" + roomType);
        if (roomsOnFloor == null) {
            roomsOnFloor = new ArrayList<>();
            Floorplan.put(String.valueOf(floorNo), roomsOnFloor);
        }
        int roomID = roomsOnFloor.size() + 1;
        String addBeds = Takestrinp("Do you want to add beds Y/N: ").toLowerCase();
        int roomCapacity = Takeintinp("Enter the capacity of the room: ");
        Rooms room = new Rooms("F"+floorNo+"_R"+roomID+"_T"+roomType, roomType, roomCapacity);
        roomsOnFloor.add(room);

        if (addBeds.equals("y")) {
            addBeds(room, roomID, floorNo);
        }
    }
    public void addBeds(Rooms room, int roomID, int floorNo) {
        int numTypesOfBeds = Takeintinp("Enter the number of types of beds you want to have.To exit enter 0: ");
        if (numTypesOfBeds == 0) {
            return;
        }
        if(numTypesOfBeds > room.roomsidecapacity){
            System.out.println("Number of beds cannot be greater than room capacity");
            numTypesOfBeds = Takeintinp("Enter the number of types of beds you want to have.To exit enter 0: ");
        }
        if(room.roomsidecapacity <=room.Bedsinroom.size()){
            System.out.println("Room capacity is full");
        }
        for (int i = 1; i <= numTypesOfBeds; i++) {
            int numBeds = Takeintinp("Enter the number of beds for this type: ");
            String bedType = Takestrinp("Enter the type of beds: ");
            int setbedprice = Takeintinp("Set bed price: ");
            for (int j = 1; j <= numBeds; j++) {
                ;
                Beds bed = new Beds(roomID + "_" + bedType + "_" + j, setbedprice, bedType);
                room.Bedsinroom.put(roomID + "_" + bedType + "_" + j, bed);
            }
        }
    }
    public void addChamber(int floorNo) {
        System.out.println("Adding chamber on floor " + floorNo);
        Random rand = new Random();
        ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorNo + "_Chamber"+"_"+ rand.nextInt(0,9)+rand.nextInt(0,9)+rand.nextInt(0,9));
        if (roomsOnFloor == null) {
            roomsOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_Chamber"+"_"+  rand.nextInt(0,9)+rand.nextInt(0,9)+rand.nextInt(0,9), roomsOnFloor);
        }
        int roomID = roomsOnFloor.size() + 1;

        int roomCapacity = Takeintinp("Enter the capacity of the chamber: ");
        Chamber chamber = new Chamber(floorNo + "_Chamber"+"_"+  rand.nextInt(0,9)+rand.nextInt(0,9)+rand.nextInt(0,9), roomCapacity);
        roomsOnFloor.add(chamber);
        Chambers.add(chamber);
        System.out.println("Chamber added: " + chamber.getChamberID());
    }
    public void ScheduleAppointment(String Date, Patient patient, String DoctorID) {
        if (Doctor_Registry.containsKey(DoctorID)) {
            Doctor doc = Doctor_Registry.get(DoctorID);
            doc.AddPatientToSchedule(Date, patient);
            financials.addIncome(String.valueOf("Doctors visit by" + patient.addmitno), doc.perpatientcharge, (doc.perpatientcharge - doc.clinicshare), true);
            doc.earned += doc.perpatientcharge - doc.clinicshare;
        } else {
            System.out.println("Doctor not found");
        }
    }
    public void RegisterDoctor() {
        String name = Takestrinp("Enter the name of the doctor: ");
        String phoneno = takesphinp("Enter the phone no of the doctor: ");
        String doctorid = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
        if (Doctor_Registry.containsKey(doctorid)) {
            String n = Takestrinp("Doctor already exists do you want to create another doc with the same details (Y/N): ");
            if (n.trim().toLowerCase().equals("n")) {
                return;
            }
            doctorid.concat("_" + Doctor_Registry.size());

        }
        String standby = Takestrinp("Is the doc a medic on standby(Y/N): ");
        boolean b;
        if (standby.trim().toLowerCase().equals("y")) {
            b = true;
        } else {
            b = false;
        }

        String speaclity = Takestrinp("Enter the speciality of the doctor: ");
        String emphno = takesphinp("Enter the emergency no of the doctor: ");
        ArrayList<String> data = new ArrayList<String>();
        data.add(name);
        data.add(phoneno);
        data.add(doctorid);
        data.add(speaclity);
        data.add(emphno);
        int dlimit = Takeintinp("Enter the daily limit of the doctor: ");
        double perpatientcharge = Takeintinp("Enter the per patient charge of the doctor: ");
        double clinicshare = Takeintinp("Enter the clinic share of the doctor: ");
        if (clinicshare > perpatientcharge) {
            System.out.println("Clinic share cannot be greater than per patient charge");
            clinicshare = Takeintinp("Enter the clinic share of the doctor: ");
        }
        ArrayList<String> Date = new ArrayList<>();
        System.out.println("If the doctor is not available on a particular day then type 0,0");
        HashMap<String, String> chamberSchedule = new HashMap<>();
        for (int i = 1; i <= 7; i++) {
            String n = Takestrinp("Enter the timings for day " + i + " of the week,separate shift by (,): ");
            Date.add(n);
        }

        doctorSchedules.put(doctorid, Date);
        Doctor doc = new Doctor(doctorid, name, speaclity, emphno, data, perpatientcharge, dlimit, clinicshare, Date, b);
        Doctor_Registry.put(doctorid, doc);
        doc.setChamberSchedule(chamberSchedule);
        if (b) {
            EMT_Registry.put(doctorid, doc);
        }
        System.out.println("Doctor Registered");
    }
    public void PrintDocPatients() {
        String docid = Takestrinp("Enter the doctor id (if all then type all): ");
        if (docid.equals("all")) {
            for (String doctorId : Doctor_Registry.keySet()) {
                Doctor doc = Doctor_Registry.get(doctorId);
                if (doc == null) {
                    System.out.println("No doctor found with this id");
                    return;
                }
                if(doc.Patient_Registry == null){
                    System.out.println("No one is in the registry");
                    return;
                }
                for (String date : doc.Patient_Registry.keySet()) {
                    ArrayList<Patient> arr = doc.Patient_Registry.get(date);
                    System.out.println("-------------------------------------------------------------------");
                    System.out.printf("| %-12s | %-12s | \n", "Date", "Patient");
                    System.out.println("-------------------------------------------------------------------");
                    for (Patient pat : arr) {
                        System.out.printf("| %-12s | %-20s |", date, pat.patientdata.get(0));
                    }
                }
            }
        } else {
            Doctor doc = Doctor_Registry.get(docid);
            if (doc == null) {
                System.out.println("No doctor found with this id");
                return;
            }
            for (String date : doc.Patient_Registry.keySet()) {
                ArrayList<Patient> arr = doc.Patient_Registry.get(date);
                System.out.println("-------------------------------------------------------------------");
                System.out.printf("| %-12s | %-12s | \n", "Date", "Patient");
                System.out.println("-------------------------------------------------------------------");
                for (Patient pat : arr) {
                    System.out.printf("| %-12s | %-20s |", date, pat.patientdata.get(0));
                }
            }

        }
    }
    public void NewBooking() {
        if (Doctor_Registry.isEmpty()) {
            System.out.println("No doctors in the clinic");
            return;
        }
        System.out.println("For exiting at any point just type exit");
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        if (name == "exit" || phoneno == "exit") {
            return;
        }
        String addmitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
        printDocChart();
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        if (!Doctor_Registry.containsKey(RegisterAseatofDoc)) {
            System.out.println("Doctor not found");
            return;
        }
        PrintSchedule(RegisterAseatofDoc);
        String enterthedate = Takestrinp("Enter the date of Registration along with shift in the format (dd/mm/yy-shift1) : ");
        if (enterthedate.trim().toLowerCase().equals("exit")) {
            return;
        }
        if (Patient_Registry.containsKey(addmitno.toLowerCase())) {
            ScheduleAppointment(enterthedate, Patient_Registry.get(addmitno), RegisterAseatofDoc);
        } else {
            String adress = Takestrinp("Enter the address of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient (for none type none): ");
            String GurdianPhoneno = takesphinp("Enter the Gurdian Phone no of the patient(for none type none): ");
            String yn = Takestrinp("Do you want to add the patients medical history Y/N: ");
            ArrayList<String> MedicalHistory = new ArrayList<>();
            if (yn.trim().toLowerCase().equals("y")) {
                String k = "";
                System.out.println("To exit type out and for new lines just press enter: ");
                while (!k.trim().toLowerCase().equals("out")) {
                    k = Takestrinp("Enter the medical history: ");
                    if(k.trim().toLowerCase().equals("out")){
                        break;
                    }
                    MedicalHistory.add(k);
                }
            }
            Patient pat = new Patient(addmitno, enterthedate, name, phoneno, null, adress, Gurdian, GurdianPhoneno, email, MedicalHistory);
            Patient_Registry.put(addmitno, pat);
            ScheduleAppointment(enterthedate, pat, RegisterAseatofDoc);

        }
    }
    public void printDocChart() {
        System.out.println("\u000c");
        header("Doctor View Menu");
        System.out.println("Doctors");
        System.out.println("------------------------------------------------------------------------------");
        System.out.printf("| %-30s | %-20s | %-20s |\n", "ID", "Doctor name", "Doctor Speciality");
        System.out.println("-------------------------------------------------------------------------------");

        for (String doctorId : Doctor_Registry.keySet()) {
            Doctor doc = Doctor_Registry.get(doctorId);
            if(doc.doctorid == null){
                continue;
            }
            System.out.printf("| %-30s | %-20s | %-20s |\n", doc.doctorid, doc.name, doc.speaclity);
        }
        System.out.println("-------------------------------------------------------------------------------");

    }
    public void RemovePatientBooking() {
        System.out.println("\u000c");
        header("Patient Removal Menu");
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String admitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
        if (validatePatient(name, phoneno)) {
            System.out.println("No such patient found");
            return;
        }
        String date = Takestrinp("Enter the date of the appointment: ");
        Patient pat = Patient_Registry.get(admitno);
        try {
            String docid = pat.getAppointment(date);
            pat.removeAppointment(docid);
            Doctor doc = Doctor_Registry.get(docid);
            doc.RemovePatientReg(date, pat);
        } catch (Exception e) {
            System.out.println("No appointment was there");
        }

    }
    public void mainMeu() throws NoSuchAlgorithmException {
        int n = 0;
        do {
            System.out.print("\u000c");
            System.out.flush();
            header("Admin Menu ");
            System.out.println("0. Emergency Admission");
            System.out.println("1. Patient Functions");
            System.out.println("2. Management and Infrastructure");
            System.out.println("3. Financials");
            System.out.println("4. User Management ");
            System.out.println("5. Export Data");
            System.out.println("6. Log doctor in");
            System.out.println("7. Log doctor out");
            System.out.println("8. Show all available doctors");
            System.out.println("9. Exit");
            n = Takeintinp("-> ");
            switch (n) {
                case 0:
                    System.out.println("\u000c");
                    EmergencyAdmission();
                    break;
                case 1:
                    System.out.println("\u000c");
                    PatientMenu();
                    break;
                case 2:
                    System.out.println("\u000c");
                    Management();
                    break;
                case 3:
                    System.out.println("\u000c");
                    FinancialsMenu();
                    break;
                case 4:
                    System.out.println("\u000c");
                    Userregmenu();
                    break;
                case 5:
                    System.out.println("\u000c");
                    System.out.println("Exporting data");
                    ArrayList<Doctor> doc = new ArrayList<>();
                    for (String docid : Doctor_Registry.keySet()) {
                        doc.add(Doctor_Registry.get(docid));
                    }
                    ArrayList<Patient>Pat_reg = new ArrayList<>();
                    for (String patid : Patient_Registry.keySet()) {
                        Pat_reg.add(Patient_Registry.get(patid));
                    }
                    for(String patid : EPatient_Registry.keySet()){
                        Pat_reg.add(EPatient_Registry.get(patid));
                    }
                    ArrayList<Staff> staff_reg = new ArrayList<>();
                    for (String staffid : Staff_Registry.keySet()) {
                        staff_reg.add(Staff_Registry.get(staffid));
                    }
                    dex.exportAll(doc,financials.Expenses,financials.Ins,Pat_reg,staff_reg,Floorplan,doctorSchedules);

                    break;
                case 6:
                    System.out.println("\u000c");
                    printDocChart();
                    String docid = Takestrinp("Enter the doc id: ");
                    Doctor docdata = Doctor_Registry.get(docid);
                    if(docdata == null){
                        System.out.println("No such doctor found");
                        return;
                    }
                    docdata.Login();
                    break;
                case 7:
                    System.out.println("\u000c");
                    printDocChart();
                    String docide = Takestrinp("Enter the doc id: ");
                    Doctor docw = findDoctorById(docide);
                    if(docw == null){
                        System.out.println("Doctor not found");
                        return;
                    }
                    docw.Logout();
                    break;
                case 8:
                    System.out.println("\u000c");
                    int counter = 0;
                    System.out.println("Available doctors: ");
                    for(Doctor docs : Doctor_Registry.values()){
                        if(docs.isMedicAvaliable){
                            System.out.println("Doctor id: "+docs.doctorid+" Doctor name: "+docs.name+" Doctor speciality: "+docs.speaclity);
                            counter++;
                            System.out.println();
                        }
                    }
                    if(counter ==0){
                        System.out.println("No doctors found");
                    }
                    System.out.println();
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Enter a number between 0-8");

            }

        }
        while (n != 9);
    }
    private void FinancialsMenu() {
        System.out.println("\u000c");
        int n = 0;
        do {
            System.out.println("Welcome to the Financial Menu of " + clinic_managementSystem + ": ");
            double[] arr = financials.quickshotdata();
            System.out.println("Expenses: " + arr[0]);
            System.out.println("Income: " + arr[1]);
            System.out.println("Available balance: " + (arr[1] - arr[0]));
            System.out.println("1.Add a new income");
            System.out.println("2.Add a new expense");
            System.out.println("3.Modify Income");
            System.out.println("4.Modify Expense");
            System.out.println("5.Show Financials");
            System.out.println("6.Pay roll");
            System.out.println("7.Exit");
            n = Takeintinp("->");
            switch (n) {
                case 1:
                    String incomename = Takestrinp("Source of income: ");
                    double cost = Takeintinp("Cost of the income: ");
                    boolean hosexp = Takestrinp("Does it have a hospital expense Y/N: ").toLowerCase().equals("y");
                    double hosexpense = 0.0;
                    if (hosexp) {
                        hosexpense = Takeintinp("Enter the hospital expense: ");
                    }
                    financials.addIncome(incomename, cost, hosexpense, hosexp);
                    break;
                case 2:
                    String expensename = Takestrinp("Source of expense: ");
                    double expensecost = Takeintinp("Cost of the expense: ");
                    financials.addExpense(expensename, expensecost);
                    break;
                case 3:
                    //modify income
                    financials.showFinancials();
                    financials.modfiyIncome();
                    break;
                case 4:
                    //modify expense
                    financials.showFinancials();
                    financials.modfiyExpense();
                    break;
                case 5:
                    financials.showFinancials();
                    break;
                case 6:
                    releasepayroll();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Enter a number between 1 and 7");
            }
        } while (n != 7);
    }
    private void releasepayroll() {
        System.out.println("\u000c");
        for (Doctor doc : Doctor_Registry.values()) {
            System.out.println("Doctor " + doc.name + " has earned " + doc.earned);
            System.out.println("Paying Doctor " + doc.name + " " + doc.earned);
            financials.addExpense(String.valueOf("Paying" + doc.name), doc.earned);
            doc.earned = 0;
        }
        for (Staff staff : Staff_Registry.values()) {
            System.out.println("Staff " + staff.staffname + " has earned " + staff.staffsalary);
            System.out.println("Paying Staff " + staff.staffname + " " + staff.staffsalary);
            financials.addExpense(String.valueOf("Paying" + staff.staffname), staff.staffsalary);
        }
    }
    private void Management() {
        System.out.print("\u000c");
        int n = 0;
        do {
            System.out.println("Welcome to the Management and Infrastructure menu of " + clinic_managementSystem);
            System.out.println("1.  Add Doctor");
            System.out.println("2.  Add Staff");
            System.out.println("3.  Show all doctors");
            System.out.println("4.  Show all staff");
            System.out.println("5.  Change Doctor things");
            System.out.println("6.  Change Staff things");
            System.out.println("7.  Remove Doctor");
            System.out.println("8.  Remove staff");
            System.out.println("9. Print Floor plan");
            System.out.println("10. Change floor plan");
            System.out.println("11. View Doc's Schedule");
            System.out.println("12. Add doctor day off");
            System.out.println("13. Add a floor");
            System.out.println("14. Exit");
            n = Takeintinp("-> ");
            switch (n) {
                case 1:
                    System.out.println("\u000c");
                    RegisterDoctor();
                    break;
                case 2:
                    System.out.println("\u000c");
                    RegisterStaff();
                    break;
                case 3:
                    for (Doctor doc : Doctor_Registry.values()) {
                        doc.printData();
                    }
                    System.out.println();
                    break;
                case 4:
                    for (Staff staff : Staff_Registry.values()) {
                        staff.printData();
                    }
                    break;
                case 5:
                    System.out.println("\u000c");
                    doctorMenu();
                    break;
                case 6:
                    System.out.println("\u000c");
                    staffMenu();
                    break;
                case 7:
                    for (Doctor doc : Doctor_Registry.values()) {
                        doc.printData();
                    }
                    String doctorId = Takestrinp("Enter Doctor ID to remove for (exiting type exit): ");
                    if (doctorId.trim().toLowerCase().equals("exit")) {
                        System.out.println("Exiting");
                        return;
                    }
                    Doctor doctor = findDoctorById(doctorId);
                    if (doctor != null) {
                        if (doctor.earned > 0) {
                            System.out.println("Do you want to pay the doctor Y/N: ");
                            String choice = Takestrinp("-> ");
                            if (choice.toLowerCase().equals("y")) {
                                financials.addExpense(String.valueOf("Paying" + doctor.name), doctor.earned);
                            }
                        }
                        for (String x : doctor.Patient_Registry.keySet()) {
                            ArrayList<Patient> pat = doctor.Patient_Registry.get(x);
                            for (Patient patient : pat) {
                                patient.removeAppointment(x);
                                System.out.println("Cancelled appointment for" + patient.patientdata.get(0) + "on " + x);
                            }
                        }
                        Doctor_Registry.remove(doctorId);
                        System.out.println("Doctor removed successfully.");

                    } else {
                        System.out.println("Doctor not found with ID: " + doctorId);

                    }
                    break;
                case 8:
                    System.out.println("\u000c");
                    for (Staff staffu : Staff_Registry.values()) {
                        staffu.printData();
                    }
                    String staffId1 = Takestrinp("Enter Staff ID to remove(exiting type exit): ");
                    if (staffId1.trim().toLowerCase().equals("exit")) {
                        System.out.println("Exiting");
                        return;
                    }
                    Staff staffer = findStaffById(staffId1);
                    String answer = Takestrinp("Do you want to pay the staff his usual salary of " + staffer.staffsalary + "(Y/N) :");
                    if (answer.trim().toLowerCase().equals("y")) {
                        financials.addExpense(String.valueOf("Paying" + staffer.staffname), staffer.staffsalary);
                    }
                    Staff_Registry.remove(staffId1);
                    break;
                case 9:
                    System.out.println("\u000c");
                    printFloorPlan();
                    break;
                case 10:
                    System.out.println("\u000c");
                    System.out.println("Floor modification menu: ");
                    int floorNo = Takeintinp("Enter the floor number: ");
                    floorPlannerMenu(floorNo, true);
                    break;
                case 11:
                    System.out.println("\u000c");
                    for (Doctor doc : Doctor_Registry.values()) {
                        doc.printData();
                    }
                    System.out.println();
                    PrintSchedule(Takestrinp("Enter the docid: "));
                    PrintDocPatients();
                    break;
                case 12:
                    System.out.println("\u000c");
                    printDocChart();
                    String docid = Takestrinp("Enter the doctor id(to stop type exit): ");
                    if (!Doctor_Registry.containsKey(docid)) {
                        System.out.println("Doctor not found");
                        return;
                    }
                    Doctor doc = Doctor_Registry.get(docid);
                    printCalendar(doc);
                    String date = Takestrinp("Enter the date of the day off: ");
                    doc.offdays.add(date);
                    if (doc.Patient_Registry.containsKey(date)) {
                        ArrayList<Patient> pat = doc.Patient_Registry.get(date);
                        for (Patient patient : pat) {
                            System.out.println("Removing appointment for patient " + patient.patientdata.get(0) + " on " + date);
                            patient.removeAppointment(date);
                        }
                    }
                    break;
                case 13:
                    System.out.println("\u000c");
                    int flno1 = Takeintinp("Enter the floor's to be added after the existing "+Floorplan.size()+" floors: ");
                    for(int i = 0;i<flno1;i++){
                        Floorplan.put(String.valueOf(Floorplan.size()+1), new ArrayList<>());
                        floorPlannerMenu(Floorplan.size(), false);
                    };
                    break;

                case 14:
                    return;
                default:
                    System.out.println("Enter a number between 1 and 14 !!!");
            }

        }
        while (n != 14);
    }
    private void RegisterStaff() {
        System.out.println("\u000c");
        String name = Takestrinp("Enter the name of the staff: ");
        String phoneno = takesphinp("Enter the phone no of the staff: ");
        String staffid = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
        if (validateStaff(name, phoneno)) {
            System.out.println("Staff already exists");
            return;
        }
        String speaclity = Takestrinp("Enter the designation of the staff: ");
        String emphno = Takestrinp("Enter the emergency no of the staff: ");
        double x = Takeintinp("Enter the salary of the staff: ");
        ArrayList<String> data = new ArrayList<String>();
        data.add(name);
        data.add(phoneno);
        data.add(staffid);
        data.add(speaclity);
        data.add(emphno);
        Staff staff = new Staff(name, speaclity, emphno, data, x);
        Staff_Registry.put(staffid, staff);
        System.out.println("Staff Registered");

    }
    public void staffMenu() {
        System.out.println("\u000c");
        Scanner scanner = new Scanner(System.in);
        displayStaffMembers();
        System.out.print("Enter Staff ID to modify: ");
        String staffId = scanner.nextLine();
        Staff staff = findStaffById(staffId);

        if (staff != null) {
            int choice;
            do {
                System.out.println("Staff Menu:");
                System.out.println("1. Change Staff Name");
                System.out.println("2. Change Staff Position");
                System.out.println("3. Change Staff Phone Number");
                System.out.println("4. Change Staff Salary");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

                switch (choice) {
                    case 1:
                        System.out.print("Enter New Staff Name: ");
                        String newName = scanner.nextLine();
                        staff.staffname = newName;
                        break;
                    case 2:
                        System.out.print("Enter New Staff Position: ");
                        String newPosition = scanner.nextLine();
                        staff.staffdesignation = newPosition;
                        break;
                    case 3:
                        System.out.print("Enter New Staff Phone Number: ");
                        String newPhoneNumber = scanner.nextLine();
                        staff.staffphno = newPhoneNumber;
                        break;
                    case 0:
                        System.out.println("Exiting Staff Menu");
                        break;
                    case 4:
                        System.out.print("Enter New Staff Salary: ");
                        double newSalary = scanner.nextDouble();
                        scanner.nextLine();
                        staff.staffsalary = newSalary;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } else {
            System.out.println("Staff member not found with ID: " + staffId);
        }
    }
    private void displayStaffMembers() {
        System.out.println("List of Staff Members:");
        for (Staff staff : Staff_Registry.values()) {
            System.out.println("Staff ID: " + staff.staffid + ", Staff Name: " + staff.staffname);
        }
    }
    private @Nullable Staff findStaffById(String staffId) {
        for (Staff staff : Staff_Registry.values()) {
            if (staff.staffid.equals(staffId)) {
                return staff;
            }
        }
        return null;
    }
    public void doctorMenu() {
        Scanner scanner = new Scanner(System.in);

        // Assuming you have a method to display the list of doctors
        for (Doctor doc : Doctor_Registry.values()) {
            doc.printData();
        }
        System.out.println();
        String doctorId = Takestrinp("Enter Doctor ID to modify: ");
        Doctor doctor = findDoctorById(doctorId);

        if (doctor != null) {
            int choice;
            do {
                System.out.print("\u000c");
                header("Doctor Menu");
                System.out.println("1. Change Doctor Name");
                System.out.println("2. Change Speciality");
                System.out.println("3. Change Phone Number");
                System.out.println("4. Change Per Patient Charge");
                System.out.println("5. Change Daily Appointment Limit");
                System.out.println("6. Add Doctor off days");
                System.out.println("0. Exit");
                choice = Takeintinp("->");// Consume the newline character left by nextInt()

                switch (choice) {
                    case 1:
                        System.out.print("Enter New Doctor Name: ");
                        String newName = scanner.nextLine();
                        doctor.name = newName;
                        break;
                    case 2:
                        System.out.print("Enter New Speciality: ");
                        String newSpeciality = scanner.nextLine();
                        doctor.speaclity = newSpeciality;
                        break;
                    case 3:
                        System.out.print("Enter New Phone Number: ");
                        String newPhoneNumber = scanner.nextLine();
                        doctor.emphno = newPhoneNumber;
                        break;
                    case 4:
                        System.out.print("Enter New Per Patient Charge: ");
                        double newPerPatientCharge = scanner.nextDouble();
                        scanner.nextLine(); // Consume the newline character left by nextDouble()
                        doctor.perpatientcharge = newPerPatientCharge;
                        break;
                    case 5:
                        System.out.print("Enter New Daily Appointment Limit: ");
                        int newDailyLimit = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character left by nextInt()
                        doctor.doctordailyLimit = newDailyLimit;
                        break;
                    case 6:
                        String issingle = Takestrinp("Is it for a single day(Y/N): ");
                        if(issingle.trim().toLowerCase().equals("Y")) {
                            String date = Takestrinp("Enter doctor off day (d/mm/yy):");
                            doctor.setOffdays(date);
                        }
                        else{
                            int x = Takeintinp("For how many days: ");
                            for(int i = 1;i<=x;i++){
                                String date = Takestrinp("Enter the date: ");
                                doctor.setOffdays(date);
                            }
                        }
                    case 0:
                        System.out.println("Exiting Doctor Menu");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } else {
            System.out.println("Doctor not found with ID: " + doctorId);
        }
    }
    private Doctor findDoctorById(String doctorId) {
        for (Doctor doctor : Doctor_Registry.values()) {
            if (doctor.doctorid.equals(doctorId)) {
                return doctor;
            }
        }
        for(Doctor doctor:EMT_Registry.values()){
            if (doctor.doctorid.equals(doctorId)) {
                return doctor;
            }
        }
        return null;
    }
    public void PatientMenu() {
        String name,phoneno;
        System.out.print("\u000c");
        do {
            System.out.println("Welcome to the Patient menu of " + clinic_managementSystem+"(emergency cases to be controlled from emergency menu point 12)");
            System.out.println("1.Schedule Appointment");
            System.out.println("2.Remove Appointment");
            System.out.println("3.Show Patient Details");
            System.out.println("4.Remove Patient ");
            System.out.println("5.Add Patient Billable service");
            System.out.println("6.Add Patient Medical History");
            System.out.println("7.Show Patient Medical History");
            System.out.println("8.Bill out patient");
            System.out.println("9.Modify Patient Details");
            System.out.println("10.Show all Patients");
            System.out.println("11.Show appointments for a day/doctor for today ");
            System.out.println("12.Modify emergency patients things");
            System.out.println("13.Add Bed charge for all patients");
            System.out.println("14. Exit");
            int x = Takeintinp("->");
            switch (x) {
                case 1:
                    NewBooking();
                    break;
                case 2:
                    RemovePatientBooking();
                    break;
                case 3:
                    System.out.println("\u000c");
                    System.out.println("All patients: ");
                    for (Patient pat12 : Patient_Registry.values()) {
                        pat12.printPatientDetails();
                    }
                    break;
                case 4:
                    Removepat();
                    break;
                case 5:
                    Addabillableservice();
                    break;
                case 6:
                    name = Takestrinp("Enter the name of the patient: ");
                    phoneno = takesphinp("Enter the phone no of the patient: ");
                    if (validatePatient(name, phoneno)) {
                        System.out.println("No patient found");
                        break;
                    }
                    Patient pat = Patient_Registry.get(name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim());
                    pat.addMedicalHistory();
                    break;
                case 7:
                    name = Takestrinp("Enter the name of the patient: ");
                    phoneno = takesphinp("Enter the phone no of the patient: ");
                    String admitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
                    Patient pat1 = Patient_Registry.get(admitno);
                    if (pat1 == null) {
                        System.out.println("No patient found");
                        break;
                    }
                    pat1.printMedicalHistory();
                    break;
                case 8:
                    System.out.println("\u000c");
                    System.out.println("Patient List");
                    for (Patient pat12 : Patient_Registry.values()) {
                        pat12.printPatientDetails();
                    }
                     admitno = Takestrinp("Enter  the admit no: ");
                    if(!Patient_Registry.containsKey(admitno)){
                        if(!EPatient_Registry.containsKey(admitno)){
                            System.out.println("There is no such patient");
                            return;
                        }
                    }
                    pat = null;
                    if(Patient_Registry.containsKey(admitno)){
                        pat = Patient_Registry.get(admitno);
                    }
                    if(EPatient_Registry.containsKey(admitno)){
                        pat = EPatient_Registry.get(admitno);
                    }
                    System.out.println("Billing out patient: " + pat.patientdata.get(0));
                    System.out.println("Do you want to bill out the patient Y/N: ");
                    String yn = Takestrinp("-> ");
                    if (yn.trim().toLowerCase().equals("y")) {
                        pat.Billoutpatient();
                        financials.addIncome(String.valueOf("Billout by" + pat.addmitno), pat.getBill(), (pat.getBill() - pat.getHospitalCost()), true);
                    } else {
                        System.out.println("Patient not billed out");
                    }

                    break;
                case 9:
                    System.out.println("\u000c");
                    System.out.println("Patient List");
                    for (Patient pat12 : Patient_Registry.values()) {
                        pat12.printPatientDetails();
                    }
                    for(Patient pat14:EPatient_Registry.values()){
                        pat14.printPatientDetails();
                    }
                    name = Takestrinp("Enter the name of the patient: ");
                    phoneno = takesphinp("Enter the phone no of the patient: ");
                    if (validatePatient(name, phoneno)) {
                        System.out.println("No patient found");
                        break;
                    }
                    Showpatientdetails(name, phoneno);
                    ModifyPatientMenu(name, phoneno);
                    break;
                case 10:
                    System.out.println("\u000c");
                    System.out.println("Patient List");
                    printPatientDataHeader();
                    System.out.println();
                    for (Patient pat12 : Patient_Registry.values()) {
                        pat12.printPatientDetails();
                    }
                    for(Patient pat13: EPatient_Registry.values()){
                        pat13.printPatientDetails();
                    }
                    break;
                case 13:
                    System.out.println("\u000c");
                    for (ArrayList<Rooms> room : Floorplan.values()) {
                        for (Rooms rooms : room) {
                            for (Beds beds : rooms.Bedsinroom.values()) {
                                if (beds.occupant != null) {
                                    if (Patient_Registry.containsKey(beds.occupant)) {
                                        pat = Patient_Registry.get(beds.occupant);
                                        pat.AddDailyBedCharge();
                                    } else if (EPatient_Registry.containsKey(beds.occupant)) {
                                        pat = EPatient_Registry.get(beds.occupant);
                                        pat.AddDailyBedCharge();
                                    }

                                }
                            }
                        }
                    }
                    System.out.println("Added Daily Bed charge for all bed occupying patients");
                    break;
                case 11:
                    System.out.println("\u000c");
                    System.out.println("Show Appointments for a Day/Doctor for Today");

                    // Assuming you have a method to get today's date
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);

                    System.out.println("1. Show Appointments for a Specific Day");
                    System.out.println("2. Show Appointments for a Specific Doctor");
                    int option = Takeintinp("Enter your choice (1 or 2): ");
                    switch (option) {
                        case 1:
                            // Show appointments for a specific day
                            String selectedDate = Takestrinp("Enter the date (yyyy-MM-dd): ");
                            showAppointmentsForDay(selectedDate);
                            break;

                        case 2:
                            // Show appointments for a specific doctor
                            printDocChart();
                            String doctorName = Takestrinp("Enter the id of the doctor: ");
                            Doctor doc = findDoctorById(doctorName);
                            if(doc == null){
                                System.out.println("Doctor not found");
                                break;
                            }
                            showAppointmentsForDoctor(doctorName, todayString);
                            break;

                        default:
                            System.out.println("Invalid option. Please enter 1 or 2.");
                    }
                    break;

                case 14:
                    return;
                case 12:


                default:
                    System.out.println("Enter a number");


            }
        }while (true) ;
    }
    private void showAppointmentsForDay(String selectedDate) {
        System.out.println("\u000c");
        System.out.println("Appointments for " + selectedDate);
        for (Doctor doc : Doctor_Registry.values()) {
            System.out.println("Doctor: " + doc.name);
            for (String date : doc.Patient_Registry.keySet()) {
                if (date.equals(selectedDate)) {
                    ArrayList<Patient> pat = doc.Patient_Registry.get(date);
                    for (Patient patient : pat) {
                        patient.printPatientDetails();
                    }
                }
            }
        }
    }
    private void showAppointmentsForDoctor(String doctorName, String todayString) {
        System.out.println("\u000c");
        System.out.println("Appointments for " + doctorName + " for " + todayString);
        for (Doctor doc : Doctor_Registry.values()) {
            if (doc.name.equals(doctorName)) {
                for (String date : doc.Patient_Registry.keySet()) {
                    if (date.equals(todayString)) {
                        ArrayList<Patient> pat = doc.Patient_Registry.get(date);
                        for (Patient patient : pat) {
                            patient.printPatientDetails();
                        }
                    }
                }
            }
        }
    }
    private void ModifyPatientMenu (String name, String phoneno){
            header("Patient Modification Menu");
            System.out.println("1. Change Patient Name");
            System.out.println("2. Change Patient Phone Number");
            System.out.println("3. Change Patient Address");
            System.out.println("4. Change Patient Email");
            System.out.println("5. Change Patient Guardian");
            System.out.println("6. Change Patient Guardian Phone Number");
            System.out.println("7. Modify a certain billable service");
            System.out.println("8. Modify medical history");
            System.out.println("9. Modify another patient");
            System.out.println("10. Show Patient Details");
            System.out.println("11. Modify another patient");
            System.out.println("12. Exit");

            int x = Takeintinp("->");
            Patient pat = Patient_Registry.get(name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim());
            switch (x) {
                case 1:
                    String newname = Takestrinp("Enter the new name of the patient: ");
                    String newphoneno = takesphinp("Enter the new phone no of the patient: ");
                    String newaddmitno = newname.substring(0).trim() + "_" + newphoneno.substring(6, 9).trim();
                    Patient_Registry.remove(name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim());
                    pat.addmitno = newaddmitno;
                    pat.patientdata.set(0, newname);
                    pat.patientdata.set(1, newphoneno);
                    Patient_Registry.put(newaddmitno, pat);
                    break;
                case 2:
                    String newphoneno1 = takesphinp("Enter the new phone no of the patient: ");
                    Patient_Registry.remove(name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim());
                    pat.patientdata.set(1, newphoneno1);
                    Patient_Registry.put(name.substring(0).trim() + "_" + newphoneno1.substring(6, 9).trim(), pat);
                    break;
                case 3:
                    String newadd = Takestrinp("Enter the new address of the patient: ");
                    pat.patientdata.set(3, newadd);
                    break;
                case 4:
                    String newemail = Takestrinp("Enter the new email of the patient: ");
                    pat = Patient_Registry.get(name.substring(0).trim() + "_" + phoneno);
                    pat.patientdata.set(4, newemail);
                    break;
                case 5:
                    String newguardian = Takestrinp("Enter the new guardian of the patient: ");
                    pat.patientdata.set(5, newguardian);
                    break;
                case 6:
                    String newguardianphno = Takestrinp("Enter the new guardian phone no of the patient: ");
                    pat.patientdata.set(6, newguardianphno);
                    break;
                case 7:
                    pat.printBillableServices();
                    String service = Takestrinp("Enter the name of the service: ");
                    int cost = Takeintinp("Enter the cost of the service: ");
                    if (pat.billable_services.containsKey(service + "_" + cost)) {
                        String yr = Takestrinp("Are you sure you want to continue Y/N: ");
                        if (yr.trim().toLowerCase().equals("y")) {
                            pat.billable_services.remove(service + "_" + cost);
                        } else {
                            return;
                        }
                        int y = pat.billable_services.get(service + "_" + cost);
                        y += cost;
                        pat.billable_services.put(service + "_" + cost, y);
                        if (pat.HospitalExpenses.get(service + "_" + cost) != null) {
                            int x1 = pat.HospitalExpenses.get(service + "_" + cost);
                            String yn = Takestrinp("There is also a hospital cost of  " + x1 + "Do you want to modify that Y/N ");
                            if (yn.trim().toLowerCase().equals("y")) {
                                x1 = Takeintinp("Enter the new hospital cost: ");
                            }

                            pat.HospitalExpenses.put(service + "_" + cost, x1);
                        } else {
                            pat.HospitalExpenses.put(service + "_" + cost, cost);
                        }
                        return;
                    }

                    pat.billable_services.put(service, cost);
                    break;
                case 8:
                    pat.printMedicalHistory();
                    System.out.println("1. Add a new medical history");
                    System.out.println("2. Modify a medical history");
                    System.out.println("3. Remove a medical history");
                    int y = Takeintinp("-> ");
                    switch (y) {
                        case 1:
                            pat.addMedicalHistory();
                            break;
                        case 2:
                            pat.modifyMedicalHistory();
                            break;
                        case 3:
                            pat.removeMedicalHistory();
                            break;
                        default:
                            System.out.println("Enter a number between 1-3");
                    }

                    break;
                case 9:
                    System.out.println("\u000c");
                    header("Patient Modification Menu");
                    String name1 = Takestrinp("Enter the name of the patient: ");
                    String phone1 = takesphinp("Enter the phone no of the patient: ");
                    ModifyPatientMenu(name1, phone1);
                    return;
                case 10:
                    pat.printPatientDetails();
                    break;
                case 11:
                    System.out.println("\u000c");
                    header("Patient Modification Menu");
                    String name2 = Takestrinp("Enter the name of the patient: ");
                    String phone2 = takesphinp("Enter the phone no of the patient: ");
                    if (validatePatient(name2, phone2)) {
                        System.out.println("No patient found");
                        break;
                    }
                    ModifyPatientMenu(name2, phone2);
                    return;
                case 12:
                    System.out.println("Exiting");
                    System.out.println("\u000c");
                    return;
                default:
                    System.out.println("Enter a number between 1-7");
            }

        }
    private void Showpatientdetails (String name, String phoneno){
        String admitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
        Patient pat = null;
        if(!Patient_Registry.containsKey(admitno)&&!EPatient_Registry.containsKey(admitno)){
            System.out.println("No patient found");
            return;
        }
            if(Patient_Registry.containsKey(admitno)){
                pat = Patient_Registry.get(admitno);
            }
        if(EPatient_Registry.containsKey(admitno)){
            pat = Patient_Registry.get(admitno);
        }
            if (pat != null) {
                pat.printPatientDetails();
            } else {
                System.out.println("Patient not found");
            }
        }
    private void Removepat () {
            System.out.println("\u000c");
            String name = Takestrinp("Enter the name of the patient: ");
            String phoneno = takesphinp("Enter the phone no of the patient: ");
            String admitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
            Patient pat = Patient_Registry.get(admitno);
            if (!pat.billable_services.isEmpty()) {
                int billable = 0;
                for (int i : pat.billable_services.values()) {
                    billable += i;
                }
                System.out.println("Patient has billable services of " + billable);
                System.out.println("Do you want to pay the bill Y/N: ");
                String choice = Takestrinp("-> ");
                if (choice.toLowerCase().equals("y")) {
                    financials.addExpense(String.valueOf("Paying" + pat.patientdata.get(0)), billable);
                } else {///
                    System.out.println("Patient not removed");
                    return;
                }
            }
            if (pat != null) {
                Patient_Registry.remove(admitno);
                for (Doctor doc : Doctor_Registry.values()) {
                    for (String x : doc.Patient_Registry.keySet()) {
                        ArrayList<Patient> pat1 = doc.Patient_Registry.get(x);
                        for (Patient patient : pat1) {
                            if (patient.addmitno.equals(admitno)) {
                                patient.removeAppointment(x);
                                System.out.println("Cancelled appointment for" + patient.patientdata.get(0) + "on " + x);
                            }
                        }
                    }
                }
                System.out.println("Patient removed");
            } else {
                System.out.println("Patient not found");
            }

        }
    private void Addabillableservice () {
            String name = Takestrinp("Enter the name of the patient: ");
            String phoneno = takesphinp("Enter the phone no of the patient: ");
            String admitno = name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim();
            Patient pat = Patient_Registry.get(admitno);
            if (pat != null) {
                String service = Takestrinp("Enter the name of the service: ");
                int cost = Takeintinp("Enter the cost of the service: ");
                if (pat.billable_services.containsKey(service + "_" + cost)) {
                    String str = Takestrinp("A service with that name already exists do you want to continue Y/");
                    if (str.trim().toLowerCase().equals("y")) {
                        str = Takestrinp("Do you want to edit the previous one or remove it Y/N");
                        if (str.trim().toLowerCase().equals("y")) {
                            pat.billable_services.remove(service + "_" + cost);

                        } else {
                            int x = pat.billable_services.get(service + "_" + cost);
                            System.out.println("Previous Cost was for this service " + x);
                            if (pat.HospitalExpenses.containsKey(service + "_" + cost)) {
                                System.out.println("Previous Hospital Cost was for this service " + pat.HospitalExpenses.get(service + "_" + cost));
                                int y = Takeintinp("Do you want to edit the hospital cost (For removal 1, for modification 2): ");
                                if (y == 1) {
                                    pat.HospitalExpenses.remove(service + "_" + cost);
                                } else {
                                    System.out.println("Previous Hospital Cost was for this service " + pat.HospitalExpenses.get(service + "_" + cost));
                                    int z = Takeintinp("Enter the new hospital cost: ");
                                    pat.HospitalExpenses.put(service + "_" + cost, z);
                                    System.out.println("Do you want to change the name: ");
                                    String yn = Takestrinp("-> ");
                                    if (yn.trim().toLowerCase().equals("y")) {
                                        String newname = Takestrinp("Enter the new name of the service: ");
                                        pat.billable_services.put(newname + "_" + cost, x);
                                        pat.billable_services.remove(service + "_" + cost);
                                    } else {
                                        pat.billable_services.remove(service + "_" + cost);
                                        pat.billable_services.put(service + "_" + cost, x);
                                    }
                                }
                            }
                            x = Takeintinp("Enter the new cost of the service: ");
                            pat.billable_services.put(service + "_" + cost, x);

                            return;
                        }

                    }
                    int x = pat.billable_services.get(service + "_" + cost);
                    x += cost;
                    pat.billable_services.put(service + "_" + cost, x);
                    return;
                }
                pat.billable_services.put(service, cost);
                String yn = Takestrinp("is there an hospital cost to it Y/N: ");
                if (yn.trim().toLowerCase().equals("y")) {
                    int cost1 = Takeintinp("Enter the cost of the service: ");
                    pat.HospitalExpenses.put(service + "_" + cost, cost1);
                }

            } else {
                System.out.println("Patient not found");
            }

        }
    private boolean validatePatient (String name, String phoneno) {
        if (Patient_Registry.containsKey(name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim())) {
            return false;
        }
        return true;
    }
    private boolean validateStaff (String name, String phoneno){
            if (Staff_Registry.containsKey(name.substring(0).trim() + "_" + phoneno.substring(6, 9).trim())) {
                return true;
            }
            return false;
        }
    public void Userregmenu () throws NoSuchAlgorithmException {
            do {
                System.out.println("0. Add another user");
                System.out.println("1. Change user password");
                System.out.println("2. Remove user");
                System.out.println("3. Exit");
                int n = Takeintinp("-> ");
                switch (n) {
                    case 0:
                        String username = Takestrinp("Enter the username: ");
                        String password = Takestrinp("Enter the password: ");
                        databaseE.registerUser(username, password);
                        databaseE.saveUserData();
                        break;
                    case 1:
                        System.out.println("\u000c");
                        if (databaseE.changePassword()) {
                            System.out.println("Sucessfully changed password");
                        } else {
                            System.out.println("Operation Failed !!");
                        }
                        break;
                    case 2:
                        System.out.println("Remove user");
                        databaseE.removeUser();
                        System.out.println("\u000c");
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Enter a number between 0 - 3");

                }
            } while (true);
        }
    // Add the corresponding methods for the new cases based on your requirements
    public void removeRoom(int floorNo) {
        String roomId = Takestrinp("Enter the ID of the room to remove: ");
        ArrayList<Rooms> floorRooms = Floorplan.get(String.valueOf(floorNo));
        if (floorRooms != null) {
            floorRooms.removeIf(room -> room.roomid.equals(roomId));
            System.out.println("Room " + roomId + " removed successfully.");
        } else {
            System.out.println("Floor " + floorNo + " not found.");
        }
    }
    public void removeChamber(int floorNo) {
        String chamberId = Takestrinp("Enter the ID of the chamber to remove: ");
        ArrayList<Rooms> floorRooms = Floorplan.get(String.valueOf(floorNo));
        if (floorRooms != null) {

            floorRooms.removeIf(room -> room instanceof Chamber && ((Chamber) room).getChamberID().equals(chamberId));
            System.out.println("Chamber " + chamberId + " removed successfully.");
        } else {
            System.out.println("Floor " + floorNo + " not found.");
        }
    }
    public void addBedsToRoom(int floorNo) {
        printFloorPlan();
        String roomId = Takestrinp("Enter the ID of the room to add beds: ");
        ArrayList<Rooms> floorRooms = Floorplan.get(String.valueOf(floorNo));
        if (floorRooms != null) {
            for (Rooms room : floorRooms) {
                if (room.roomid.equals(roomId)) {
                    int numberOfBeds = Takeintinp("Enter the number of beds to add: ");
                    String typeofbeds = Takestrinp("Enter the type of bed: ");
                    for (int i = 0; i < numberOfBeds; i++) {
                        int cost = Takeintinp("Enter the cost: ");
                        room.addBed(floorNo+"_"+roomId+"_"+i,cost,typeofbeds);
                    }
                    System.out.println(numberOfBeds + " bed(s) added to room " + roomId + ".");
                    return;
                }
            }
            System.out.println("Room " + roomId + " not found.");
        } else {
            System.out.println("Floor " + floorNo + " not found.");
        }
    }
    public void removeBedFromRoom(int floorNo) {
        String roomId = Takestrinp("Enter the ID of the room to remove a bed from: ");
        ArrayList<Rooms> floorRooms = Floorplan.get(String.valueOf(floorNo));
        if (floorRooms != null) {
            for (Rooms room : floorRooms) {
                if (room.roomid.equals(roomId)) {
                    room.printRoom();
                    String bedid = Takestrinp("Enter the bedid: ");
                    if(!room.Bedsinroom.containsKey(bedid)){
                        System.out.println("Bed not found");
                        return;
                    }
                    Beds beder = room.Bedsinroom.get(bedid);
                    if(beder.occupant != null || beder != null){
                        System.out.println("Bed is occupied");
                        return;
                    }
                    room.removebed(bedid);
                    System.out.println("Bed removed from room " + roomId + ".");
                    return;
                }
            }
            System.out.println("Room " + roomId + " not found.");
        } else {
            System.out.println("Floor " + floorNo + " not found.");
        }
    }
    public void changeRoomCapacity(int floorNo) {
        String roomId = Takestrinp("Enter the ID of the room to change capacity: ");
        ArrayList<Rooms> floorRooms = Floorplan.get(String.valueOf(floorNo));
        if (floorRooms != null) {
            for (Rooms room : floorRooms) {
                if (room.roomid.equals(roomId)) {
                    int newCapacity = Takeintinp("Enter the new capacity: ");
                    room.roomsidecapacity = newCapacity;
                    System.out.println("Capacity of room " + roomId + " changed to " + newCapacity + ".");
                    return;
                }
            }
            System.out.println("Room " + roomId + " not found.");
        } else {
            System.out.println("Floor " + floorNo + " not found.");
        }
    }
    public void changeBedType(int floorNo) {
        String roomId = Takestrinp("Enter the ID of the room with beds to change type: ");
        ArrayList<Rooms> floorRooms = Floorplan.get(String.valueOf(floorNo));
        if (floorRooms != null) {
            for (Rooms room : floorRooms) {
                if (room.roomid.equals(roomId)) {
                    room.printRoom();
                    String bedid =  Takestrinp("Enter the bedid: ");
                    Beds bed = null;
                    for(Beds bc : room.Bedsinroom.values())
                    {
                        if(bed.bedid == bedid){
                            bed = bc;
                            break;
                        }
                        else{
                            System.out.println("No bed found");
                            return;
                        }
                    }
                    String newBedType = Takestrinp("Enter the new bed type: ");
                    bed.changeBedType(newBedType);
                    System.out.println("Bed type in room " + roomId + " changed to " + newBedType + ".");
                    return;
                }
            }
            System.out.println("Room " + roomId + " not found or not a patient room.");
        } else {
            System.out.println("Floor " + floorNo + " not found.");
        }
    }

}
