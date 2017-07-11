package com.movil.tesis.yanbal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isDNIValid(String dni) {
        boolean validDNI;

        try {
            if (dni.length() == 10) {
                int thirdDigit = Integer.parseInt(dni.substring(2, 3));
                if (thirdDigit < 6) {
                    int[] validationCoeff = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int checkDigit = Integer.parseInt(dni.substring(9, 10));
                    int sum = 0;
                    int digit;
                    for (int i = 0; i < (dni.length() - 1); i++) {
                        digit = Integer.parseInt(dni.substring(i, i + 1)) * validationCoeff[i];
                        sum += ((digit % 10) + (digit / 10));
                    }
                    if ((sum % 10 == 0) && (sum % 10 == checkDigit)) {
                        validDNI = true;
                    } else if ((10 - (sum % 10)) == checkDigit) {
                        validDNI = true;
                    } else {
                        validDNI = false;
                    }
                } else {
                    validDNI = false;
                }
            } else {
                validDNI = false;
            }
        } catch (Exception exc) {
            validDNI = false;
        }
        return validDNI;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
