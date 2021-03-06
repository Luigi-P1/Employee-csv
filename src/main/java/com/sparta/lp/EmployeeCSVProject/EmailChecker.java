package com.sparta.lp.EmployeeCSVProject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailChecker extends checkFactory{
    @Override
    public boolean check(String email){
        Pattern emailPattern=Pattern.compile("[a-zA-Z.]+@{1}[a-zA-Z.]+");
        Matcher matcher=emailPattern.matcher(email);

        return matcher.matches();
    }
}
