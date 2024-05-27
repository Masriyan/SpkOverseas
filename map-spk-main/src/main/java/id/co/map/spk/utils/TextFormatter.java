package id.co.map.spk.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFormatter {

    public static final String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public String toRupiahFormat(Double amount){

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

//        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);

        return kursIndonesia.format(amount);
    }

    public String toDateFormat(String sDate){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(sDate);
            sdf = new SimpleDateFormat("dd MMM yyyy");

            return  sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Covert to Bahasa date format
     * @param date should be yyyy-MM-dd
     * @return dd MMMM yyyy
     */
    public String toBahasaDateFormat(String date){
        int month = Integer.valueOf(date.substring(5,7));

        return date.substring(8,10) + " " + bulan[month-1] + " " + date.substring(0,4);
    }

    public static String padZeroRight(int s, int n) {
        return String.format("%-0" + n + "d", s);
    }

    public static String padZeroLeft(int s, int n) {
        return String.format("%0" + n + "d", s);
    }
}
