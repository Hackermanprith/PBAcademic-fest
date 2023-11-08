package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Hospital {
    static HashMap<String, ArrayList<Rooms>> Floorplan;
    HashMap<String, Operation> Operation_Registry;
    HashMap<String,Boolean> hasThings;
    HashMap<String ,Patient> Patient_Registry;
    HashMap<String,Doctor>Doctor_Registry;
    HashMap<String,ArrayList<String>> Doctor_Schedule;
    public static int Takeintinp(String msg) {
        System.out.println(msg);
        Scanner sc = new Scanner(System.in);
        int number = 0;
        try {
            number = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid Input, Try again");
            number = Takeintinp(msg);
        }
        return number;
    }

    public static String Takestrinp(String msg) {
        System.out.println(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    Hospital() {
        Patient_Registry = new HashMap<>();
        Doctor_Registry = new HashMap<>();
        Floorplan = new HashMap<>();
        hasThings = new HashMap<>();
        int n;
        System.out.println("Welcome to the Clinic Management System");
        String haslab = Takestrinp("Do you have a lab Y/N: ");
        hasThings.put("Lab",haslab.toLowerCase().equals("y"));
        String hasoperation = Takestrinp("Do you have a operation room Y/N: ");
        hasThings.put("OT",hasoperation.toLowerCase().equals("y"));
        String haswaiting = Takestrinp("Do you have bed's Y/N: ");
        hasThings.put("Chambers",haswaiting.toLowerCase().equals("y"));



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

    public void floorPlannerMenu(int floorNo) {
        int n;
        System.out.println("Floor Planner Menu for Floor " + floorNo);
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
                    roomPlanner(floorNo, roomType);
                    break;
                case 2:
                    if (hasThings.get("Chambers")) {
                        addChamber(floorNo);
                    } else {
                        System.out.println("Chambers are not available in this hospital.");
                    }
                    break;
                case 3:
                    if (hasThings.get("OT")) {
                        addOperationRoom(floorNo);
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
        int numTypesOfBeds = Takeintinp("Enter the number of types of beds you want to have: ");
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
    public void Mainmenu(){
        while (true) {
            System.out.println("Main Menu");
            System.out.println("1. Add Doctor");
            System.out.println("2. Add a Patient Schedule");
            System.out.println("3. Patient Modifications");
            System.out.println("4. Add a new Operation");
            System.out.println("5. Add Staff");
            System.out.println("6. Modify Staff");
            System.out.println("7. Finances");
            System.out.println("8. Exit");
            System.out.println("6. Exit");

            int choice = Takeintinp("Enter your choice: ");
            switch (choice) {
                case 1:
                    RegisterDoctor();
                    break;
                case 2:
                    NewBooking();
                    break;
                case 3:
                    RegisterPatient();
                    break;
                case 4:
                    //AddOperationRoom(1); // Assuming floor 1 for OT
                    break;
                case 5:
                    //AddStaff();
                    break;
                case 6:
                    System.out.println("Exiting the Clinic Management System.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
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
        printDocChart();
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
        printFreeOT();
        String date = Takestrinp("Enter the date of the operation: ");
        String theatreID = Takestrinp("Enter the theatre ID: ");
        String patientID = Takestrinp("Enter the patient ID: ");
        String doctorID = Takestrinp("Enter the doctor ID: ");
        String operationID = Takestrinp("Enter the operation ID: ");
        String time = Takestrinp("Enter the time of the operation: ");
        Operation operation = new Operation(patientID, date, operationID, doctorID,time,theatreID);
        Operation_Registry.put(operationID, operation);
        System.out.println("Operation scheduled successfully.");
    }

    private void printFreeOT() {
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
                OperationTheatre theatre  = (OperationTheatre) Floorplan.get(theatreID);
                theatre.removeOperation(date, operation);
            Operation_Registry.remove(operationID);
            System.out.println("Operation removed successfully.");
        } else {
            System.out.println("Operation not found.");
        }
    }
    public void printDocChart(){

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

}
