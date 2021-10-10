package com.sparta.lp.EmployeeCSVProject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameChecker extends checkFactory{
    @Override
    public  boolean check(String name){
        Pattern namePattern=Pattern.compile("[a-zA-Z]+");

        Matcher matcher=namePattern.matcher(name);
        return matcher.matches();
    }
}
