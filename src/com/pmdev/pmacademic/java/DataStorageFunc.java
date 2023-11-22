package com.pmdev.pmacademic.java;
import java.io.*;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;


class DataExporter {
    static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        String secretKeyString = "a412e3ed51818720c5ab521d4f6789cc";
        byte[] keyBytes = secretKeyString.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    static byte[] encrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data.getBytes());
    }

    static void saveToFile(byte[] data, String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }
    public static void exportAllRoomsData(Rooms[] allRooms) {
        try (FileWriter writer = new FileWriter("all_rooms_data.txt")) {
            for (Rooms room : allRooms) {
                // Create a StringBuilder to build the export string for each room
                StringBuilder exportData = new StringBuilder();

                // Append room details
                exportData.append("Room ID: ").append(room.roomid).append("\n");
                exportData.append("Room Type: ").append(room.roomtype).append("\n");
                exportData.append("Room Side Capacity: ").append(room.roomsidecapacity).append("\n");

                // Append occupant details
                exportData.append("Occupants:\n");
                for (Map.Entry<String, Beds> entry : room.Bedsinroom.entrySet()) {
                    exportData.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                // Write the export string to the file
                writer.write(exportData.toString());

                // Add a separator between room entries
                writer.write("\n===\n");
            }

            System.out.println("All rooms data exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting all rooms data.");
        }
    }
    public static void exportAllBedsData(Beds[] allBeds) {
        try (FileWriter writer = new FileWriter("all_beds_data.txt")) {
            for (Beds bed : allBeds) {
                // Create a StringBuilder to build the export string for each bed
                StringBuilder exportData = new StringBuilder();

                // Append bed details
                exportData.append("Bed ID: ").append(bed.bedid).append("\n");
                exportData.append("Occupant: ").append(bed.occupant).append("\n");
                exportData.append("Bed Cost: ").append(bed.bed_cost).append("\n");
                writer.write(exportData.toString());

                // Add a separator between bed entries
                writer.write("\n===\n");
            }

            System.out.println("All beds data exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting all beds data.");
        }
    }
    public static void ExportAllChambersData(Chamber[] allChambers) {
        try (FileWriter writer = new FileWriter("all_chambers_data.txt")) {
            for (Chamber chamber : allChambers) {
                // Create a StringBuilder to build the export string for each chamber
                StringBuilder exportData = new StringBuilder();

                // Append chamber details
                exportData.append("Chamber ID: ").append(chamber.getChamberID()).append("\n");
                exportData.append("Chamber Status: ").append(chamber.chamberstatus).append("\n");

                // Append off times
                exportData.append("Off Times:\n");
                for (String offTime : chamber.offtimes) {
                    exportData.append(offTime).append("\n");
                }

                // Append chamber schedule
                exportData.append("Chamber Schedule:\n");
                for (Map.Entry<String, String> entry : chamber.ChamberSchedule.entrySet()) {
                    exportData.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                // Append booked slots
                exportData.append("Booked Slots:\n");
                for (Map.Entry<String, String> entry : chamber.bookings.entrySet()) {
                    exportData.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                // Write the export string to the file
                writer.write(exportData.toString());

                // Add a separator between chamber entries
                writer.write("\n===\n");
            }

            System.out.println("All chambers data exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error exporting all chambers data.");
        }
    }

}
class DataLoader{

    private static byte[] loadFromFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read;
            while ((read = fis.read(buf)) != -1) {
                bos.write(buf, 0, read);
            }
            return bos.toByteArray();
        }
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        String secretKeyString = "a412e3ed51818720c5ab521d4f6789cc";
        byte[] keyBytes = secretKeyString.getBytes();
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static String decrypt(byte[] encryptedData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }

    static HashMap<String, String> reconstructHashMap(String data) {
        HashMap<String, String> reconstructedMap = new HashMap<>();
        String[] keyValuePairs = data.substring(1, data.length() - 1).split(", ");
        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            reconstructedMap.put(entry[0], entry[1]);
        }
        return reconstructedMap;
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