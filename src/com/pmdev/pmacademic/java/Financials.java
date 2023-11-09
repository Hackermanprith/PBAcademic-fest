package com.pmdev.pmacademic.java;

import java.util.ArrayList;
import java.util.HashMap;

public class Financials {
    HashMap<String, ArrayList<String>> Expenses;
    HashMap<String, ArrayList<String>> Ins;
    //add expense method
    public void quickshotdata(){
        double expenses=0.0;
        double income = 0.0;
        for(String x : Expenses.keySet()){
           ArrayList<String> ex = Expenses.get(x);
           expenses += Double.valueOf(ex.get(0));
        }
        for(String k : Ins.keySet()){
            ArrayList<String> ex = Expenses.get(k);
            income += Double.valueOf(ex.get(0));
        }
    }

}
