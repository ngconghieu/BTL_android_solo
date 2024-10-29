package com.example.project.Object;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidate {
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        // it nhat 8 ky tu, hoa thuong hoac so
        String passwordRegex = "^[a-zA-Z0-9]{4,8}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }


}
