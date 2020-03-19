package com.example.alkautsarefendi.dashboardtkr.format;

import java.text.NumberFormat;

/**
 * Created by Akautsar Efendi on 11/7/2017.
 */

public class FormatAngka {

    public static String Angko(double d) {

        NumberFormat nf = NumberFormat.getInstance();

        return nf.format(d);
    }
}