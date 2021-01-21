package com.elcazadordebaterias.coordinapp.utils;

public class EmailValidation {

    public static boolean isInvalidEmail(CharSequence target) {
        if (target == null) {
            return true;
        } else {
            return !android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
