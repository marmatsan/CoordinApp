package com.elcazadordebaterias.coordinapp.utils;

public class EmailValidation {

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    
}
