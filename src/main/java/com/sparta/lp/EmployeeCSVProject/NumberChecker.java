package com.sparta.lp.EmployeeCSVProject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class NumberChecker {
    public static  boolean numberChecker(String number){
        Pattern namePattern=Pattern.compile("[0-9]+");

        Matcher matcher=namePattern.matcher(number);
        return matcher.matches();
    }
}
