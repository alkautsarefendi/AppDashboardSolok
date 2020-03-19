package com.example.alkautsarefendi.dashboardtkr.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Akautsar Efendi on 11/7/2017.
 */

public class FormatRupiah {

    public static String Rupiah(double d) {

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setGroupingSeparator(',');
        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(d);
    }
}
