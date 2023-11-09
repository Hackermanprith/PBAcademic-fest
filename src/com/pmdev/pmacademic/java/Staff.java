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
    public void printStaffDetails(){
        System.out.println("Staff Details");
        System.out.println("Name: "+staffdata.get(0));
        System.out.println("Phone No: "+staffdata.get(1));
        System.out.println("Staff ID: "+staffdata.get(2));
        System.out.println("Address: "+staffdata.get(3));
        System.out.println("Email: "+staffdata.get(4));
        System.out.println("Salary: "+staffsalary);
    }
    //increase salary
    public void increaseSalary(double amount){
        staffsalary += amount;
    }
    //decrease salary
    public void decreaseSalary(double amount){
        staffsalary -= amount;
    }
    //change designation
    public void changeDesignation(String designation){
        staffdesignation = designation;
    }
}
