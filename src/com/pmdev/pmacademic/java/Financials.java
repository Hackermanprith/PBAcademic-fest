package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

public class Financials {
    HashMap<String, ArrayList<String>> Expenses;
    HashMap<String, ArrayList<String>> Ins;

    //add expense method
    public double[] quickshotdata() {
        double expenses = 0.0;
        double income = 0.0;
        for (String x : Expenses.keySet()) {
            ArrayList<String> ex = Expenses.get(x);
            expenses += Double.valueOf(ex.get(0));
        }
        for (String k : Ins.keySet()) {
            ArrayList<String> ex = Expenses.get(k);
            income += Double.valueOf(ex.get(0));
        }
        return new double[]{expenses, income};
    }

    public void addIncome(String serviceName, double cost, double hospcost, boolean hosexp) {
        ArrayList<String> data = new ArrayList<>();
        if (hosexp) {
            data.add(String.valueOf(hospcost));
            Expenses.put(serviceName, data);
        }
        data.add(0, String.valueOf(cost));
        data.add(1, String.valueOf(hospcost));
        Ins.put(serviceName, data);
    }

    public void addExpense(String serviceName, double cost) {
        ArrayList<String> data = new ArrayList<>();
        data.add(0, String.valueOf(cost));
        Expenses.put(serviceName, data);
    }

    public void showfinancials() {
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-12s | %-12s | %-12s |\n", "Income name", "Cost Incurred ", "Amount Gained");
        System.out.println("--------------------------------------------------");
        for (String incomename : Ins.keySet()) {
            ArrayList<String> exp = Ins.get(incomename);
            for (String i : exp) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-12s | %-12s | %-12s |\n", "Expense name", "Cost Incurred ");
        System.out.println("--------------------------------------------------");
        for (String expensename : Ins.keySet()) {
            ArrayList<String> exp = Expenses.get(expensename);
            for (String i : exp) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}
