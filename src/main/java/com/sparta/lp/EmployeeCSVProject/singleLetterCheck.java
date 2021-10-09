package com.sparta.lp.EmployeeCSVProject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class singleLetterCheck extends checkFactory{
    @Override
    public boolean check(String name){
        Pattern namePattern=Pattern.compile("[a-zA-Z]{1}");

        Matcher matcher=namePattern.matcher(name);
        if (matcher.matches() && name.length()==1){return true;}
        return false;
    }
}

