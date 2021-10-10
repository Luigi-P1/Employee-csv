package com.sparta.lp.EmployeeCSVProject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TableLineAdder {
    public static void LineAdder(List<String> dataSet, PreparedStatement writer) throws SQLException, ParseException {
        List <String> newLine= new ArrayList<String>();
        for (String s: dataSet ) {
            newLine=Arrays.asList(s.split(","));
            for (int i = 0; i < newLine.size(); i++) {
                if (i !=7 &&i!=8) {
                    writer.setString(i + 1, newLine.get(i));
                }else {
                    Date d= new SimpleDateFormat("MM/dd/yyyy").parse(newLine.get(i));
                    java.sql.Date d1 = new java.sql.Date(d.getTime());
                    writer.setDate(i+1,d1);
                }
        }
        writer.executeUpdate();

    }}
}
