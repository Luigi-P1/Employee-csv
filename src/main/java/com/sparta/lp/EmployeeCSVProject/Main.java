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
        boolean dataMissing=false;
        boolean dataIsValid=true;
        boolean dataIsDuplicate=false;
        List<String> allID=new ArrayList<String>();
        List<String> validDataSet=new ArrayList<String>();
        List<String> allValidID=new ArrayList<>();
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
            PreparedStatement DuplicateData=conn.prepareStatement("INSERT INTO Employees_Duplicates" +"(Employee_ID, Name_Prefix, First_Name, Middle_Initial, " +
                    "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salery) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
            while (( line=in.readLine())!=null) {
                if (lineNumber==1){
                    headerNames=Arrays.asList(line.split(","));

                }else {
                    dataIsValid=true;
                    newLine = Arrays.asList(line.split(","));
                    for (int i = 0; i < newLine.size(); i++) {
                        newLine.add(i, newLine.get(i).trim());
                    }
                    for (int j=0; j<newLine.size(); j++) {
                        checkFactory validator= checkerFactory.check(j+1);
                        if (!validator.check(newLine.get(j))){
                            dataIsValid=false;
                        }
                    }
                    if (!dataIsValid){
                        logger.info("Invalid or missing data found on line "+lineNumber+" of the csv file, its been added to a separate table.");
                        for (int i = 0; i < newLine.size(); i++) {
                            corruptData.setString(i+1,newLine.get(i));
                            if (newLine.get(i).length()==0){dataMissing=true;}
                        }
                        corruptData.executeUpdate();
                        if (dataMissing){
                            missingDataCount++;
                        }else {
                            invalidDataCount++;
                        }
                    }else {
                        if (allValidID.contains(newLine.get(0))){
                            dataIsDuplicate=true;
                        }
                        if (dataIsDuplicate){
                            int locationOfDuplicate=allValidID.indexOf(newLine.get(0));
                            validDataSet.remove(locationOfDuplicate);
                        }else {
                            allValidID.add(newLine.get(0));
                            validDataSet.add(line);
                        }
                    }

                }
                lineNumber++;
            }
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }
}
