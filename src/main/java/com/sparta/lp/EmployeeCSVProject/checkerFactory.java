package com.sparta.lp.EmployeeCSVProject;

public class checkerFactory {
    static checkFactory check(int order){
        checkFactory theFactory=null;
        if (order==1||order==10){
            theFactory=new NumberChecker();
        }else if (order==2){
            theFactory=new PrefixChecker();
        }else if (order==3 ||order==5){
            theFactory=new NameChecker();
        }else if (order==4 ||order==6){
            theFactory=new singleLetterCheck();
        }else if (order ==7 ) {
            theFactory=new EmailChecker();
        }else if (order == 8 || order == 9) {
            theFactory=new DateChecker();
        }
        return theFactory;
    }
}
