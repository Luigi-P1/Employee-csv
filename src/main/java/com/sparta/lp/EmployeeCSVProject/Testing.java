package com.sparta.lp.EmployeeCSVProject;
import org.junit.jupiter.api.Test;



import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Testing {
    @Test
    public void numberCheckerTest() {
        List<String> numbers = Arrays.asList("", "jbihin", "@qepm.wq9n", "29108780");
        for (int i = 0; i < 3; i++) {
            assertFalse(checkerFactory.check(1).check(numbers.get(i)));
        }
        assertTrue(checkerFactory.check(1).check(numbers.get(3)));
    }

    @Test
    public void namePrefixCheckerTest() {
        List<String> names = Arrays.asList("", "29108780", "@qepm.wq9n", "44jbihin", "t.", "Tdf.");
        for (int i = 0; i < 4; i++) {
            assertFalse(checkerFactory.check(2).check(names.get(i)));
        }
        assertTrue(checkerFactory.check(2).check(names.get(4)));
        assertTrue(checkerFactory.check(2).check(names.get(5)));
    }

    @Test
    public void nameCheckerTest() {
        List<String> names = Arrays.asList("", "29108780", "@qepm.wq9n", "jbihin", "Tsljkbb");
        checkFactory check = checkerFactory.check(3);
        for (int i = 0; i < 3; i++) {
            assertFalse(check.check(names.get(i)));
        }
        assertTrue(check.check(names.get(3)));
        assertTrue(check.check(names.get(4)));
    }

    @Test
    public void initialCheckerTest() {
        List<String> names = Arrays.asList("", "29108780", "@qepm.wq9n", "jbihin", "t", "T");
        for (int i = 0; i < 4; i++) {
            assertFalse(checkerFactory.check(4).check(names.get(i)));
        }
        assertTrue(checkerFactory.check(4).check(names.get(4)));
        assertTrue(checkerFactory.check(4).check(names.get(5)));
    }

    @Test
    public void emailCheckerTest() {
        List<String> emails = Arrays.asList("", "lll.rlgn@", "@qepm.wqfn", "della.new@hotmail.com");
        for (int i = 0; i < 3; i++) {
            assertFalse(checkerFactory.check(7).check(emails.get(i)));
        }
        assertTrue(checkerFactory.check(7).check(emails.get(3)));
    }

    @Test
    public void dateCheckerTest() {
        List<String> invalidDates = Arrays.asList("", "2", "//", "2/2/4", "3//5", "2/2", "/4/2020", "3/5/");
        List<String> validDates = Arrays.asList("2/3/2022", "22/3/1998", "2/11/2000", "22/11/1976");
        for (String s : invalidDates) {
            assertFalse(checkerFactory.check(8).check(s));
        }
        for (String d : validDates) {
            assertFalse(checkerFactory.check(8).check(d));
        }
    }
}
