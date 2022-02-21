import java.sql.*;
import java.io.*;  
import java.util.Scanner;  
/*
CSCE 315
9-27-2021 Lab
 */
public class jdbcpostgreSQL {

  //Commands to run this script
  //This will compile all java files in this directory
  //javac *.java
  //This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
  //Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
  //Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

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

  //function to input elements into the Menu Table.  
  public static void inputElementsIntoMenuTable(String fileName){
    Scanner sc;
    
    try{
      sc = new Scanner(new File(fileName));

      // populates in the following order Item, name, Description, price 
      while(sc.hasNext()){
        
        String[] parseArr = sc.nextLine().split(",");
        System.out.println(parseArr.toString());
      }




    } 
    catch (FileNotFoundException e) {e.printStackTrace();}
  }

  // leetCode to set up the main menu items

  public static void setupDatabase() {
    //Building the connection with your credentials
    //TODO: update teamNumber, sectionNumber, and userPassword here

   //Connecting to the database
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
      //create a statement object
      Statement stmt = conn.createStatement(); 

      //Running a query
      //TODO: update the sql command here
      //String sqlStatement = "INSERT INTO teammembers VALUES ('John Smith', 904, 'Inception', '04/01/2022');";

      String sqlStatement = "SELECT * FROM teammembers LIMIT 10;";

      //send statement to DBMS
      //This executeQuery command is useful for data retrieval
      ResultSet result = stmt.executeQuery(sqlStatement);
      //OR
      //This executeUpdate command is useful for updating data
      //int result = stmt.executeUpdate(sqlStatement);

      //OUTPUT
      //You will need to output the results differently depeninding on which function you use
      System.out.println("--------------------Query Results--------------------");
      while (result.next()) {
        System.out.println(result.getString("student_name"));
      }
      //OR
      // System.out.println(result);
    } catch (Exception e){
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
    }
  }
public static void main(String args[]) {

  setupDatabase();

  


  // _________running commands________
  runSQLCommands();





  // _________________Close Connection__________________

  //closing the connection
  try {
    conn.close();
    System.out.println("Connection Closed.");
  } catch(Exception e) {
    System.out.println("Connection NOT Closed.");
  }

  // ___________________________________________________

}//end main
}//end Class
