package com.sparta.lp.EmployeeCSVProject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateChecker extends checkFactory{
    @Override
    public   boolean check(String date){
        Pattern datePattern=Pattern.compile("[0-9]{1,2}/[0-9]{1,2}/[0,9]{4}");

        Matcher matcher=datePattern.matcher(date);
        return matcher.matches();
    }
}
