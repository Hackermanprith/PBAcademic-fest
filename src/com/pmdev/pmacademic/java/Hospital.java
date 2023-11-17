package com.pmdev.pmacademic.java;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Hospital {
    static HashMap<String, ArrayList<Rooms>> Floorplan;
    static HashMap<String ,Patient> Patient_Registry;
    HashMap<String,Doctor>Doctor_Registry;
     static Map<String, ArrayList<String>> doctorSchedules;
    HashMap<String,Staff>Staff_Registry;
    String clinic_managementSystem = null;
    Financials financials;
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
    public  String takesphinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        String number;
        number = sc.next();
        System.out.println();
        if (number.length() != 10) {
            number = takesphinp("Invalid Input, Try again, please enter a 11 digit number: ");
        }
        return number;
    }
    private  void PrintSchedule(String docid){
        Doctor doc = findDoctorById(docid);
        if(doc == null){
            System.out.println("Doctor not found");
            return;
        }
        System.out.println("----------------------------------------------------------------");
        String formatsepcifier = "| %-9s | %-20s | %-20s |";
        System.out.printf(formatsepcifier,"Day","Shift 1","Shift 2");
        System.out.println();
        System.out.println("----------------------------------------------------------------");
        for(int i = 1;i<=7;i++){
            switch (i){
                case 1:
                    System.out.println();
                    System.out.printf(formatsepcifier,"Monday ",doctorSchedules.get(docid).get(0).split(",")[0],doctorSchedules.get(docid).get(0).split(",")[1]);
                    System.out.println();
                    break;
                case 2:
                    System.out.printf(formatsepcifier,"Tuesday",doctorSchedules.get(docid).get(1).split(",")[0],doctorSchedules.get(docid).get(1).split(",")[1]);
                    System.out.println();
                    break;
                case 3:
                    System.out.printf(formatsepcifier,"Wednesday",doctorSchedules.get(docid).get(2).split(",")[0],doctorSchedules.get(docid).get(1).split(",")[1]);
                    System.out.println();
                    break;
                case 4:
                    System.out.printf(formatsepcifier,"Thursday", doctorSchedules.get(docid).get(3).split(",")[0],doctorSchedules.get(docid).get(3).split(",")[1]);
                    System.out.println();
                    break;
                case 5:
                    System.out.printf(formatsepcifier,"Friday",doctorSchedules.get(docid).get(4).split(",")[0],doctorSchedules.get(docid).get(4).split(",")[1]);
                    System.out.println();
                    break;
                case 6:
                    System.out.printf(formatsepcifier,"Saturday",doctorSchedules.get(docid).get(5).split(",")[0],doctorSchedules.get(docid).get(5).split(",")[1]);
                    System.out.println();
                    break;
                case 7:
                    System.out.printf(formatsepcifier,"Sunday: ",doctorSchedules.get(docid).get(6).split(",")[0],doctorSchedules.get(docid).get(6).split(",")[1]);
                    System.out.println();
                    break;
            }
        }
    }
    public void header(String menuname){
        String text = "Welcome to the " +menuname+"of "+clinic_managementSystem +" \n by Methodist School Dankuni";

        try {
            // Get the terminal width using System.console()
            int terminalWidth = 16;

            int leftPadding = (terminalWidth - text.length()) / 2;
            String centeredText = String.format("%" + (leftPadding + text.length()) + "s", text);

            System.out.println(centeredText);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    Hospital() {
        Patient_Registry = new HashMap<>();
        Doctor_Registry = new HashMap<>();
        Staff_Registry = new HashMap<>();
        Floorplan = new HashMap<>();
        header("Clinic Management System ");
        clinic_managementSystem = Takestrinp("Enter the name of the clinic: ");
        doctorSchedules = new HashMap<>();
        int nooffloors = Takeintinp("Enter the number of floors: ");
        floorPlannerMenu(nooffloors,false);
        System.out.print("\u000c");
        financials = new Financials();
        System.out.println("Please enter at least 1 doc");
        RegisterDoctor();
        System.out.println("Please enter at least 1 staff");
        //TODO: Patient default
        ArrayList<String> testhistory = new ArrayList<>();
        testhistory.add("Cancer");
        testhistory.add("More Cancer");
        testhistory.add("Cancer ++");
        Patient pat = new Patient("default","default","default","default","default","default","default","default","default",testhistory);
        Patient_Registry.put("default_0",pat);
        //TODO: Display docschedule in table format

        RegisterStaff();
        mainMeu();


    }
    public static void printFloorPlan() {
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
            System.out.println();
            System.out.println("Rooms on floor");
            for(Rooms room: roomsOnFloor){
                System.out.println("Room " + room.roomid + " - " + room.roomtype + " - Capacity: " + room.roomsidecapacity);
            }
            System.out.println();
            System.out.println("Chambers on floor: ");
            for (Rooms room : roomsOnFloor) {
                if (room.roomtype.equals("Chamber")) {
                    Chamber chamber = (Chamber) room;
                    System.out.println("Chamber " + chamber.getChamberID());
                }
            }
            for(Rooms room: roomsOnFloor){
                System.out.println("Room " + room.roomid + " - " + room.roomtype + " - Capacity: " + room.roomsidecapacity);
            }
            System.out.println();
        }
    }
    public void floorPlannerMenu(int floorNo,boolean refferenced) {
        int n;
        if(refferenced){
            printFloorPlan();
        }
        for(int i = 1;i<= floorNo;i++){
            System.out.println("Floor Planner Menu for Floor " +i);
            do {
                System.out.println("1. Add Rooms");
                System.out.println("2. Add Chamber");
                System.out.println("3. Exit");
                n = Takeintinp("Enter your choice: ");
                switch (n) {
                    case 1:
                        String roomType = Takestrinp("Enter the type of room: ");
                        roomPlanner(i, roomType);
                        break;
                    case 2:
                        addChamber(i);
                        break;
                    case 3:
                        continue;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (n != 3);
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
        System.out.println("Adding chamber on floor " + floorNo);
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
            financials.addIncome(String.valueOf("Doctors visit by"+ patient.addmitno), doc.perpatientcharge, (doc.perpatientcharge-doc.clinicshare), true);
            doc.earned+=doc.perpatientcharge-doc.clinicshare;
        }
        else{
            System.out.println("Doctor not found");
        }
    }
    public void RegisterDoctor(){
        String name = Takestrinp("Enter the name of the doctor: ");
        String phoneno = takesphinp("Enter the phone no of the doctor: ");
        String doctorid = name.substring(0).trim()+"_"+phoneno.substring(6,9).trim();
        if(Doctor_Registry.containsKey(doctorid)){
            System.out.println("Doctor already exists");
            return;
        }
        String speaclity = Takestrinp("Enter the speaclity of the doctor: ");
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
        if(clinicshare > perpatientcharge){
            System.out.println("Clinic share cannot be greater than per patient charge");
            clinicshare = Takeintinp("Enter the clinic share of the doctor: ");
        }
        ArrayList<String>Date = new ArrayList<>();
        for(int i = 1;i<=7;i++){
            String n = Takestrinp("Enter the timings for day"+i+"of the week,separate shift by (,): ");
            Date.add(n);
        }
        doctorSchedules.put(doctorid,Date);
        Doctor doc = new Doctor(doctorid,name,speaclity,emphno,data,perpatientcharge,dlimit,clinicshare,Date);
        Doctor_Registry.put(doctorid,doc);
        System.out.println("Doctor Registered");
    }
    public void PrintDocPatients(){
        String docid = Takestrinp("Enter the doctor id (if all then type all): ");
        if (docid.equals("all")) {
            for (String doctorId : Doctor_Registry.keySet()) {
                Doctor doc = Doctor_Registry.get(doctorId);
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
        else{
            Doctor doc = Doctor_Registry.get(docid);
            if(doc == null){
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
    public void NewBooking(){
        if(Doctor_Registry.isEmpty()){
            System.out.println("No doctors in the clinic");
            return;
        }
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim()+"_"+phoneno.substring(6,9).trim();
        printDocChart();
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        if(!Doctor_Registry.containsKey(RegisterAseatofDoc)){
            System.out.println("Doctor not found");
            return;
        }
        PrintSchedule(RegisterAseatofDoc);
        String enterthedate = Takestrinp("Enter the date of Registration : ");
        if(Patient_Registry.containsKey(addmitno.toLowerCase())){
            ScheduleAppointment(enterthedate,Patient_Registry.get(addmitno),RegisterAseatofDoc);
        }
        else{
            String adress = Takestrinp("Enter the address of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
            String GurdianPhoneno = takesphinp("Enter the Gurdian Phone no of the patient: ");
            String yn = Takestrinp("Do you want to add the patients medical history Y/N: ");
            ArrayList<String>MedicalHistory = new ArrayList<>();
            if(yn.trim().toLowerCase().equals("y")){
                String k = "";
                System.out.println("To exit type out and for new lines just press enter: ");
                while(!k.trim().toLowerCase().equals("out")){
                    k = Takestrinp("Enter the medical history: ");
                    MedicalHistory.add(k);
                }
            }
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email,MedicalHistory);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);

        }
    }
    public void printDocChart() {
     System.out.println("\u000c");
     header("Doctor View Menu");
     System.out.println("Doctors");
     System.out.println("------------------------------------------------------------------------------");
     System.out.printf("| %-12s | %-20s | %-20s |\n", "ID", "Doctor name", "Doctor Speciality");
     System.out.println("-------------------------------------------------------------------------------");

     for (String doctorId : Doctor_Registry.keySet()) {
         Doctor doc = Doctor_Registry.get(doctorId);
         System.out.printf("| %-12s | %-20s | %-20s |\n", doc.doctorid, doc.name, doc.speaclity);
     }
     System.out.println("--------------------------------------------------------");

 }
    public void RemovePatientBooking(){
        System.out.println("\u000c");
        header("Patient Removal Menu");
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String admitno = name.substring(0).trim() +"_"+phoneno.substring(6,9).trim();
        if(validatePatient(name,phoneno)){
            System.out.println("No such patient found");
            return;
        }
        String date = Takestrinp("Enter the date of the appointment: ");
        Patient pat = Patient_Registry.get(admitno);
        try{
            String docid = pat.getAppointment(date);
            pat.removeAppointment(docid);
            Doctor doc = Doctor_Registry.get(docid);
            doc.RemovePatientReg(date,pat);
        }
        catch (Exception e){
            System.out.println("No appointment was there");
        }

    }
    //done
    public void mainMeu(){
        int n = 0;
        do{
            System.out.print("\u000c");
            System.out.flush();
            header("Admin Menu");
            System.out.println("1. Patient Functions");
            System.out.println("2. Management and Infrastructure");
            System.out.println("3. Financials");
            System.out.println("4. Export Data(Not available yet)");
            System.out.println("5. Exit");
             n = Takeintinp("-> ");
             switch (n){
                 case 1:
                     PatientMenu();
                     break;
                 case 2:
                     Management();
                     break;
                 case 3:
                     FinancialsMenu();
                     break;
                 case 4:
                     System.out.println("This feature is not available yet.");
                     break;
                 case 5:
                     return;
                 default:
                     System.out.println("Enter a number between 1-5");

             }

        }
        while(n!= 6);
    }
    //done
    private void FinancialsMenu() {
        System.out.println("\u000c");
        int n= 0;
        do{
            System.out.println("Welcome to the Financial Menu of "+clinic_managementSystem+": " );
            double [] arr = financials.quickshotdata();
            System.out.println("Expenses: "+arr[0]);
            System.out.println("Income: "+arr[1]);
            System.out.println("Available balance: "+(arr[1]-arr[0]));
            System.out.println("1.Add a new income");
            System.out.println("2.Add a new expense");
            System.out.println("3.Modify Income");
            System.out.println("4.Modify Expense");
            System.out.println("5.Show Financials");
            System.out.println("6.Pay roll");
            System.out.println("7.Exit");
            n = Takeintinp("->");
            switch (n){
                case 1:
                    String incomename= Takestrinp("Source of income: ");
                    double cost = Takeintinp("Cost of the income: ");
                    boolean hosexp = Takestrinp("Does it have a hospital expense Y/N: ").toLowerCase().equals("y");
                    double hosexpense = 0.0;
                    if(hosexp){
                        hosexpense = Takeintinp("Enter the hospital expense: ");
                    }
                    financials.addIncome(incomename,cost,hosexpense,hosexp);
                    break;
                case 2:
                    String expensename= Takestrinp("Source of expense: ");
                    double expensecost = Takeintinp("Cost of the expense: ");
                    financials.addExpense(expensename,expensecost);
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
            }
        }while (n!= 6);
    }

    private void releasepayroll() {
        System.out.println("\u000c");
        for(Doctor doc:Doctor_Registry.values()){
            System.out.println("Doctor "+doc.name+" has earned "+doc.earned);
            System.out.println("Paying Doctor "+doc.name+" "+doc.earned);
            financials.addExpense(String.valueOf("Paying"+doc.name),doc.earned);
            doc.earned = 0;
        }
        for(Staff staff:Staff_Registry.values()){
            System.out.println("Staff "+staff.staffname+" has earned "+staff.staffsalary);
            System.out.println("Paying Staff "+staff.staffname+" "+staff.staffsalary);
            financials.addExpense(String.valueOf("Paying"+staff.staffname),staff.staffsalary);
        }
    }
    private void Management() {
        System.out.print("\u000c");
        int n = 0;
        do{
            System.out.println("Welcome to the Management and Infrastructure menu of "+clinic_managementSystem );
            System.out.println("1.Add Doctor");
            System.out.println("2.Add Staff");
            System.out.println("4.Show all doctors");
            System.out.println("5.Show all staff");
            System.out.println("6.Change Doctor things");
            System.out.println("7.Change Staff things");
            System.out.println("8.Remove Doctor");
            System.out.println("9.Remove staff");
            System.out.println("10.Print Floor plan");
            System.out.println("11.Change floor plan");
            System.out.println("12.View Doc's Schedule");
            System.out.println("13. Exit");
            n = Takeintinp("-> ");
            switch (n){
                case 1:
                    RegisterDoctor();
                    break;
                case 2:
                    RegisterStaff();
                    break;
                case 3:
                    addChamber(1);
                    break;
                case 4:
                    for(Doctor doc:Doctor_Registry.values()){
                        doc.printData();
                    }
                    break;
                case 5:
                    for(Staff staff:Staff_Registry.values()){
                        staff.printData();
                    }
                    break;
                case 6:
                    doctorMenu();
                    break;
                case 7:
                    staffMenu();
                    break;
                case 8:
                    for(Doctor doc:Doctor_Registry.values()){
                        doc.printData();
                    }
                    String doctorId = Takestrinp("Enter Doctor ID to remove for (exiting type exit): ");
                    if(doctorId.trim().toLowerCase().equals("exit")){
                        System.out.println("Exiting");
                        return;
                    }
                    Doctor doctor = findDoctorById(doctorId);
                    if (doctor != null) {
                        if(doctor.earned > 0){
                            System.out.println("Do you want to pay the doctor Y/N: ");
                            String choice = Takestrinp("-> ");
                            if(choice.toLowerCase().equals("y")){
                                financials.addExpense(String.valueOf("Paying"+doctor.name),doctor.earned);
                            }
                        }
                        for(String x  : doctor.Patient_Registry.keySet()) {
                            ArrayList<Patient> pat = doctor.Patient_Registry.get(x);
                            for (Patient patient : pat) {
                                patient.removeAppointment(x);System.out.println("Cancelled appointment for" + patient.patientdata.get(0) + "on " + x);
                            }
                        }
                        Doctor_Registry.remove(doctorId);
                        System.out.println("Doctor removed successfully.");

                    } else {
                        System.out.println("Doctor not found with ID: " + doctorId);
                    }
                    break;
                case 9:
                    for(Staff staffu:Staff_Registry.values()){
                        staffu.printData();
                    }
                    String staffId1 = Takestrinp("Enter Staff ID to remove(exiting type exit): ");
                    if(staffId1.trim().toLowerCase().equals("exit")){
                        System.out.println("Exiting");
                        return;
                    }
                    Staff staffer = findStaffById(staffId1);
                    String answer = Takestrinp("Do you want to pay the staff his usual salary of "+staffer.staffsalary +"(Y/N) :");
                    if(answer.trim().toLowerCase().equals("y")){
                        financials.addExpense(String.valueOf("Paying"+staffer.staffname),staffer.staffsalary);
                    }
                    Staff_Registry.remove(staffId1);
                    break;
                case 10:
                    printFloorPlan();
                    break;
                case 11:
                    System.out.println("Want to add a new room (Y/N) ");
                    String choice = Takestrinp("-> ");
                    if(choice.toLowerCase().equals("Y")){
                        floorPlannerMenu(Floorplan.size()+2,true);
                    }
                    System.out.println("Floor modification menu: ");
                    int floorNo = Takeintinp("Enter the floor number: ");
                    printFloorPlan();
                    floorPlannerMenu(floorNo,true);
                    break;
                case 12:
                    for(Doctor doc:Doctor_Registry.values()){
                        doc.printData();
                    }
                    PrintSchedule(Takestrinp("Enter the doctor id: "));
                    PrintDocPatients();
                    break;
                case 13:
                    return;
                default:
                    System.out.println("Enter a number between 1 and 11 !!!");
            }

        }
        while (n != 9);
    }
    private void RegisterStaff() {
        System.out.println("\u000c");
        String name = Takestrinp("Enter the name of the staff: ");
        String phoneno = takesphinp("Enter the phone no of the staff: ");
        String staffid = name.substring(0).trim()+"_"+phoneno.substring(6,9).trim();
        if(validateStaff(name,phoneno)){
            System.out.println("Staff already exists");
            return;
        }
        String speaclity = Takestrinp("Enter the speaclity of the staff: ");
        String emphno = Takestrinp("Enter the emergency no of the staff: ");
        double x = Takeintinp("Enter the salary of the staff: ");
        ArrayList<String> data = new ArrayList<String>();
        data.add(name);
        data.add(phoneno);
        data.add(staffid);
        data.add(speaclity);
        data.add(emphno);
        Staff staff = new Staff(name,speaclity,emphno,data,x);
        Staff_Registry.put(staffid,staff);
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
                System.out.println("4.Change Staff Salary");
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
        for(Doctor doc:Doctor_Registry.values()){
            doc.printData();
        }
        System.out.print("Enter Doctor ID to modify: ");
        String doctorId = scanner.nextLine();

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
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()

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
        return null;
    }
    public void PatientMenu(){
        System.out.print("\u000c");
        do{
            System.out.println("Welcome to the Patient menu of "+clinic_managementSystem );
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
            System.out.println("11.Schedule OT(coming soon)");
            System.out.println("12.Remove OT(coming soon)");
            System.out.println("13. Exit");
            int x = Takeintinp("->");
            switch (x){
                case 1:
                    NewBooking();
                    break;
                case 2:
                    RemovePatientBooking();
                    break;
                case 3:
                    for(Patient pat12:Patient_Registry.values()){
                        pat12.printPatientDetails();
                    }
                    String name = Takestrinp("Enter the name of the patient: ");
                    String phoneno = takesphinp("Enter the phone no of the patient: ");
                    if(validatePatient(name,phoneno)){
                        System.out.println("No patient found");
                        return;
                    }
                    Showpatientdetails(name,phoneno);
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
                    if(validatePatient(name,phoneno)) {
                        System.out.println("No patient found");
                        return;
                    }
                    Patient pat = Patient_Registry.get(name.substring(0).trim()+"_"+phoneno.substring(6,9).trim());
                    pat.addMedicalHistory();
                    break;
                case 7:
                    name= Takestrinp("Enter the name of the patient: ");
                    phoneno= takesphinp("Enter the phone no of the patient: ");
                    String admitno = name.substring(0).trim() +"_"+phoneno.substring(6,9).trim();
                    Patient pat1 = Patient_Registry.get(admitno);
                    if(pat1 == null){
                        System.out.println("No patient found");
                        return;
                    }
                    pat1.printMedicalHistory();
                    break;
                case 8:
                    System.out.println("\u000c");
                    System.out.println("Patient List");
                    for(Patient pat12:Patient_Registry.values()){
                        pat12.printPatientDetails();
                    }
                    Billoutpatient();
                    break;
                case 9:
                    System.out.println("\u000c");
                    System.out.println("Patient List");
                    for(Patient pat12:Patient_Registry.values()){
                        pat12.printPatientDetails();
                    }
                    name = Takestrinp("Enter the name of the patient: ");
                    phoneno = takesphinp("Enter the phone no of the patient: ");
                    if(validatePatient(name,phoneno)){
                        System.out.println("No patient found");
                        return;
                    }
                    Showpatientdetails(name,phoneno);
                    ModifyPatientMenu(name,phoneno);
                    break;
                case 10:
                    System.out.println("\u000c");
                    System.out.println("Patient List");
                    for(Patient pat12:Patient_Registry.values()){
                        pat12.printPatientDetails();
                    }
                    break;
                case 11,12:
                    System.out.println("This feature is not available yet.");
                    break;
                case 13:
                    return;
                default:
                    System.out.println("Enter a number");
            }

        }
        while (true);

    }
    private void Billoutpatient() {
        String patname = Takestrinp("Enter the name of the patient: ");
        String patphoneno = takesphinp("Enter the phone no of the patient: ");
        if(validatePatient(patname,patphoneno)){
            System.out.println("No patient found");
            return;
        }
        Patient pat = Patient_Registry.get(patname.substring(0).trim()+"_"+patphoneno.substring(6,9).trim());
        pat.printPatientDetails();
        pat.printBillableServices();
        System.out.println("-----------------------------------------------");
        System.out.println("Total Bill: "+pat.getBill());
        System.out.println("-----------------------------------------------");
    }
    private void ModifyPatientMenu(String name, String phoneno) {
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
        Patient pat = Patient_Registry.get(name.substring(0).trim()+"_"+phoneno.substring(6,9).trim());
        switch (x){
            case 1:
                String newname = Takestrinp("Enter the new name of the patient: ");
                String newphoneno = takesphinp("Enter the new phone no of the patient: ");
                String newaddmitno = newname.substring(0).trim()+"_"+newphoneno.substring(6,9).trim();
                Patient_Registry.remove(name.substring(0).trim()+"_"+phoneno.substring(6,9).trim());
                pat.addmitno = newaddmitno;
                pat.patientdata.set(0,newname);
                pat.patientdata.set(1,newphoneno);
                Patient_Registry.put(newaddmitno,pat);
                break;
            case 2:
                String newphoneno1 = takesphinp("Enter the new phone no of the patient: ");
                Patient_Registry.remove(name.substring(0).trim()+"_"+phoneno.substring(6,9).trim());
                pat.patientdata.set(1,newphoneno1);
                Patient_Registry.put(name.substring(0).trim()+"_"+newphoneno1.substring(6,9).trim(),pat);
                break;
            case 3:
                String newadd = Takestrinp("Enter the new address of the patient: ");
                pat.patientdata.set(3,newadd);
                break;
            case 4:
                String newemail = Takestrinp("Enter the new email of the patient: ");
                pat = Patient_Registry.get(name.substring(0).trim()+"_"+phoneno);
                pat.patientdata.set(4,newemail);
                break;
            case 5:
                String newguardian = Takestrinp("Enter the new guardian of the patient: ");
                pat.patientdata.set(5,newguardian);
                break;
            case 6:
                String newguardianphno = Takestrinp("Enter the new guardian phone no of the patient: ");
                pat.patientdata.set(6,newguardianphno);
                break;
            case 7:
                pat.printBillableServices();
                String service = Takestrinp("Enter the name of the service: ");
                int cost = Takeintinp("Enter the cost of the service: ");
                if(pat.billable_services.containsKey(service+"_"+cost)){
                    int y  = pat.billable_services.get(service+"_"+cost);
                    y+=cost;
                    pat.billable_services.put(service+"_"+cost,y);
                    return;
                }
                pat.billable_services.put(service,cost);
                break;
            case 8:
                pat.printMedicalHistory();
                System.out.println("1. Add a new medical history");
                System.out.println("2. Modify a medical history");
                System.out.println("3. Remove a medical history");
                int y = Takeintinp("-> ");
                switch (y){
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
                ModifyPatientMenu(name1,phone1);
                return;
            case 10:
                pat.printPatientDetails();
                break;
            case 11:
                System.out.println("\u000c");
                header("Patient Modification Menu");
                String name2 = Takestrinp("Enter the name of the patient: ");
                String phone2 = takesphinp("Enter the phone no of the patient: ");
                if(validatePatient(name2,phone2)){
                    System.out.println("No patient found");
                    break;
                }
                ModifyPatientMenu(name2,phone2);
                return;
            case 12:
                System.out.println("Exiting");
                System.out.println("\u000c");
                return;
            default:
                System.out.println("Enter a number between 1-7");
        }

    }
    private void Showpatientdetails(String name,String phoneno){
        String admitno = name.substring(0).trim() +"_"+phoneno.substring(6,9).trim();
        Patient pat = Patient_Registry.get(admitno);
        if(pat != null){
            pat.printPatientDetails();
        }
        else{
            System.out.println("Patient not found");
        }
    }
    private void Removepat(){
        System.out.println("\u000c");
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String admitno = name.substring(0).trim() +"_"+phoneno.substring(6,9).trim();
        Patient pat = Patient_Registry.get(admitno);
        if(!pat.billable_services.isEmpty()){
            int billable = 0;
            for(int i : pat.billable_services.values()){
                billable+=i;
            }
            System.out.println("Patient has billable services of "+billable);
            System.out.println("Do you want to pay the bill Y/N: ");
            String choice = Takestrinp("-> ");
            if(choice.toLowerCase().equals("y")){
                financials.addExpense(String.valueOf("Paying"+pat.patientdata.get(0)),billable);
            }
            else{///
                System.out.println("Patient not removed");
                return;
            }
        }
        if(pat != null){
            Patient_Registry.remove(admitno);
            for(Doctor doc:Doctor_Registry.values()){
                for(String x  : doc.Patient_Registry.keySet()) {
                    ArrayList<Patient> pat1 = doc.Patient_Registry.get(x);
                    for (Patient patient : pat1) {
                        if(patient.addmitno.equals(admitno)){
                            patient.removeAppointment(x);
                            System.out.println("Cancelled appointment for" + patient.patientdata.get(0) + "on " + x);
                        }
                    }
                }
            }
            System.out.println("Patient removed");
        }
        else{
            System.out.println("Patient not found");
        }

    }
    private void Addabillableservice(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String admitno = name.substring(0).trim() +"_"+phoneno.substring(6,9).trim();
        Patient pat = Patient_Registry.get(admitno);
        if(pat != null){
            String service = Takestrinp("Enter the name of the service: ");
            int cost = Takeintinp("Enter the cost of the service: ");
            if(pat.billable_services.containsKey(service+"_"+cost)){
                int x  = pat.billable_services.get(service+"_"+cost);
                x+=cost;
                pat.billable_services.put(service+"_"+cost,x);
                return;
            }
            pat.billable_services.put(service,cost);
        }
        else{
            System.out.println("Patient not found");
        }
        
    }
    private boolean validatePatient(String name,String phoneno){
        if(Patient_Registry.containsKey(name.substring(0).trim()+"_"+phoneno.substring(6,9).trim())){
            return false;
        }
        return true;
    }
    private boolean validateStaff(String name,String phoneno){
        if(Staff_Registry.containsKey(name.substring(0).trim()+"_"+phoneno.substring(6,9).trim())){
            return true;
        }
        return false;
    }
}
