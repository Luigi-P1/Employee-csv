package com.sparta.lp.EmployeeCSVProject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Main {
    private static Logger logger=Logger.getLogger("Employee File Logger");
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        String line=null;
        int validDataCount=0;
        int missingDataCount=0;
        int invalidDataCount=0;
        int lineNumber=0;
        boolean allDataPresent=true;
        boolean dataIsValid=true;
        List<String> allID=new ArrayList<String>();
        List<String> allData=new ArrayList<String>();
        LinkedHashSet<String> validID=new LinkedHashSet<>();
        List<String> duplicateID=new ArrayList<String>();
        List<String> duplicateData=new ArrayList<String>();
        List<String> headerNames=new ArrayList<String>();
        List<String> newLine=new ArrayList<String>();
        List<String> lineInList=new ArrayList<String>();
        try (BufferedReader in=new BufferedReader(new FileReader("EmployeeRecords.csv"));
             Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/Employees","root","MySp@rt@Qu3u3L£n")){
            Statement statement=conn.createStatement();
            statement.executeUpdate("DROP TABLE Employees");
            statement.executeUpdate("DROP TABLE Employees_Duplicates");
            statement.executeUpdate("DROP TABLE Employees_with_missing_or_invalid_data");
            statement.executeUpdate("CREATE TABLE \"Employees\" ( \"Employee ID\" INTEGER,"+
                    "\"Name Prefix\" varchar(255), \"First_Name\" varchar(255), \"Middle_initials\" varchar(1), "+
                    "\"Last_Name\" varchar(255), \"Gender\"	varchar(1), \"Email\" varchar(255), \"Date_of_Birth\" datetime"+
                    "\"Date_of_Joining\" datetime, \"Salary\" INTEGER");
            statement.executeUpdate("CREATE TABLE \"Employees_Duplicates\" ( \"Employee ID\" INTEGER,"+
                    "\"Name Prefix\" varchar(255), \"First_Name\" varchar(255), \"Middle_initials\" varchar(1), "+
                    "\"Last_Name\" varchar(255), \"Gender\"	varchar(1), \"Email\" varchar(255), \"Date_of_Birth\" datetime"+
                    "\"Date_of_Joining\" datetime, \"Salary\" INTEGER");
            statement.executeUpdate("CREATE TABLE \"Employees_with_missing_or_invalid_data\" ( \"Employee ID\" INTEGER,"+
                    "\"Name Prefix\" varchar(255), \"First_Name\" varchar(255), \"Middle_initials\" varchar(1), "+
                    "\"Last_Name\" varchar(255), \"Gender\"	varchar(1), \"Email\" varchar(255), \"Date_of_Birth\" datetime"+
                    "\"Date_of_Joining\" datetime, \"Salary\" INTEGER");
            PreparedStatement corruptData=conn.prepareStatement("INSERT INTO Employees_with_missing_or_invalid_data" +"(Employee_ID, Name_Prefix, First_Name, Middle_Initial, " +
                    "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salery) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement validData=conn.prepareStatement("INSERT INTO Employees" +"(Employee_ID, Name_Prefix, First_Name, Middle_Initial, " +
                    "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salery) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
            while (( line=in.readLine())!=null) {
                if (lineNumber==1){
                    headerNames=Arrays.asList(line.split(","));

                }else {
                    allDataPresent=true;
                    dataIsValid=true;
                    newLine = Arrays.asList(line.split(","));
                    for (int i = 0; i < newLine.size(); i++) {
                        newLine.add(i, newLine.get(i).trim());
                    }
                    for (int j=0; j<newLine.size(); j++){
                        if (newLine.get(j).length()==0){
                            allDataPresent=false;
                            missingDataCount++;
                            logger.info("missing "+headerNames.get(j)+" for "+newLine.get(2)+" "+newLine.get(4)+".  See line "+(missingDataCount+invalidDataCount)+" of the table ");
                        }
                    }
                    if (!allDataPresent){
                        for (int k = 1; k < 11; k++) {
                            corruptData.setString(k,newLine.get(k));
                        }
                        corruptData.executeUpdate();
                    }
                    else {};


                }
                lineNumber++;
            }
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }
}
