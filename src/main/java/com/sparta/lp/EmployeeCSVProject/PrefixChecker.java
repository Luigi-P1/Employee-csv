package com.sparta.lp.EmployeeCSVProject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrefixChecker extends checkFactory{
    @Override
    public boolean check(String name){
        Pattern emailPattern=Pattern.compile("[a-z]+.{1}");
        Matcher matcher=emailPattern.matcher(name);

        return matcher.matches();
    }
}
