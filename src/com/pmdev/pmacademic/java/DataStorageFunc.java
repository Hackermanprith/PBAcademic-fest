package com.pmdev.pmacademic.java;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

class DataExporter {
    static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        String secretKeyString = "9G7sTn4Bp2Y8fR1a";
        byte[] keyBytes = secretKeyString.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    static String encrypt(String data, SecretKey secretKey) throws Exception {
        return data;
    }

    // Helper function to export Rooms data
    public static void exportAllDoctorsData(ArrayList<Doctor>docs) {
        try {
            File directory = new File("Data");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Data directory created.");
                } else {
                    System.out.println("Failed to create Data directory.");
                    return;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter("Data"+File.separator+"all_doctors_data.txt")) {
            SecretKey secretKey = generateSecretKey();
            for (Doctor doctor : docs) {
                // Create a StringBuilder to build the export string for each doctor
                StringBuilder exportData = new StringBuilder();

                // Append doctor details
                exportData.append("Doctor ID: ").append(encrypt(doctor.doctorid, secretKey)).append("\n");
                exportData.append("Name: ").append(encrypt(doctor.name, secretKey)).append("\n");
                exportData.append("Specialty: ").append(encrypt(doctor.speaclity, secretKey)).append("\n");
                exportData.append("Emergency Number: ").append(encrypt(doctor.emphno, secretKey)).append("\n");
                exportData.append("Per Patient Charge: ").append(encrypt(String.valueOf(doctor.perpatientcharge), secretKey)).append("\n");
                exportData.append("Clinic Share: ").append(encrypt(String.valueOf(doctor.clinicshare), secretKey)).append("\n");
                exportData.append("Amount Earned: ").append(encrypt(String.valueOf(doctor.earned), secretKey)).append("\n");
                exportData.append("isEMT: ").append(encrypt(String.valueOf(doctor.isMedicOnStandby),secretKey)).append("\n");

                // Append doctor timings
                try {
                    exportData.append("Timings:\n");
                    Set<Integer> timingKeys = doctor.timings.keySet();
                    for (Integer key : timingKeys) {
                        exportData.append(encrypt(String.valueOf(key), secretKey)).append(": ")
                                .append(encrypt(doctor.timings.get(key), secretKey)).append("\n");
                    }
                }catch (Exception e){
                    exportData.append("Timings:\n");
                    exportData.append("none : none");
                }

                // Append off days
                try {
                    exportData.append("Off Days:\n");
                    for (String offDay : doctor.offdays) {
                        exportData.append(encrypt(offDay, secretKey)).append("\n");
                    }
                }catch (Exception e){
                    exportData.append("Off Days:\n ");
                    exportData.append("null");
                }
                try {
                    exportData.append("Patient Registry:\n");
                    for (Map.Entry<String, ArrayList<Patient>> entry : doctor.Patient_Registry.entrySet()) {
                        exportData.append("Patient ID: ").append(encrypt(entry.getKey(), secretKey)).append("\n");
                        exportData.append("Patient Details:\n");
                        for (Patient patient : entry.getValue()) {
                            exportData.append(encrypt(patient.toString(), secretKey)).append("\n");
                        }
                        exportData.append("===\n");
                    }
                }catch (Exception e){
                    exportData.append("Patient Registry: \n");
                    exportData.append("Null");
                }
                // Write the export string to the file
                writer.write(exportData.toString());

                // Add a separator between doctor entries
                writer.write("\n===\n");
            }

            System.out.println("All doctors data exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting all doctors data.");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void exportEncryptedHashMapToFile(HashMap<String, ArrayList<String>> data, String filename, String delimiter) {
        try {
            File directory = new File("Data");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Data directory created.");
                } else {
                    System.out.println("Failed to create Data directory.");
                    return;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter("Data"+File.separator+filename)) {
            SecretKey secretKey = generateSecretKey();
            for (Map.Entry<String, ArrayList<String>> entry : data.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> values = entry.getValue();

                // Encrypt each value
                ArrayList<String> encryptedValues = new ArrayList<>();
                for (String value : values) {
                    String encryptedValue = new String(encrypt(value, secretKey));
                    encryptedValues.add(encryptedValue);
                }

                // Join encrypted values with the delimiter
                String line = key + delimiter + String.join(delimiter, encryptedValues) + "\n";
                writer.write(line);
            }

            System.out.println("Encrypted HashMap data exported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting encrypted HashMap data.");
        }
    }
    public static void exportAllPatients(ArrayList<Patient> patients,String filename) {
        try {
            File directory = new File("Data");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Data directory created.");
                } else {
                    System.out.println("Failed to create Data directory.");
                    return;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter("Data" +File.separator+filename)) {
            SecretKey secretKey = generateSecretKey();
            for (Patient patient : patients) {
                // Prepare patient data as a string
                StringBuilder patientDataString = new StringBuilder();
                patientDataString.append("Admission No: ").append(patient.addmitno).append("\n");
                patientDataString.append("Admission Date: ").append(patient.admissiondate).append("\n");
                patientDataString.append("Name: ").append(patient.patientdata.get(0)).append("\n");
                patientDataString.append("Phone No: ").append(patient.patientdata.get(1)).append("\n");
                patientDataString.append("Address: ").append(patient.patientdata.get(2)).append("\n");
                patientDataString.append("Email: ").append(patient.patientdata.get(3)).append("\n");
                patientDataString.append("Guardian: ").append(patient.patientdata.get(4)).append("\n");
                patientDataString.append("Guardian Phone No: ").append(patient.patientdata.get(5)).append("\n");
                patientDataString.append("Bed ID: ").append(patient.bedid).append("\n");
                patientDataString.append("Is Emergency: ").append(patient.isEmergency).append("\n");

                // Export Medical History
                try {
                    patientDataString.append("Medical History:\n");
                    if (!patient.MedicalHistory.isEmpty()) {
                      for (String date : patient.MedicalHistory.keySet()) {
                            patientDataString.append("\tDate: ").append(date)
                                    .append(", Diagnosis: ").append(patient.MedicalHistory.get(date).get(1)).append("\n");
                            // Add more details if needed
                        }
                    }
                }catch(Exception e){
                    System.out.println("");

                }
                try {
                    patientDataString.append("Billable Services:\n");
                    for (Map.Entry<String, Integer> entry : patient.billable_services.entrySet()) {
                        patientDataString.append("\tService: ").append(entry.getKey())
                                .append(", Cost: ").append(entry.getValue()).append("\n");
                    }

                }catch (Exception e)
                {
                    System.out.println(e);// Export Upcoming Appointments
                }
                try {
                    patientDataString.append("Upcoming Appointments:\n");
                    for (Map.Entry<String, String> entry : patient.UpcomingAppointments.entrySet()) {
                        patientDataString.append("\tDate: ").append(entry.getKey())
                                .append(", Doctor: ").append(entry.getValue()).append("\n");
                    }
                }catch (Exception e){
                    System.out.println(e);
                }

                try {
                    patientDataString.append("Hospital Expenses:\n");
                    for (Map.Entry<String, Integer> entry : patient.HospitalExpenses.entrySet()) {
                        patientDataString.append("\tExpense: ").append(entry.getKey())
                                .append(", Cost: ").append(entry.getValue()).append("\n");
                    }
                }catch (Exception e){
                    System.out.println(e);
                }
                // Export Hospital Expenses

                // Convert patient data to bytes
                String encryptedData = encrypt(patientDataString.toString(), secretKey);

                // Write the export string to the file
                writer.write(new String(encryptedData));

                // Add a separator between patient entries
                writer.write("\n===\n");
            }

            System.out.println("All patients data exported successfully to file: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting all patients data.");
        }
    }
    public static void exportAllStaff(List<Staff> staffList, SecretKey secretKey) {
        try {
            File directory = new File("Data");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Data directory created.");
                } else {
                    System.out.println("Failed to create Data directory.");
                    return;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            // Create a StringBuilder to build the export string for all staff
            StringBuilder exportData = new StringBuilder();

            // Iterate through each staff and append their details
            for (Staff staff : staffList) {
                exportData.append("ID: ").append(encrypt(staff.staffid, secretKey)).append("\n");
                exportData.append("Name: ").append(encrypt(staff.staffname, secretKey)).append("\n");
                exportData.append("Phone Number: ").append(encrypt(staff.staffphno, secretKey)).append("\n");
                exportData.append("Designation: ").append(encrypt(staff.staffdesignation, secretKey)).append("\n");
                exportData.append("Salary: ").append(encrypt(String.valueOf(staff.staffsalary), secretKey)).append("\n");

                // Append additional staff data
                List<String> staffData = staff.staffdata;
                if (!staffData.isEmpty()) {
                    exportData.append("\nAdditional Data:\n");
                    for (String data : staffData) {
                        exportData.append(encrypt(data, secretKey)).append("\n");
                    }
                }

                // Add a separator between staff entries
                exportData.append("\n===\n");
            }

            // Create a file for all staff using a generic filename
            String filename = "Data" +File.separator+"all_staff_data.txt";
            try (FileWriter writer = new FileWriter(filename)) {
                // Write the export string to the file
                writer.write(exportData.toString());
                System.out.println("All staff data exported successfully to " + filename);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error exporting all staff data to " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting all staff data.");
        }
    }
    public static void exportFloorplanData(HashMap<String, ArrayList<Rooms>> floorplan, String exportDirectory) {
        try {
            // Create the Floorplan directory if it doesn't exist
            File directory = new File(exportDirectory);
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Floorplan directory created.");
                } else {
                    System.out.println("Failed to create Floorplan directory.");
                    return;
                }
            }

            // Export floorplan data to individual files for each floor
            for (String floor : floorplan.keySet()) {
                String filename = exportDirectory + File.separator + floor + "_floor_data.txt";
                exportRoomsData(floorplan.get(floor), filename);
                System.out.println("Floor data exported successfully for " + floor);
            }
            System.out.println("Floorplan data exported successfully.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting floorplan data.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error exporting data.");
        }
    }
    public void exportDoctorSchedules(Map<String, ArrayList<String>> schedules, String outputPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (Map.Entry<String, ArrayList<String>> entry : schedules.entrySet()) {
                String doctorId = entry.getKey();
                ArrayList<String> timingsList = entry.getValue();

                StringBuilder scheduleBuilder = new StringBuilder();

                // Iterate through each day's timings
                for (int i = 0; i < timingsList.size(); i++) {
                    String dayTimings = timingsList.get(i);

                    // Append day and timings
                    scheduleBuilder.append(dayTimings);

                    // Add "|" between days, except for the last day
                    if (i < timingsList.size() - 1) {
                        scheduleBuilder.append(" | ");
                    }
                }

                String line = doctorId + ": " + scheduleBuilder.toString();
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Doctor schedules exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting doctor schedules.");
        }
    }

    private static void exportRoomsData(ArrayList<Rooms> rooms, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Rooms room : rooms) {
                StringBuilder exportData = new StringBuilder();

                // Append room details
                exportData.append("Room ID: ").append(room.roomid).append("\n");
                exportData.append("Room Type: ").append(room.roomtype).append("\n");
                exportData.append("Room Side Capacity: ").append(room.roomsidecapacity).append("\n");

                // Append beds in the room
                exportData.append("Beds in Room:\n");
                for (Beds bed : room.Bedsinroom.values()) {
                    exportData.append("  Bed ID: ").append(bed.bedid).append("\n");
                    exportData.append("  Bed Type: ").append(bed.bedtype).append("\n");
                    exportData.append("  Bed Cost: ").append(bed.bed_cost).append("\n");
                    exportData.append("  Occupant: ").append(bed.occupant).append("\n");
                    exportData.append("-------------------------\n");
                }

                // Write the export string to the file
                writer.write(exportData.toString());

                // Add a separator between room entries
                writer.write("\n===\n");
            }

            System.out.println("Rooms data exported successfully to " + filename);
        }
    }
    public void exportAll(ArrayList<Doctor>Docs,HashMap<String, ArrayList<String>> Expenses,HashMap<String, ArrayList<String>> Ins,ArrayList<Patient>pat,ArrayList<Staff>Staffs,HashMap<String,ArrayList<Rooms>> Floorplan,Map<String,ArrayList<String>>schedules) throws NoSuchAlgorithmException {
        exportAllPatients(pat,"Patients.txt");
        exportEncryptedHashMapToFile(Expenses,"Expenses.txt","|");
        exportEncryptedHashMapToFile(Ins,"Income.txt","|");
        exportAllDoctorsData(Docs);
        exportAllStaff(Staffs,generateSecretKey());
        exportFloorplanData(Floorplan,"Floorplan");
        exportDoctorSchedules(schedules,"Data"+File.separator+"DoctorSchedules.txt");


    }
}

class DataLoader {

    static SecretKey generateSecretKey() {
        String secretKeyString = "9G7sTn4Bp2Y8fR1a";
        byte[] keyBytes = secretKeyString.getBytes();
        return new SecretKeySpec(keyBytes, "AES");
    }

    static String decrypt(String encryptedData, SecretKey secretKey) throws Exception {
        // Convert the decrypted byte array to a string
        return encryptedData;
    }

    public static ArrayList<Doctor> importAllDoctorsData(String filename, SecretKey secretKey) {
        ArrayList<Doctor> importedDoctors = new ArrayList<>();
        Doctor currentDoctor = new Doctor();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            ArrayList<String> list = new ArrayList<String>();

            while ((line = reader.readLine()) != null) {
                if (line.equals("===")) {
                    // Separator between doctor entries
                    if (currentDoctor != null) {
                        importedDoctors.add(currentDoctor);
                    }
                    currentDoctor = new Doctor();
                } else {
                    String[] parts = line.split(": ");
                    if (parts.length == 2) {
                        String attribute = parts[0];
                        String value = parts[1];
                        value = decrypt(value, secretKey);

                        switch (attribute) {
                            case "Doctor ID":
                                currentDoctor.doctorid = value;
                                break;
                            case "Name":
                                currentDoctor.name = value;
                                break;
                            case "Specialty":
                                currentDoctor.speaclity = value;
                                break;
                            case "Emergency Number":
                                currentDoctor.emphno = value;
                                break;
                            case "Per Patient Charge":
                                currentDoctor.perpatientcharge = Double.parseDouble(value);
                                break;
                            case "Clinic Share":
                                currentDoctor.clinicshare = Double.parseDouble(value);
                                break;
                            case "Amount Earned":
                                currentDoctor.earned = Double.parseDouble(value);
                                break;
                            case "Timings":
                                HashMap<Integer, String> timings = new HashMap<>();
                                while (!(line = reader.readLine()).equals("")) {
                                    String[] timingParts = line.split(": ");
                                    int key = Integer.parseInt(decrypt(timingParts[0], secretKey));
                                    String timingValue = decrypt(timingParts[1], secretKey);
                                    timings.put(key, timingValue);
                                }
                                currentDoctor.timings = timings;
                                break;
                            case "Off Days":
                                ArrayList<String> offDays = new ArrayList<>();
                                while (!(line = reader.readLine()).equals("")) {
                                    offDays.add(decrypt(line, secretKey));
                                }
                                currentDoctor.offdays = offDays;
                                break;
                            case "Patient Registry":
                                HashMap<String, ArrayList<Patient>> patientRegistry = new HashMap<>();
                                while (!(line = reader.readLine()).equals("===")) {
                                    String patientId = decrypt(line.split(": ")[1], secretKey);
                                    ArrayList<Patient> patientDetails = new ArrayList<>();
                                    while (!(line = reader.readLine()).equals("===")) {
                                        String decryptedPatientString = decrypt(line, secretKey);
                                        Patient patient = new Patient(); // Create a Patient constructor
                                        // Parse and set patient details based on your actual implementation
                                        // ...
                                        patientDetails.add(patient);
                                    }
                                    patientRegistry.put(patientId, patientDetails);
                                }
                                currentDoctor.Patient_Registry = patientRegistry;
                                break;
                            case "isEMT":
                                currentDoctor.isMedicOnStandby = Boolean.parseBoolean(value);
                        }


                    }
                }
            }

            // Add the last doctor to the list
            if (currentDoctor != null) {
                importedDoctors.add(currentDoctor);
            }

            System.out.println("All doctors data imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error importing all doctors data.");
        }

        return importedDoctors;
    }

    public static ArrayList<Patient> importAllPatients(String filename) {
        ArrayList<Patient> patients = new ArrayList<>();


        try {
            // Read the encrypted data from the file
            byte[] encryptedData = Files.readAllBytes(Paths.get(filename));

            // Generate secret key
            SecretKey secretKey = DataExporter.generateSecretKey();

            // Decrypt the data
            String decryptedData = DataLoader.decrypt(new String(encryptedData), secretKey);

            // Split the decrypted data into patient sections
            String[] patientSections = decryptedData.split("===");

            // Process each patient's data
            for (String patientData : patientSections) {
                // Skip empty sections
                if (patientData.trim().isEmpty()) {
                    continue;
                }

                // Create a new patient object
                Patient patient = new Patient();
                BufferedReader reader = new BufferedReader(new StringReader(patientData));

                // Split the patient data into lines
                String[] lines = patientData.split("\n");

                // Extract data from each line
                for (String line : lines) {
                    String[] parts = line.split(": ");
                    if (parts.length == 2) {
                        String attribute = parts[0].trim();
                        String value = parts[1].trim();

                        // Decrypt the value if needed
                        value = DataLoader.decrypt(value, secretKey);

                        // Set the value in the patient object
                        switch (attribute) {
                            case "Admission No":
                                patient.addmitno = value;
                                break;
                            case "Admission Date":
                                patient.admissiondate = value;
                                break;
                            case "Name":
                                patient.patientdata.add(0, value);
                                break;
                            case "Phone No":
                                patient.patientdata.add(1, value);
                                break;
                            case "Address":
                                patient.patientdata.add(2, value);
                                break;
                            case "Email":
                                patient.patientdata.add(3, value);
                                break;
                            case "Guardian":
                                patient.patientdata.add(4, value);
                                break;
                            case "Guardian Phone No":
                                patient.patientdata.add(5, value);
                                break;
                            case "Bed ID":
                                patient.bedid = value;
                                break;
                            case "Is Emergency":
                                patient.isEmergency = Boolean.parseBoolean(value);
                                break;
                            case "Medical History":
                                HashMap<String, ArrayList<String>> medicalHistory = new HashMap<>();
                                while (!(line = reader.readLine()).equals("")) {
                                    String date = line.split(", Diagnosis: ")[0].substring(7);
                                    String diagnosis = line.split(", Diagnosis: ")[1];
                                    ArrayList<String> entry = new ArrayList<>();
                                    entry.add(date);
                                    entry.add(diagnosis);
                                    medicalHistory.put(date, entry);
                                }
                                patient.MedicalHistory = medicalHistory;
                                break;
                            case "Billable Services":
                                HashMap<String, Integer> billableServices = new HashMap<>();
                                while (!(line = reader.readLine()).equals("")) {
                                    String service = line.split(", Cost: ")[0].substring(8);
                                    int cost = Integer.parseInt(line.split(", Cost: ")[1]);
                                    billableServices.put(service, cost);
                                }
                                patient.billable_services = billableServices;
                                break;
                            case "Upcoming Appointments":

                                HashMap<String, String> upcomingAppointments = new HashMap<>();
                                while (!(line = reader.readLine()).equals("")) {
                                    String date = line.split(", Doctor: ")[0].substring(7);
                                    String doctor = line.split(", Doctor: ")[1];
                                    upcomingAppointments.put(date, doctor);
                                }
                                patient.UpcomingAppointments = upcomingAppointments;
                                break;
                            case "Hospital Expenses":
                                HashMap<String, Integer> hospitalExpenses = new HashMap<>();
                                while (!(line = reader.readLine()).equals("")) {
                                    String expense = line.split(", Cost: ")[0].substring(8);
                                    int cost = Integer.parseInt(line.split(", Cost: ")[1]);
                                    hospitalExpenses.put(expense, cost);
                                }
                                patient.HospitalExpenses = hospitalExpenses;
                                break;
                        }
                    }
                }

                patients.add(patient);
            }

            System.out.println("All patients data imported successfully from file: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error importing all patients data.");
        }

        return patients;
    }

    public static HashMap<String, ArrayList<String>> decryptHmapExpenses() {
        HashMap<String, ArrayList<String>> data = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Data"+File.separator+"Expenses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String key = parts[0];
                    String[] values = parts[1].split("\\|");
                    ArrayList<String> decryptedValues = new ArrayList<>();
                    for (String value : values) {
                        decryptedValues.add(decrypt(value, generateSecretKey()));
                    }
                    data.put(key, decryptedValues);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static HashMap<String, ArrayList<String>> decryptHmapIncome() {
        HashMap<String, ArrayList<String>> data = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("Data"+File.separator+"Income.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String key = parts[0];
                    String[] values = parts[1].split("\\|");
                    ArrayList<String> decryptedValues = new ArrayList<>();
                    for (String value : values) {
                        decryptedValues.add(decrypt(value, generateSecretKey()));
                    }
                    data.put(key, decryptedValues);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public static ArrayList<Staff> importAllStaffs() {
        ArrayList<Staff> staffs = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Data"+File.separator+"all_staff_data.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String key = parts[0];
                    String[] values = parts[1].split("\\|");
                    ArrayList<String> decryptedValues = new ArrayList<>();
                    for (String value : values) {
                        decryptedValues.add(decrypt(value, generateSecretKey()));
                    }
                    Staff staff = new Staff();
                    staff.staffid = decryptedValues.get(0);
                    staff.staffname = decryptedValues.get(1);
                    staff.staffsalary = Double.parseDouble(decryptedValues.get(2));
                    staffs.add(staff);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return staffs;
    }
    public static HashMap<String, ArrayList<Rooms>> importFloorplanData(String importDirectory) {
        HashMap<String, ArrayList<Rooms>> floorplan = new HashMap<>();

        try {
            File directory = new File(importDirectory);
            if (!directory.exists()) {
                System.out.println("Import directory does not exist.");
                return floorplan;
            }

            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith("_floor_data.txt")) {
                    String floorName = file.getName().replace("_floor_data.txt", "");
                    ArrayList<Rooms> rooms = importRoomsData(file.getAbsolutePath());
                    floorplan.put(floorName, rooms);
                    System.out.println("Floor data imported successfully for " + floorName);
                }
            }
            System.out.println("Floorplan data imported successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error importing floorplan data.");
        }

        return floorplan;
    }
    private static ArrayList<Rooms> importRoomsData(String filename) {
        ArrayList<Rooms> rooms = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Rooms room = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Room ID: ")) {
                    if (room != null) {
                        rooms.add(room);
                    }
                    room = new Rooms();
                    room.roomid = line.substring("Room ID: ".length()).trim();
                } else if (line.startsWith("Room Type: ")) {
                    room.roomtype = line.substring("Room Type: ".length()).trim();
                } else if (line.startsWith("Room Side Capacity: ")) {
                    room.roomsidecapacity = Integer.parseInt(line.substring("Room Side Capacity: ".length()).trim());
                } else if (line.equals("Beds in Room:")) {
                    room.Bedsinroom = new HashMap<>();
                } else if (line.startsWith("  Bed ID: ")) {
                    Beds bed = new Beds();
                    bed.bedid = line.substring("  Bed ID: ".length()).trim();
                    bed.bedtype = reader.readLine().substring("  Bed Type: ".length()).trim();
                    bed.bed_cost = Integer.parseInt(reader.readLine().substring("  Bed Cost: ".length()).trim());
                    bed.occupant = reader.readLine().substring("  Occupant: ".length()).trim();
                    room.Bedsinroom.put(bed.bedid, bed);
                    reader.readLine(); // Consume separator line
                }
            }

            if (room != null) {
                rooms.add(room);
            }

            System.out.println("Rooms data imported successfully from " + filename);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error importing rooms data.");
        }

        return rooms;
    }


    public void saveClinicName(String clinicManagementSystem) {
        try {
            File directory = new File("Data");
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("Data directory created.");
                } else {
                    System.out.println("Failed to create Data directory.");
                    return;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Data"+File.separator+"clinic_name.txt"))) {
            writer.write(clinicManagementSystem);
            System.out.println("Clinic name saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving clinic name.");
        }
    }
    public String loadClinicName() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Data"+File.separator+"clinic_name.txt"))) {
            String clinicName = reader.readLine();
            System.out.println("Clinic name loaded successfully.");
            return clinicName;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading clinic name.");
        }
        return null;
    }

    public Map<String, ArrayList<String>> importDoctorSchedules(String doctorSchedules) {
        Map<String, ArrayList<String>> schedules = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Data"+File.separator+doctorSchedules))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String doctorId = parts[0];
                    String scheduleString = parts[1];

                    // Split the scheduleString using "|"
                    String[] dayTimingsArray = scheduleString.split(" \\| ");

                    // Create an ArrayList to store dayTimings
                    ArrayList<String> timingsList = new ArrayList<>(Arrays.asList(dayTimingsArray));

                    // Add the doctorId and timingsList to the schedules map
                    schedules.put(doctorId, timingsList);
                }
            }
            System.out.println("Doctor schedules imported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error importing doctor schedules.");
        }
        return schedules;
    }

}


class Databasedb {
    public static final String DATABASE_FILE = "saltedUserDatabase.txt";
    public static final int SALT_LENGTH = 16; // Length of the salt in bytes
    public static Map<String, String> userDatabase = new HashMap<>();
    public static String Takestrinp(String msg) {
        System.out.print(msg);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();

    }

   public static void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    userDatabase.put(parts[0], parts[1] + ":" + parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUserData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE))) {
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerUser(String username, String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            // Combine the password and salt, then hash
            String saltedPassword = Base64.getEncoder().encodeToString(salt) + ":" + password;
            String hashedPassword = hashPassword(saltedPassword);

            // Store the username, salt, and hashed password
            userDatabase.put(username, Base64.getEncoder().encodeToString(salt) + ":" + hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static boolean loginUser(String username, String password) throws NoSuchAlgorithmException {
        if (userDatabase.containsKey(username)) {
            String storedValue = userDatabase.get(username);
            String[] parts = storedValue.split(":");
            if (parts.length == 2) {
                // Extract salt and hashed password
                byte[] salt = Base64.getDecoder().decode(parts[0]);
                String hashedPassword = parts[1];

                // Combine the entered password with the stored salt and hash
                String saltedPassword = Base64.getEncoder().encodeToString(salt) + ":" + password;
                String hashedInputPassword = hashPassword(saltedPassword);

                // Compare the hashed input password with the stored hashed password
                return hashedPassword.equals(hashedInputPassword);
            }
        }
        return false; // User not found or incorrect password
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
    }
    //change password
    public static boolean changePassword() throws NoSuchAlgorithmException {
       String username = Takestrinp("Enter the username: ");
       String oldPassword = Takestrinp("Enter the old password:");
       String newPassword = Takestrinp("Enter the new password: ");
        if (userDatabase.containsKey(username)) {
            String storedValue = userDatabase.get(username);
            String[] parts = storedValue.split(":");
            if (parts.length == 2) {
                byte[] salt = Base64.getDecoder().decode(parts[0]);
                String hashedPassword = parts[1];
                String saltedOldPassword = Base64.getEncoder().encodeToString(salt) + ":" + oldPassword;
                String hashedOldInputPassword = hashPassword(saltedOldPassword);
                if (hashedPassword.equals(hashedOldInputPassword)) {
                    SecureRandom random = new SecureRandom();
                    byte[] newSalt = new byte[SALT_LENGTH];
                    random.nextBytes(newSalt);
                    String saltedNewPassword = Base64.getEncoder().encodeToString(newSalt) + ":" + newPassword;
                    String hashedNewPassword = hashPassword(saltedNewPassword);
                    userDatabase.put(username, Base64.getEncoder().encodeToString(newSalt) + ":" + hashedNewPassword);

                    return true; // Password changed successfully
                }
            }
        }
        return false; // User not found or incorrect old password
    }
    public static void removeUser() {
        String username = Takestrinp("Enter the username: ");
        if (userDatabase.containsKey(username)) {
            String password = Takestrinp("Enter the password: ");

            try {
                String storedValue = userDatabase.get(username);
                String[] parts = storedValue.split(":");
                if (parts.length == 2) {
                    byte[] salt = Base64.getDecoder().decode(parts[0]);
                    String hashedPassword = parts[1];
                    String saltedPassword = Base64.getEncoder().encodeToString(salt) + ":" + password;
                    String hashedInputPassword = hashPassword(saltedPassword);
                    if (hashedPassword.equals(hashedInputPassword)) {
                        userDatabase.remove(username);
                        System.out.println("User removed successfully.");
                    } else {
                        System.out.println("Incorrect password. User removal failed.");
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User not found. User removal failed.");
        }
    }


}