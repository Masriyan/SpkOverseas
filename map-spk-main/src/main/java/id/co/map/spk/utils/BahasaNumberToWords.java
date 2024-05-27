package id.co.map.spk.utils;

import java.text.DecimalFormat;

/**
 * @author Awie on 9/11/2019
 */
public class BahasaNumberToWords {

    private static final String[] tensNamesEnglish = {
            "",
            " ten",
            " twenty",
            " thirty",
            " forty",
            " fifty",
            " sixty",
            " seventy",
            " eighty",
            " ninety"
    };

    private static final String[] numNamesEnglish = {
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine",
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen"
    };


    private static final String[] tensNames = {
            "",
            " sepuluh",
            " dua puluh",
            " tiga puluh",
            " empat puluh",
            " lima puluh",
            " enam puluh",
            " tujuh puluh",
            " delapan puluh",
            " sembilan puluh"
    };

    private static final String [] hundredNames = {
            "",
            " seratus",
            " dua ratus",
            " tiga ratus",
            " empat ratus",
            " lima ratus",
            " enam ratus",
            " tujuh ratus",
            " delapan ratus",
            " sembilan ratus"
    };

    private static final String[] numNames = {
            "",
            " satu",
            " dua",
            " tiga",
            " empat",
            " lima",
            " enam",
            " tujuh",
            " delapan",
            " sembilan",
            " sepuluh",
            " sebelas",
            " dua belas",
            " tiga belas",
            " empat belas",
            " lima belas",
            " enam belas",
            " tujuh belas",
            " delapan belas",
            " sembilan belas"
    };

    private static String convertLessThanOneThousandEnglish(int number) {
        String soFar;

        if (number % 100 < 20){
            soFar = numNamesEnglish[number % 100];
            number /= 100;
        }
        else {
            soFar = numNamesEnglish[number % 10];
            number /= 10;

            soFar = tensNamesEnglish[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNamesEnglish[number] + " hundred" + soFar;
    }


    private String convertLessThanOneThousand(int number) {
        StringBuilder soFar = new StringBuilder();
        String lastNum = "";

        if (number % 100 < 20){
            soFar.append(numNames[number % 100]);
            number /= 100;
        }
        else {
            lastNum = numNames[number % 10];
            number /= 10;

            soFar.append(tensNames[number % 10] + lastNum);
            number /= 10;
        }

        if (number == 0) return soFar.toString();
        return hundredNames[number] + soFar;
    }

    public String convert(double number) {
        // 0 to 999 999 999 999
        if (number == 0) { return "nol"; }

        String snumber = Double.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXX.nnn.nnn.nnn
        int billions = Integer.parseInt(snumber.substring(0,3));
        // nnn.XXX.nnn.nnn
        int millions  = Integer.parseInt(snumber.substring(3,6));
        // nnn.nnn.XXX.nnn
        int hundredThousands = Integer.parseInt(snumber.substring(6,9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9,12));

        StringBuilder tradBillions = new StringBuilder();
        switch (billions) {
            case 0:
                tradBillions.append("");
                break;
            default :
                tradBillions.append(convertLessThanOneThousand(billions) + " miliar");
        }
        StringBuilder result =  new StringBuilder(tradBillions);

        StringBuilder tradMillions = new StringBuilder();
        switch (millions) {
            case 0:
                tradMillions.append("");
                break;
            default :
                tradMillions.append(convertLessThanOneThousand(millions)  + " juta ");
        }
        result.append(tradMillions);

        StringBuilder tradHundredThousands = new StringBuilder();
        switch (hundredThousands) {
            case 0:
                tradHundredThousands.append("");
                break;
            case 1 :
                tradHundredThousands.append("seribu ");
                break;
            default :
                tradHundredThousands.append(convertLessThanOneThousand(hundredThousands) + " ribu ");
        }
        result.append(tradHundredThousands);

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result.append(tradThousand);
        result.append(" rupiah");
        // remove extra spaces!
        return result.toString().replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

    public String convertInggris(double number) {
        // 0 to 999 999 999 999
        if (number == 0) { return "zero"; }

        String snumber = Double.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXX.nnn.nnn.nnn
        int billions = Integer.parseInt(snumber.substring(0,3));
        // nnn.XXX.nnn.nnn
        int millions  = Integer.parseInt(snumber.substring(3,6));
        // nnn.nnn.XXX.nnn
        int hundredThousands = Integer.parseInt(snumber.substring(6,9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9,12));

        StringBuilder tradBillions = new StringBuilder();
        switch (billions) {
            case 0:
                tradBillions.append("");
                break;
            default :
                tradBillions.append(convertLessThanOneThousandEnglish(billions) + " billion");
        }
        StringBuilder result =  new StringBuilder(tradBillions);

        StringBuilder tradMillions = new StringBuilder();
        switch (millions) {
            case 0:
                tradMillions.append("");
                break;
            default :
                tradMillions.append(convertLessThanOneThousandEnglish(millions)  + " million ");
        }
        result.append(tradMillions);

        StringBuilder tradHundredThousands = new StringBuilder();
        switch (hundredThousands) {
            case 0:
                tradHundredThousands.append("");
                break;
            case 1 :
                tradHundredThousands.append("one thousand ");
                break;
            default :
                tradHundredThousands.append(convertLessThanOneThousandEnglish(hundredThousands) + " thousand ");
        }
        result.append(tradHundredThousands);

        String tradThousand;
        tradThousand = convertLessThanOneThousandEnglish(thousands);
        result.append(tradThousand);
        // result.append(" rupiah");
        // remove extra spaces!
        return result.toString().replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }
}
