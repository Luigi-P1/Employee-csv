package com.sparta.lp.EmployeeCSVProject;

import org.sqlite.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameChecker {
    public class singleLetterElements{
        static boolean singleLetterCheck(String name){
            if (nameChecker(name) && name.length()==1){return true;}
            return false;
        }
    }
    public static  boolean nameChecker(String name){
        Pattern namePattern=Pattern.compile("[a-zA-Z]+");

        Matcher matcher=namePattern.matcher(name);
        return matcher.matches();
    }
    //public static boolean isAlphabetical(String str)
}
