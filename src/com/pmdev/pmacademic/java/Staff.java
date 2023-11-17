package com.pmdev.pmacademic.java;

import java.util.ArrayList;

public class Staff {
    String staffname;
    String staffid;
    String staffphno;
    String staffdesignation;
    ArrayList<String> staffdata;
    double staffsalary;
    Staff(String staffname,String staffphno,String staffdesignation,ArrayList<String> staffdata,double staffsalary){
        this.staffname = staffname;
        this.staffphno = staffphno;
        this.staffid = staffname.trim().substring(0,3)+staffphno.trim().substring(0,3);
        this.staffdesignation = staffdesignation;
        this.staffdata = staffdata;
        this.staffsalary = staffsalary;
    }

    public void printData() {
        System.out.println("Staff Details:");
        System.out.printf("%-15s | %-25s |%-15s | %-20s |%-15s  |\n", "ID", "Name", "Phone Number", "Designation", "Salary");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-15s%-25s%-15s%-20s%-15.2f%n", staffid, staffname, staffphno, staffdesignation, staffsalary);

        // Print additional staff data from ArrayList in a table
        if (!staffdata.isEmpty()) {
            System.out.println("\nAdditional Data:");
            System.out.printf("%-30s%n", "Additional Information");
            for (String data : staffdata) {
                System.out.printf("%-30s%n", data);
            }
        }
        //increase salary
    }
}
