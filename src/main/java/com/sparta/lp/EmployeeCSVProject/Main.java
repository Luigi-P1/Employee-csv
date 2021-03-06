package com.sparta.lp.EmployeeCSVProject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Main {
    private static Logger logger=Logger.getLogger("EmployeeFileLogger");
    public static void Sanatise() {
        PropertyConfigurator.configure("log4j.properties");
        String line=null;
        int totalDataCount=0;
        int validDataCount=0;
        int missingDataCount=0;
        int invalidDataCount=0;
        int lineNumber=1;
        int duplicateDataCount=0;
        long sanatationStart=0;
        long sanatationEnd=0;
        long databaseImportStart=0;
        long databaseImportEnd=0;
        boolean dataMissing=false;
        boolean dataIsValid=true;
        boolean dataIsDuplicate=false;
        List<String> validDataSet=new ArrayList<String>();
        List<String> allValidID=new ArrayList<>();
        List<String> duplicateData=new ArrayList<String>();
        List<String> invalidOrMissingData=new ArrayList<String>();
        List<String> newLine=new ArrayList<String>();
        try (BufferedReader in=new BufferedReader(new FileReader("EmployeeRecords.csv"));
             Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/Employees","root","MySp@rt@Qu3u3L£n")){
            Statement statement=conn.createStatement();
            statement.executeUpdate("CREATE TABLE Employees ( Employee_ID INTEGER,"+
                    "Name_Prefix VARCHAR(255), First_Name VARCHAR(255), Middle_initials VARCHAR(1), "+
                    "Last_Name VARCHAR(255), Gender	varchar(1), Email varchar(255), Date_of_Birth datetime,"+
                    "Date_of_Joining datetime, Salary INTEGER)");
            statement.executeUpdate("CREATE TABLE Employees_Duplicates ( Employee_ID INTEGER,"+
                    "Name_Prefix varchar(255), First_Name varchar(255), Middle_initials varchar(1), "+
                    "Last_Name varchar(255), Gender	varchar(1), Email varchar(255), Date_of_Birth datetime,"+
                    "Date_of_Joining datetime, Salary INTEGER)");
            statement.executeUpdate("CREATE TABLE Employees_with_missing_or_invalid_data ( Employee_ID INTEGER,"+
                    "Name_Prefix varchar(255), First_Name varchar(255), Middle_initials varchar(1), "+
                    "Last_Name varchar(255), Gender	varchar(1), Email varchar(255), Date_of_Birth datetime,"+
                    "Date_of_Joining datetime, Salary INTEGER)");
            PreparedStatement corruptData=conn.prepareStatement("INSERT INTO Employees_with_missing_or_invalid_data" +"(Employee_ID, Name_Prefix, First_Name, Middle_initials, " +
                    "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salary) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement validData=conn.prepareStatement("INSERT INTO Employees" +"(Employee_ID, Name_Prefix, First_Name, Middle_initials, " +
                    "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salary) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement DuplicateData=conn.prepareStatement("INSERT INTO Employees_Duplicates" +"(Employee_ID, Name_Prefix, First_Name, Middle_initials, " +
                    "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salary) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
            sanatationStart=System.nanoTime();
            while (( line=in.readLine())!=null) {
                if (lineNumber!=1){
                    dataIsValid=true;
                    dataMissing =false;
                    dataIsDuplicate=false;
                    newLine = Arrays.asList(line.split(","));
                    for (int i = 0; i < newLine.size(); i++) {
                        newLine.set(i, newLine.get(i).trim());
                    }
                    for (int j=0; j<newLine.size(); j++) {
                        checkFactory validator= checkerFactory.check(j+1);
                        if (!validator.check(newLine.get(j))){
                            dataIsValid=false;
                        }
                    }
                    if (!dataIsValid){
                        logger.info("Invalid or missing data found on line "+lineNumber
                                +" of the csv file, its been added to a separate table.");
                        for (int i = 0; i < newLine.size(); i++) {
                            if (newLine.get(i).length()==0){
                                dataMissing=true;
                            }
                        }
                        invalidOrMissingData.add(line);
                        if (dataMissing){missingDataCount++;
                        }else {invalidDataCount++;
                        }
                    }else {
                        if (allValidID.contains(newLine.get(0))){
                            dataIsDuplicate=true;
                            logger.info("Duplicate data found on line "+lineNumber
                                    +" of the csv file, its been added to a separate table.");
                        }
                        if (dataIsDuplicate){
                            int locationOfDuplicate=allValidID.indexOf(newLine.get(0));
                            String data=validDataSet.remove(locationOfDuplicate);
                            duplicateData.add(data);
                            validDataCount--;
                            duplicateDataCount++;
                        }else {
                            allValidID.add(newLine.get(0));
                            validDataSet.add(line);
                            validDataCount++;
                        }
                    }
                    totalDataCount++;
                }
                lineNumber++;
            }
            sanatationEnd=System.nanoTime();
            databaseImportStart=System.nanoTime();
            TableLineAdder.LineAdder(validDataSet,validData);
            TableLineAdder.LineAdder(duplicateData,DuplicateData);
            TableLineAdder.LineAdder(invalidOrMissingData,corruptData);
            databaseImportEnd=System.nanoTime();
        }catch(IOException | SQLException | ParseException e){
            e.printStackTrace();
        }
        double sanatationDuration =(double) (sanatationEnd-sanatationStart)/1000000000;
        double databaseImportDuration=(double) (databaseImportEnd-databaseImportStart)/1000000000;
        System.out.println("Total amount of data:"+totalDataCount+"\nDuplicate data count:"+duplicateDataCount+
                "\nMissing data count:"+missingDataCount+"\nInvalid data count:"+invalidDataCount+
                "\nValid data count:"+validDataCount);
        System.out.println("Time taken to sanatise the data: "+sanatationDuration+"s \nTime taken to import data to the database: "+databaseImportDuration+"s");
    }

    public static void main(String[] args) throws SQLException {
        System.out.println("Would you like to sanatise the data?");
        Scanner scan = new Scanner(System.in);
        String choice=scan.next();
        try (Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/Employees","root","MySp@rt@Qu3u3L£n")) {
            Statement statement = conn.createStatement();
            try {
                statement.executeUpdate("DROP TABLE Employees");
                statement.executeUpdate("DROP TABLE Employees_Duplicates");
                statement.executeUpdate("DROP TABLE Employees_with_missing_or_invalid_data");
            } catch (SQLSyntaxErrorException p) {
                System.out.println("table(s) didn't exest");
            }catch(SQLException e){
                e.printStackTrace();
            }
        }

        if (choice.equals("y")){
            Sanatise();
        }else {
            String line=null;
            int lineNumber=1;
            long databaseImportStart=0;
            long databaseImportEnd=0;
            List<String> dataSet=new ArrayList<String>();

            try (BufferedReader in=new BufferedReader(new FileReader("EmployeeRecordsLarge.csv"));
                 Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/Employees","root","MySp@rt@Qu3u3L£n")){
                Statement statement=conn.createStatement();
                statement.executeUpdate("CREATE TABLE Employees ( Employee_ID INTEGER,"+
                        "Name_Prefix VARCHAR(255), First_Name VARCHAR(255), Middle_initials VARCHAR(1), "+
                        "Last_Name VARCHAR(255), Gender	varchar(1), Email varchar(255), Date_of_Birth datetime,"+
                        "Date_of_Joining datetime, Salary INTEGER)");
                PreparedStatement dataToDatabase=conn.prepareStatement("INSERT INTO Employees" +"(Employee_ID, Name_Prefix, First_Name, Middle_initials, " +
                        "Last_Name, Gender, Email, Date_of_Birth, Date_of_Joining, Salary) "+"VALUES(?,?,?,?,?,?,?,?,?,?)");
                while (( line=in.readLine())!=null) {
                    if (lineNumber != 1) {
                        dataSet.add(line);
                    }
                    lineNumber++;
                }
                databaseImportStart=System.nanoTime();
                TableLineAdder.LineAdder(dataSet,dataToDatabase);
                databaseImportEnd=System.nanoTime();
            }catch(IOException | SQLException | ParseException e){
                e.printStackTrace();
            }
            double databaseImportDuration=(double) (databaseImportEnd-databaseImportStart)/1000000000;
            System.out.println("Time taken sor import data to the database: "+databaseImportDuration+"s");
        }
    }
}
