package com.pmdev.pmacademic.java;

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
    public static String takesphinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        String number;
        number = sc.next();
        System.out.println();
        if (number.length() != 10) {
            number = takesphinp("Invalid Input, Try again, please enter a 11 digit number");
        }
        return number;
    }
    public static void PrintSchedule(String docid){
        for(int i = 1;i<=7;i++){
            switch (i){
                case 1:
                    System.out.println("Monday: "+      doctorSchedules.get(docid).get(0));
                    break;
                case 2:
                    System.out.println("Tuesday: "+      doctorSchedules.get(docid).get(1));
                    break;
                case 3:
                    System.out.println("Wednesday: "+doctorSchedules.get(docid).get(2));

                    break;
                case 4:
                    System.out.println("Thursday: "+   doctorSchedules.get(docid).get(3));
                    break;
                case 5:
                    System.out.println("Friday: "+   doctorSchedules.get(docid).get(4));
                    break;
                case 6:
                    System.out.println("Saturday: "+doctorSchedules.get(docid).get(5));
                    break;
                case 7:
                    System.out.println("Sunday: "+ doctorSchedules.get(docid).get(6));
                    break;
            }
        }
    }

    public void header(String menuname){
        String text = "Welcome to Clinic Management System \n by Methodist School Dankuni";
        if(clinic_managementSystem != null){
            text = "Welcome to the " +menuname+"of "+clinic_managementSystem +" \n by Methodist School Dankuni";
        }


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
        header("Clinic Management System");
        clinic_managementSystem = Takestrinp("Enter the name of the clinic: ");
        doctorSchedules = new HashMap<>();
        int nooffloors = Takeintinp("Enter the number of floors: ");
        floorPlannerMenu(nooffloors);
        System.out.print("\u000c");
        financials = new Financials();
        System.out.println("Please enter at least 1 doc");
        RegisterDoctor();
        System.out.println("Please enter at least 1 staff");
        RegisterStaff();
        mainMeu();


    }

    public void RegisterPatient(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String addmitno = name.trim()+"_"+phoneno.substring(6,9).trim();
        printDocChart();
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        PrintSchedule(RegisterAseatofDoc);
        String enterthedate = Takestrinp("Enter the date of Registration : ");
        if(Patient_Registry.containsKey(addmitno.toLowerCase())){
            ScheduleAppointment(enterthedate,Patient_Registry.get(addmitno),RegisterAseatofDoc);
        }
        else{
            System.out.println("Not Registered");
            String adress = Takestrinp("Enter the adress of the patient: ");
            String email = Takestrinp("Enter the email of the patient: ");
            String Gurdian = Takestrinp("Enter the Gurdian of the patient: ");
            String GurdianPhoneno = Takestrinp("Enter the Gurdian Phone no of the patient: ");
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);
        }


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
            System.out.println("Chambers on floor: ");
            for (Rooms room : roomsOnFloor) {
                if (room.roomtype.equals("Chamber")) {
                    Chamber chamber = (Chamber) room;
                    System.out.println("Chamber " + chamber.getChamberID());
                }
            }
            System.out.println();
            System.out.println("Operation Theatres on floor: ");
        }
    }

    public void floorPlannerMenu(int floorNo) {
        int n;
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
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (true);
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
        double clinicshare = Takeintinp("Enter the clinic share of the doctor: ");
        if(clinicshare > perpatientcharge){
            System.out.println("Clinic share cannot be greater than per patient charge");
            clinicshare = Takeintinp("Enter the clinic share of the doctor: ");
        }
        Doctor doc = new Doctor(doctorid,name,speaclity,emphno,data,perpatientcharge,dlimit,clinicshare);
        Doctor_Registry.put(doctorid,doc);
        ArrayList<String>Date = new ArrayList<>();
        for(int i = 1;i<=7;i++){
           String n = Takestrinp("Enter the timings for day"+i+"of the week: ");
           Date.add(n);
        }
        doctorSchedules.put(doctorid,Date);

        System.out.println("Doctor Registered");
    }
    public void PrintDocPatients(String docid){
        Doctor doc = Doctor_Registry.get(docid);
        for(String date: doc.Patient_Registry.keySet()){
            ArrayList<Patient> arr = doc.Patient_Registry.get(date);
            System.out.println("-------------------------------------------------------------------");
            System.out.printf("| %-12s | %-12s | \n","Date","Patient");
            System.out.println("-------------------------------------------------------------------");
            for(Patient pat : arr){
                System.out.printf("| %-12s | %-20s |",date,pat.patientdata.get(0));
            }
        }
    }
    public void NewBooking(){
        String name = Takestrinp("Enter the name of the patient: ");
        String phoneno = takesphinp("Enter the phone no of the patient: ");
        String addmitno = name.substring(0).trim()+"_"+phoneno.substring(6,9).trim();
        printDocChart();
        String RegisterAseatofDoc = Takestrinp("Enter the doctor id to register the patient: ");
        PrintSchedule(RegisterAseatofDoc);
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
            String GurdianPhoneno = takesphinp("Enter the Gurdian Phone no of the patient: ");
            Patient pat = new Patient(addmitno,enterthedate,name,phoneno,null,adress,Gurdian,GurdianPhoneno,email);
            Patient_Registry.put(addmitno,pat);
            ScheduleAppointment(enterthedate,pat,RegisterAseatofDoc);

        }
    }
 public void printDocChart() {
     System.out.println("\u000c");
     header("Doctor View Menu");
     System.out.println("Doctors");
     System.out.println("--------------------------------------------------------");
     System.out.printf("| %-12s | %-20s | %-20s |\n", "ID", "Doctor name", "Doctor Speciality");
     System.out.println("--------------------------------------------------------");

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

    public void mainMeu(){
        int n = 0;
        do{
            System.out.print("\u000c");
            System.out.flush();
            header("Admin Menu");
            System.out.println("1. Patient Functions");
            System.out.println("2. Management and Infrastructure");
            System.out.println("3. Financials");
            System.out.println("4. Export Data");
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
                     n = Takeintinp("Enter a number in range 1 and 6:");

             }

        }
        while(n!= 6);
    }
    private void FinancialsMenu() {
        System.out.println("\u000c");
        int n= 0;
        do{
            System.out.println("Welcome to the Financial Menu of "+clinic_managementSystem+": " );
            double [] arr = financials.quickshotdata();
            System.out.println("Expenses: "+arr[0]);
            System.out.println("Income: "+arr[1]);
            System.out.println("Available balance"+(arr[1]-arr[0]));
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
            System.out.println("Paying Staff "+staff.staffname+"_"+staff.staffsalary);
            financials.addExpense(String.valueOf("Paying"+staff.staffname),staff.staffsalary);
        }
    }
    private void Management() {
        System.out.print("\u000c");
        int n = 0;
        do{
            System.out.println("Welcome to the Management and Infrastructure menu of "+clinic_managementSystem );
            System.out.println("1. Add Doctor");
            System.out.println("2. Add Staff");
            System.out.println("3. Add Chamber");
            System.out.println("4. Change Doctor things");
            System.out.println("5. Change Staff things");
            System.out.println("6.Remove Doctor");
            System.out.println("7.Remove staff");
            System.out.println("8.Print Floor plan");
            System.out.println("9. Exit");
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
                    doctorMenu();
                    break;
                case 5:
                    staffMenu();
                    break;
                case 7:
                    displayStaffMembers();
                    String staffId = Takestrinp("Enter Staff ID to remove: ");
                    Staff staff = findStaffById(staffId);
                    if (staff != null) {
                        System.out.println("Do you want to pay the staff member Y/N: ");
                        String choice = Takestrinp("-> ");
                        if(choice.toLowerCase().equals("y")){
                            financials.addExpense(String.valueOf("Paying"+staff.staffname),staff.staffsalary);
                        }
                        Staff_Registry.remove(staffId);
                        System.out.println("Staff member removed successfully.");
                    } else {
                        System.out.println("Staff member not found with ID: " + staffId);
                    }
                    break;
                case 6:
                    displayDoctors();
                    String doctorId = Takestrinp("Enter Doctor ID to remove: ");
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
                case 8:
                    printFloorPlan();
                    break;
                case 9:
                    return;
                default:
                    n = Takeintinp("Enter a number between (1-7): ");
            }

        }
        while (n != 9);
    }

    private void RegisterStaff() {
        System.out.println("\u000c");
        String name = Takestrinp("Enter the name of the staff: ");
        String phoneno = takesphinp("Enter the phone no of the staff: ");
        String staffid = name.substring(0).trim()+"_"+phoneno.substring(6,9).trim();
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
                    case 5:
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

    private Staff findStaffById(String staffId) {
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
        displayDoctors();

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
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } while (choice != 0);
        } else {
            System.out.println("Doctor not found with ID: " + doctorId);
        }
    }

    private void displayDoctors() {
        System.out.println("List of Doctors:");
        for (Doctor doctor : Doctor_Registry.values()) {
            System.out.println("Doctor ID: " + doctor.doctorid + ", Doctor Name: " + doctor.name);
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
            System.out.println("1.Add Patient");
            System.out.println("2.Schedule Appointment");
            System.out.println("3.Remove Appointment");
            System.out.println("4.Show schedule for doctor");
            System.out.println("4.Schedule OT(coming soon)");
            System.out.println("5.Remove OT(coming soon)");
            System.out.println("6. Exit");
            int x = Takeintinp("->");
            switch (x){
                case 1:
                    RegisterPatient();
                    break;
                case 2:
                    NewBooking();
                    break;
                case 3:
                    RemovePatientBooking();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Enter a number");
            }

        }
        while (true);


    }

    

}
