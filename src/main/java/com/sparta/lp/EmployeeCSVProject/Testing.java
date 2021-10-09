package com.sparta.lp.EmployeeCSVProject;
import org.junit.jupiter.api.Test;



import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Testing {
    @Test
    public void emailCheckerTest(){
        List<String> emails= Arrays.asList("","lll.rlgn@","@qepm.wqfn","hrieuh@rakgnj.ljn");
        for (int i=0;i<3;i++) {
            assertFalse(EmailChecker.emailChecker(emails.get(i)));
        }
        assertTrue(EmailChecker.emailChecker(emails.get(3)));
    }
    @Test
    public void nameCheckerTest(){
        List<String> names= Arrays.asList("","29108780","@qepm.wq9n","jbihin");
        for (int i=0;i<3;i++) {
            assertFalse(NameChecker.nameChecker(names.get(i)));
        }
        assertTrue(NameChecker.nameChecker(names.get(3)));
    }
    @Test
    public void initialCheckerTest(){
        List<String> names= Arrays.asList("","29108780","@qepm.wq9n","jbihin","t");
        for (int i=0;i<4;i++) {
            assertFalse(NameChecker.singleLetterElements.singleLetterCheck(names.get(i)));
        }
        assertTrue(NameChecker.singleLetterElements.singleLetterCheck(names.get(4)));
    }
    @Test
    public void numberCheckerTest(){
        List<String> numbers= Arrays.asList("","jbihin","@qepm.wq9n","29108780");
        for (int i=0;i<3;i++) {
            assertFalse(NumberChecker.numberChecker(numbers.get(i)));
        }
        assertTrue(NumberChecker.numberChecker(numbers.get(3)));
    }
    @Test
    public void dateCheckerTest(){
        List<String> invalidDates=Arrays.asList("","2","//","2/2/4","3//5","2/2","/4/2020","3/5/");
        List<String> validDates=Arrays.asList("2/3/2222","22/3/22","2/11/2222","22/11/2222");
        for (String s:invalidDates) {
            assertFalse(DateChecker.dateChecker(s));
        }
        for (String d:validDates) {
            assertFalse(DateChecker.dateChecker(d));
        }
    }
}
