package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
// /u00C
public class Hospital {
    static HashMap<String, ArrayList<Rooms>> Floorplan;
    HashMap<String, Operation> Operation_Registry;
    HashMap<String,Boolean> hasThings;
    HashMap<String ,Patient> Patient_Registry;
    HashMap<String,Doctor>Doctor_Registry;
    HashMap<String,ArrayList<String>> Doctor_Schedule;
    String clinic_managementSystem;
    Financials financials;
    public static int Takeintinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        int number = 0;
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

    Hospital() {
        Patient_Registry = new HashMap<>();
        Doctor_Registry = new HashMap<>();
        Floorplan = new HashMap<>();
        hasThings = new HashMap<>();
        System.out.println("Welcome to the Clinic Management System");
        System.out.println("                                by MSD");
        clinic_managementSystem = Takestrinp("Enter the name of the clinic: ");
        String hasoperation = Takestrinp("Do you have a operation room Y/N: ");
        hasThings.put("OT",hasoperation.toLowerCase().equals("y"));
        String haswaiting = Takestrinp("Do you have bed's Y/N: ");
        hasThings.put("Bed",haswaiting.toLowerCase().equals("y"));
        haswaiting = Takestrinp("Does the clinic have chamber Y/N: ");
        hasThings.put("Chambers",haswaiting.toLowerCase().equals("y"));
        int nooffloors = Takeintinp("Enter the number of floors: ");
        floorPlannerMenu(nooffloors);
        System.out.print("\033[H\033[2J");
        financials = new Financials();

    }

    public void RegisterPatient(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = Takestrinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        String enterthedate = Takestrinp("Enter the date of Registration : ");
        if(Patient_Registry.containsKey(addmitno.toLowerCase())){
            ScheduleAppointment(enterthedate,Patient_Registry.get(addmitno),RegisterAseatofDoc);
        }
        else{
            System.out.println("Not Registered");
            ArrayList<String> patientdata = new ArrayList<String>();
            String adress = Takestrinp("Enter the adress of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
            String GurdianPhoneno = Takestrinp("Enter the Gurdian Phone no of the patient: ");
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);
        }

    }

    public static void beds_printing() {
        for (String floorKey : Floorplan.keySet()) {
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            String[] floorInfo = floorKey.split("_");
            int floor = Integer.parseInt(floorInfo[0]);

            System.out.println("Empty beds on Floor " + floor + ":");

            for (Rooms room : roomsOnFloor) {
                for (String bedType : room.occupant.keySet()) {
                    Beds bed = room.occupant.get(bedType);
                    if (bed.occupant == null) {
                        System.out.println("Room " + floorKey + " - Bed Type: " + bedType);
                    }
                }
            }
        }
    }
    public void AddDoctor(){
        String docname = Takestrinp("Enter the name of the doctor: ");
        String docspecialty = Takestrinp("Enter the specialty of the doctor: ");
        String doctorphoneno = Takestrinp("Enter the phone no of the doctor: ");
        int doccapacity = Takeintinp("Enter the doccapacity: ");
        System.out.println("Enter the Day and timings of doctor(if not doing on a day just type 0): ");
        ArrayList<String>  days= new ArrayList<>();
        for(int i = 0;i<7;i++){
            days.add(Takestrinp("Enter the timing for day "+i));
        }
        Doctor_Schedule.put(docname+"_"+docspecialty.substring(0,4),days);


    }

    public void floorPlannerMenu(int floorNo) {
        int n;
        for(int i = 1;i<= floorNo;i++){
            System.out.println("Floor Planner Menu for Floor " +i);
            do {
                System.out.println("1. Add Rooms");
                if (hasThings.get("Chambers")) {
                    System.out.println("2. Add Chamber");
                }
                if (hasThings.get("OT")) {
                    System.out.println("3. Add Operation Room");
                }
                System.out.println("4. Exit");
                n = Takeintinp("Enter your choice: ");
                switch (n) {
                    case 1:
                        String roomType = Takestrinp("Enter the type of room: ");
                        roomPlanner(i, roomType);
                        if (hasThings.get("Chambers")) {
                            addChamber(i);
                        } else {
                            System.out.println("Chambers are not available in this hospital.");
                        }
                        break;
                    case 3:
                        if (hasThings.get("OT")) {
                            addOperationRoom(i);
                        } else {
                            System.out.println("Operation rooms are not available in this hospital.");
                        }
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (n != 4);
        }
    }


    public void roomPlanner(int floorNo, String roomType) {
        ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorNo + "_" + roomType);
        if (roomsOnFloor == null) {
            roomsOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_" + roomType, roomsOnFloor);
        }

        int roomID = roomsOnFloor.size() + 1;
        String addBeds = Takestrinp("Do you want to add beds Y/N: ").toLowerCase();

        int roomCapacity = Takeintinp("Enter the capacity of the room: ");
        Rooms room = new Rooms(roomID + "_" + roomType, roomType, roomCapacity);
        roomsOnFloor.add(room);

        if (addBeds.equals("y")) {
            addBeds(room, roomID, floorNo);
        }
    }
    public void addOperationRoom(int floorNo) {
        ArrayList<Rooms> theatresOnFloor = Floorplan.get(floorNo + "_OperationRoom");
        if (theatresOnFloor == null) {
            theatresOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_OperationRoom", theatresOnFloor);
        }

        int theatreID = theatresOnFloor.size() + 1;

        int theatreCapacity = Takeintinp("Enter the capacity of the operation theatre: ");
        OperationTheatre theatre = new OperationTheatre(theatreID + "_OperationRoom", theatreCapacity);
        theatresOnFloor.add(theatre);

        System.out.println("Operation theatre added: " + theatre.getTheatreID());
    }


    public void addBeds(Rooms room, int roomID, int floorNo) {
        int numTypesOfBeds = Takeintinp("Enter the number of types of beds you want to have.To exit enter 0: ");
        if(numTypesOfBeds == 0){
            return;
        }
        for (int i = 1; i <= numTypesOfBeds; i++) {
            int numBeds = Takeintinp("Enter the number of beds for this type: ");
            String bedType = Takestrinp("Enter the type of beds: ");
            for (int j = 1; j <= numBeds; j++) {
                Beds bed = new Beds(roomID + "_" + bedType + "_" + j, null, bedType);
                room.occupant.put(bedType, bed);
            }
        }
    }
    public void addChamber(int floorNo) {
        ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorNo + "_Chamber");
        if (roomsOnFloor == null) {
            roomsOnFloor = new ArrayList<>();
            Floorplan.put(floorNo + "_Chamber", roomsOnFloor);
        }

        int roomID = roomsOnFloor.size() + 1;

        int roomCapacity = Takeintinp("Enter the capacity of the chamber: ");
        Chamber chamber = new Chamber(roomID + "_Chamber", roomCapacity);
        roomsOnFloor.add(chamber);

        System.out.println("Chamber added: " + chamber.getChamberID());
    }


public void ScheduleAppointment(String Date,Patient patient,String DoctorID){
        if(Doctor_Registry.containsKey(DoctorID)){
            Doctor doc = Doctor_Registry.get(DoctorID);
            doc.AddPatientToSchedule(Date,patient);

        }
        else{
            System.out.println("Doctor not found");
        }
    }
    public void RegisterDoctor(){
        String name = Takestrinp("Enter the name of the doctor: ");
        String phoneno = Takestrinp("Enter the phone no of the doctor: ");
        String doctorid = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String speaclity = Takestrinp("Enter the speaclity of the doctor: ");
        String emphno = Takestrinp("Enter the emergency no of the doctor: ");
        ArrayList<String> data = new ArrayList<String>();
        data.add(name);
        data.add(phoneno);
        data.add(doctorid);
        data.add(speaclity);
        data.add(emphno);
        int dlimit = Takeintinp("Enter the daily limit of the doctor: ");
        double perpatientcharge = Takeintinp("Enter the per patient charge of the doctor: ");
        Doctor doc = new Doctor(doctorid,name,speaclity,emphno,data,perpatientcharge,dlimit);
        Doctor_Registry.put(doctorid,doc);
        System.out.println("Doctor Registered");
    }
    public void NewBooking(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = Takestrinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim()+"_"+phoneno.substring(8,11).trim();
        String[] docdata = printDocChart();
        printDoctorSchedule(docdata[0],docdata[1]);
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        String enterthedate = Takestrinp("Enter the date of Registration : ");
        if(Patient_Registry.containsKey(addmitno.toLowerCase())){
            ScheduleAppointment(enterthedate,Patient_Registry.get(addmitno),RegisterAseatofDoc);
        }
        else{
            System.out.println("Not Registered");
            ArrayList<String> patientdata = new ArrayList<String>();
            String adress = Takestrinp("Enter the adress of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
            String GurdianPhoneno = Takestrinp("Enter the Gurdian Phone no of the patient: ");
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);

        }
    }
    public void AddOTSchedule(){
        String date = Takestrinp("Enter the date of the operation: ");
        String patientID = Takestrinp("Enter the patient ID: ");
        String doctorID = Takestrinp("Enter the doctor ID: ");
        String operationID = Takestrinp("Enter the operation ID: ");
        String time = Takestrinp("Enter the time of the operation: ");
        FreeOT(date,time);
        String theatreID = Takestrinp("Enter theatre id: ");
        Operation operation = new Operation(patientID, date, operationID, doctorID,time,theatreID);
        Operation_Registry.put(operationID, operation);
        System.out.println("Operation scheduled successfully.");
    }


    public void removeOTSchedule(){
        for(String floorKey:Floorplan.keySet()){
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            for(Rooms room:roomsOnFloor){
                if(room.roomtype.equals("OperationRoom")){
                    OperationTheatre theatre = (OperationTheatre) room;
                    theatre.printOperations();
                }
            }
        }
        String operationID = Takestrinp("Enter the operation ID: ");
        if (Operation_Registry.containsKey(operationID)) {
            Operation operation = Operation_Registry.get(operationID);
            String date = operation.getDate();
            String theatreID = operation.getTheatreid();
            ArrayList<Rooms> theatreFLOOR = Floorplan.get(1);
            System.out.println("Operation removed successfully.");
        } else {
            System.out.println("Operation not found.");
        }
    }
    public String[] printDocChart(){
        System.out.println("Doctors");
        System.out.println("Doc id            Docname            Doctor Speciality");
        for(String l : Doctor_Registry.keySet()){
            Doctor  doc = Doctor_Registry.get(l);
            System.out.printf(doc.doctorid + "     ||  "+doc.name + "       ||"+ "    || "+ doc.speaclity);

        }
        String docname = Takestrinp("Enter the name of the doctor: ");
        String docSpeciality = Takestrinp("Enter the speciality of tbe doctor: ");
        return new String[]{docname,docSpeciality};


    }
    public void printDoctorSchedule(String doctorName, String specialty) {
        String doctorKey = doctorName + "_" + specialty.substring(0,4);

        if (Doctor_Schedule.containsKey(doctorKey)) {
            ArrayList<String> schedule = Doctor_Schedule.get(doctorKey);

            System.out.println("Doctor Schedule for " + doctorName + " (" + specialty + "):");
            System.out.println("--------------------------------------------------");
            System.out.printf("| %-12s | %-12s | %-12s |\n", "Day", "Timing", "Patient ID");
            System.out.println("--------------------------------------------------");

            for (String day : schedule) {
                System.out.println(day);
            }

            System.out.println("--------------------------------------------------");
            System.out.println();
        } else {
            System.out.println("Doctor not found or schedule not available.");
        }
    }
    public void FreeOT(String date,String time){
        for(String floorKey:Floorplan.keySet()){
            ArrayList<Rooms> roomsOnFloor = Floorplan.get(floorKey);
            for(Rooms room:roomsOnFloor){
                if(room.roomtype.equals("OperationRoom")){
                    OperationTheatre theatre = (OperationTheatre) room;
                    if(theatre.availability.containsKey(date+"_"+time)){
                        if(theatre.availability.get(date+"_"+time)){
                            System.out.println("Theatre "+theatre.theatreID+" is available");
                        }
                        else{
                            System.out.println("Theatre "+theatre.theatreID+" is not available");
                        }
                    }
                    else{
                        System.out.println("Theatre "+theatre.theatreID+" is available");
                    }
                }
            }
        }
    }
    public void RemovePatientBooking(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = Takestrinp("Enter the phone no of the patient: ");
        String admitno = name.substring(0).trim() +"_"+phoneno.substring(8,11).trim();
        String date = Takestrinp("Enter the date of the appointment: ");
        Patient pat = Patient_Registry.get(admitno);
        String docid = pat.getAppointment(date);
        pat.removeAppointment(docid);
        Doctor doc = Doctor_Registry.get(docid);
        doc.RemovePatientReg(date,pat);


    }

    public void mainMeu(){
        int n = 0;
        do{
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("Welcome to the admin menu of "+clinic_managementSystem );
            System.out.println("1. Patient Functions");
            System.out.println("2. Staff Functions");
            System.out.println("3. Management and Infrastructure");
            System.out.println("4. Financials");
            System.out.println("5. Export Data");
            System.out.println("6. Exit");
             n = Takeintinp("-> ");
             switch (n){
                 case 1:

                     PatientMenu();
                     break;
                 case 2:
                     StaffFunctions();
                     break;
                 case 3:
                     Management();
                     break;
                 case 4:
                     FinancialsMenu();
                     break;
                 case 5:
                     ExportData();
                     break;
                 case 6:
                     return;
                 default:
                     n = Takeintinp("Enter a number in range 1 and 6:");

             }

        }
        while(n!= 6);
    }

    private void FinancialsMenu() {
        int n= 0;
        do{
            System.out.println("Welcome to the Financial Menu of "+clinic_managementSystem+": " );
            System.out.println("1.Add a new income");
            System.out.println("2.Add a new expense");
            System.out.println("3.Modify Income");
            System.out.println("4.Modify Expense");
            System.out.println("5.");
        }while (n!= 5);
    }

    private void ExportData() {
    }

    private void Management() {
    }

    private void StaffFunctions() {
    }

    public void PatientMenu(){
        int n= 0;
        System.out.print("\033[H\033[2J");
        do{
            System.out.println("Welcome to the Patient menu of "+clinic_managementSystem );
            System.out.println("1.Add Patient");
            System.out.println("2.Schedule Appointment");
            System.out.println("3.Remove Appointment");
            System.out.println("4.Schedule OT");
            System.out.println("5.Remove OT");
            System.out.println("6. Exit");
            int x = Takeintinp("->");
            switch (x){
                case 1:
                    RegisterPatient();
                    break;
                case 2:
                    NewBooking();
                case 3:
                    RemovePatientBooking();
                case 4:
                    AddOTSchedule();
                case 5:
                    removeOTSchedule();
                case 6:
                    return;
                default:
                    x = Takeintinp("Enter a number between (1-6): ");
            }

        }
        while (n != 6);

    }
    

}
