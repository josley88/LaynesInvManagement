import java.sql.*;
import java.io.*;  
import java.util.Scanner;  

public class jdbcpostgreSQL {

  //Commands to run this script
  //This will compile all java files in this directory
  //javac *.java
  //This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
  //Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL.java
  //Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL.java

  //MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE

  // _________________Global Variables_________________
  public static Connection conn = null;
  public static String teamNumber = "21";
  public static String sectionNumber = "904";
  public static String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
  public static String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
  public static String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
  public static String userPassword = "315gang";
  //___________________________________________________


  public static void dropTables(){
    try{
      String sqlStatement = "DROP TABLE monday;";
      Statement stmt = conn.createStatement();
      int result = stmt.executeUpdate(sqlStatement); 
      sqlStatement = "DROP TABLE tuesday;";
      result = stmt.executeUpdate(sqlStatement); 
      sqlStatement = "DROP TABLE wednesday;";
      result = stmt.executeUpdate(sqlStatement); 
      sqlStatement = "DROP TABLE thursday;";
      result = stmt.executeUpdate(sqlStatement); 
      sqlStatement = "DROP TABLE friday;";
      result = stmt.executeUpdate(sqlStatement); 
      sqlStatement = "DROP TABLE saturday;";
      result = stmt.executeUpdate(sqlStatement); 
      sqlStatement = "DROP TABLE sunday;";
      result = stmt.executeUpdate(sqlStatement); 
      System.out.println(result);
    }catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }      
  }
  // function to input weekly purchase into daily order tables it will take in the file name and it will update the database directly
  public static void inputElementsIntoDaysofTheWeekOrders(String fileName){
    Scanner sc;
    
    try{
      //drops sun - sat
      dropTables(); 
      //TODO conditional droptable
      Statement stmt = conn.createStatement();


      sc = new Scanner(new File(fileName));
      String filler = sc.nextLine().replace("\'", "\'\'");
      String[] parseArr = filler.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
      String tableName = sc.nextLine().replace("\'", "\'\'");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      
      tableName = parseArr[0].strip() + fileName.substring(0, (fileName.length()-4));
      String sqlStatement = "CREATE TABLE " + parseArr[0].strip() + " ( ";
      // populates in the following order Item, Quantity, Total
      sqlStatement += parseArr[1].strip() + " INT PRIMARY KEY, " + parseArr[2].strip() + " INT, "+ parseArr[3].strip() + " TEXT"+" );";
      
      // SQL side;
      //Statement stmt = conn.createStatement();
      int result = stmt.executeUpdate(sqlStatement);
      System.out.println(result);
      //conn.close();
      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0){
          if(parseArr[1].equals("")!=true) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + "," + parseArr[2].strip() + "," + "\'" + parseArr[3].strip() + "\'" + ");";
            //stmt = conn.createStatement();
            System.out.println(sqlStatement);
            result = stmt.executeUpdate(sqlStatement);
            System.out.println(result);
            //conn.close();
            for (String string : parseArr) {
              if (string.length() != 0) {
                System.out.println(string);
              }
            }
          }
          else{ // handles when we enter into a new day 
            if(sc.hasNextLine()){ // checks if we are at end of file 
              tableName = sc.nextLine().replace("\'", "\'\'");
              parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

              tableName = parseArr[0].strip() + fileName.substring(0, (fileName.length()-4));
              sqlStatement = "CREATE TABLE " + parseArr[0].strip() + " ( ";
              // populates in the following order Item, Quantity, Total
              sqlStatement += parseArr[1].strip() + " INT PRIMARY KEY, " + parseArr[2].strip() + " INT, "+ parseArr[3].strip() + " TEXT"+" );";

              // SQL side;
              stmt = conn.createStatement();
              result = stmt.executeUpdate(sqlStatement);
              System.out.println(result);
              //conn.close();
            }
          }
        }
      }




    } 
    catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
  }
  }



  // function to input the elements into the inventory
  public static void inputElementsIntoInventory(String fileName){
    Scanner sc;
    
    try{
      //drops sun - sat
      dropTables(); 
      //TODO conditional droptable
      Statement stmt = conn.createStatement();


      sc = new Scanner(new File(fileName));
      String filler = sc.nextLine().replace("\'", "\'\'");
      String[] parseArr = filler.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
      String tableName = sc.nextLine().replace("\'", "\'\'");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      tableName = "INVENTORY";
      //tableName = parseArr[0].strip() + fileName.substring(0, (fileName.length()-4));
      String sqlStatement = "CREATE TABLE " + "INVENTORY" + " ( ";
      // populates in the following order Item, Quantity, Total
      sqlStatement += parseArr[1].strip() + " TEXT, " + parseArr[2].strip() + " TEXT PRIMARY KEY, "+ parseArr[3].strip() + " INT, "+ parseArr[4].strip() + " INT, "+ parseArr[5].strip() + " TEXT, "+ parseArr[6].strip() + " TEXT, "+ parseArr[7].strip() + " INT, "+ parseArr[8].strip() + " TEXT, "+ parseArr[9].strip() + " TEXT, "+ parseArr[10].strip() + " TEXT, "+ parseArr[11].strip() + " INT, "+ parseArr[12].strip() + " TEXT, "+" );";
      
      // SQL side;
      //Statement stmt = conn.createStatement();
      int result = stmt.executeUpdate(sqlStatement);
      System.out.println(result);
      //conn.close();
      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0 && sc.hasNextLine()){
          if(parseArr[1].equals("")!=true) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + "," + parseArr[2].strip() + "," + "\'" + parseArr[3].strip() + "\'" + "," + "\'" + parseArr[4].strip() + "\'" + "," + "\'" + parseArr[5].strip() + "\'"+ "," + "\'" + parseArr[6].strip() + "\'" + "," + "\'" + parseArr[7].strip() + "\'" + "," + "\'" + parseArr[8].strip() + "\'" + "," + "\'" + parseArr[9].strip() + "\'" + "," + "\'" + parseArr[10].strip() + "\'" + "," + "\'" + parseArr[11].strip() + "\'" + "," + "\'" + parseArr[12].strip() + "\'" + ");";
            //stmt = conn.createStatement();
            System.out.println(sqlStatement);
            result = stmt.executeUpdate(sqlStatement);
            System.out.println(result);
            //conn.close();
            for (String string : parseArr) {
              if (string.length() != 0) {
                System.out.println(string);
              }
            }
          }
          else{ // handles when we enter into a new day 
            if(sc.hasNextLine()){ // checks if we are at end of file 
              tableName = sc.nextLine().replace("\'", "\'\'");
              parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            }
          }
        }
      }




    } 
    catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
  }
  }





  
  //function to input elements into the Menu Table.

  public static void inputElementsIntoMenuTable(String fileName){
    Scanner sc;
    
    try{
      sc = new Scanner(new File(fileName));
      String tableName = sc.nextLine().replace("\'", "\'\'");
      String[] parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      
      tableName = "Menu_Key";
      String sqlStatement = "CREATE TABLE " + "Menu_Key" + " ( ";
      // populates in the following order Item, name, Description, price 
      String tableFormatting = sc.nextLine().replace("\'", "\'\'");;
      parseArr = tableFormatting.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      //sanitizes the parse Arr values for (" ")
      for (String string : parseArr) {
        if (string.length() != 0) {
          System.out.println(string);
        }
      }
      sqlStatement += parseArr[1].strip() + " INT PRIMARY KEY, " + parseArr[2].strip() + " TEXT, "+ parseArr[3].strip() + " TEXT, " + parseArr[4].strip() + " TEXT"+" );";
      System.out.println(sqlStatement);
      // SQL side;
      Statement stmt = conn.createStatement();
      int result = stmt.executeUpdate(sqlStatement);
      System.out.println(result);
      //conn.close();
      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + ",\'" + parseArr[2].strip() + "\'," + "\'" + parseArr[3].strip() + "\'" + ",\'" + parseArr[4].strip() + "\');";
        //stmt = conn.createStatement();
        System.out.println(sqlStatement); 
        result = stmt.executeUpdate(sqlStatement);
        System.out.println(result);
        //conn.close();
        for (String string : parseArr) {
          if (string.length() != 0) {
            System.out.println(string);
          }
        }
      }




    } 
    catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
  }
  }





  public static void setupDatabase() {

   try {
      conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    System.out.println("Opened database successfully");
  }





  public static void runSQLCommands() {
    try{
      // create a statement object
      Statement stmt = conn.createStatement();

      // Running a query
      // String sqlStatement = "INSERT INTO teammembers VALUES ('John Smith', 904, 'Inception', '04/01/2022');";

      String sqlStatement = "SELECT * FROM teammembers LIMIT 10;";

      // send statement to DBMS
      // This executeQuery command is useful for data retrieval
      ResultSet result = stmt.executeQuery(sqlStatement);
      // OR
      // This executeUpdate command is useful for updating data
      // int result = stmt.executeUpdate(sqlStatement);

      // OUTPUT
      // You will need to output the results differently depeninding on which function you use
      System.out.println("--------------------Query Results--------------------");
      while (result.next()) {
        System.out.println(result.getString("student_name"));
      }
      // OR
      // System.out.println(result);
    } catch (Exception e){
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
    }
  }


  public static void closeConnection() {
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }
  }


public static void main(String args[]) {

  

  // _________setup the database_______
  setupDatabase();

  System.out.println("I got here 1 \n");
  // _________running commands_________
  //runSQLCommands();
  inputElementsIntoDaysofTheWeekOrders("./CSCE315-1/ThirdWeekSales.csv");
  System.out.println("I got here 2 \n");

  // __________Close Connection________
  closeConnection();

  }
}