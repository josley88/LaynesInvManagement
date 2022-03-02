import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class jdbcpostgreSQL {

  //Commands to run this script
  //This will compile all java files in this directory
  //javac *.java
  //This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
  //Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL.java
  //Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL.java

  // Access Database: psql -h csce-315-db.engr.tamu.edu -U csce315904_21user csce315904_21db

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


  // function to input weekly purchase into daily order tables. It will take in the file name and it will update the database directly
  public static void inputElementsIntoWeekOrders(String fileName){
    Scanner sc;
    
    try{
      Statement stmt = conn.createStatement();


      sc = new Scanner(new File(fileName));
      String filler = sc.nextLine().replace("\'", "\'\'");
      String[] parseArr = filler.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
      String tableName = sc.nextLine().replace("\'", "\'\'");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      String day = parseArr[0].strip();
      tableName =  fileName.substring((fileName.length()-13), (fileName.length()-4));
      String sqlStatement = "CREATE TABLE " + tableName + " ( ";
      // populates in the following order Item, Quantity, Total
      sqlStatement += parseArr[1].strip() + " TEXT PRIMARY KEY, " + parseArr[2].strip() + " INT, "+ parseArr[3].strip() + " TEXT );";
      print(sqlStatement);
      // SQL side;
      int result = stmt.executeUpdate(sqlStatement);
      print(result);

      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0){
          if(parseArr[1].equals("")!=true) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES (\'" + day + "_" + parseArr[1].strip() + "\'," + parseArr[2].strip() + "," + "\'" + parseArr[3].strip() + "\');";
           print(sqlStatement);
            result = stmt.executeUpdate(sqlStatement);
            print(result);
            for (String string : parseArr) {
              if (string.length() != 0) {
                print(string);
              }
            }
          }
          else{ // changes day if new day
            if(sc.hasNextLine()){ // checks if we are at end of file 
              //skips a line
              parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
              day = parseArr[0].strip();
              print("NEW DAY IS " + day);
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
      Statement stmt = conn.createStatement();


      sc = new Scanner(new File(fileName));
      String filler = sc.nextLine().replace("\'", "\'\'");
      String[] parseArr = filler.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
      String tableName = sc.nextLine().replace("\'", "\'\'").replace(" ", "_").replace("_#", "");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      tableName = "INVENTORY";
      String sqlStatement = "CREATE TABLE " + "INVENTORY" + " ( ";
      // populates in the following order Item, Quantity, Total
      sqlStatement += parseArr[1].strip() + " TEXT, " + parseArr[2].strip() + " TEXT PRIMARY KEY, "+ parseArr[3].strip() + " INT, "+ parseArr[4].strip() + " INT, "+ parseArr[5].strip() + " TEXT, "+ parseArr[6].strip() + " TEXT, "+ parseArr[7].strip() + " INT, "+ parseArr[8].strip() + " TEXT, "+ parseArr[9].strip() + " TEXT, "+ parseArr[10].strip() + " TEXT, "+ parseArr[11].strip() + " INT, "+ parseArr[12].strip() + " TEXT"+" );";
      print(sqlStatement);
      // SQL side;
      int result = stmt.executeUpdate(sqlStatement);
      print(result);
      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0 && sc.hasNextLine()){
          if(parseArr[1].equals("")!=true) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES (\'" + parseArr[1].strip() + "\',\'" + parseArr[2].strip() + "\'," + "\'" + parseArr[3].strip() + "\'" + "," + "\'" + parseArr[4].strip() + "\'" + "," + "\'" + parseArr[5].strip() + "\'"+ "," + "\'" + parseArr[6].strip() + "\'" + "," + "\'" + parseArr[7].strip() + "\'" + "," + "\'" + parseArr[8].strip() + "\'" + "," + "\'" + parseArr[9].strip() + "\'" + "," + "\'" + parseArr[10].strip() + "\'" + "," + "\'" + parseArr[11].strip() + "\'" + "," + "\'" + parseArr[12].strip() + "\'" + ");";
            print(sqlStatement);
            result = stmt.executeUpdate(sqlStatement);
            print(result);
            for (String string : parseArr) {
              if (string.length() != 0) {
                print(string);
              }
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





  
  // function to input elements into the Menu Table.

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
          print(string);
        }
      }
      sqlStatement += parseArr[1].strip() + " INT PRIMARY KEY, " + parseArr[2].strip() + " TEXT, "+ parseArr[3].strip() + " TEXT, " + parseArr[4].strip() + " TEXT"+" );";
      print(sqlStatement);
      // SQL side;
      Statement stmt = conn.createStatement();
      int result = stmt.executeUpdate(sqlStatement);
      print(result);
      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + ",\'" + parseArr[2].strip() + "\'," + "\'" + parseArr[3].strip() + "\'" + ",\'" + parseArr[4].strip() + "\');";
        print(sqlStatement);
        result = stmt.executeUpdate(sqlStatement);
        print(result);
        for (String string : parseArr) {
          if (string.length() != 0) {
            print(string);
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


  public static void inputItemConversions(String fileName){
    Scanner sc;
    String[] parseArr;
    
    try{
      sc = new Scanner(new File(fileName));
      String tableName = sc.nextLine().replace("\'", "\'\'");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      
      // only create table if it doesn't already exist
      if (!tableExist(conn, "itemconversion")) {
        print("Table doesn't exist");
        tableName = "itemConversion";
        String sqlStatement = "CREATE TABLE " + tableName + " ( ";
        // populates in the following order Item, Description 
        String tableFormatting = sc.nextLine().replace("\'", "\'\'");
        parseArr = tableFormatting.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        //sanitizes the parse Arr values for (" ")
        for (String string : parseArr) {
          if (string.length() != 0) {
            print(string);
          }
        }
        sqlStatement += parseArr[1].strip() + " INT PRIMARY KEY, " + parseArr[2].strip() + " TEXT );";
        print(sqlStatement);
        // SQL side;
        Statement stmt = conn.createStatement();
        int result = stmt.executeUpdate(sqlStatement);
        print(result);
        while (sc.hasNextLine()) {

          //creates array of elements in a line
          parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
          sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + ",\'" + parseArr[2].strip() + "\');";
          print(sqlStatement);
          result = stmt.executeUpdate(sqlStatement);
          print(result);
          for (String string : parseArr) {
            if (string.length() != 0) {
              print(string);
            }
          }
        }
      }


    } 
    catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  public static boolean tableExist(Connection conn, String tableName) throws SQLException {
  boolean tExists = false;
  ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
  
  try {
    while (rs.next()) { 
      String tName = rs.getString("TABLE_NAME");
      if (tName != null && tName.equals(tableName)) {
        
          tExists = true;
          break;
      }
    }
  } catch (Exception e) {
     print(e);
  }
  return tExists;
}

  // gets menu items from the database
  public static ArrayList<ArrayList<String>> getDBInventory () {
    try {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM inventory;");
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<String>();
        row.add(rs.getString("sku"));
        row.add(rs.getString("Description"));
        row.add(rs.getString("Quantity"));
        row.add(rs.getString("category"));
        row.add(rs.getString("delivered"));
        row.add(rs.getString("_price_"));
        result.add(row);
        // print(row.toString());
      }


      rs.close();
      return result;

    } catch (SQLException e) {
      e.printStackTrace();
    }


    return null;
  }

  public static ArrayList<ArrayList<String>> getDBDTO () {
    try {
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM weeksales;");
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      print("OPENED: " + rs.next());
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<String>();
        row.add(rs.getString("item"));
        row.add(rs.getString("quantity"));
        result.add(row);
        //print(row.toString());
      }


      rs.close();
      return result;

    } catch (SQLException e) {
      e.printStackTrace();
    }


    return null;
  }

  public static void setupDatabase() {

   try {
      conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    print("Opened database successfully");
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
      print("--------------------Query Results--------------------");
      while (result.next()) {
        print(result.getString("student_name"));
      }
      // OR
      // print(result);
    } catch (Exception e){
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
    }
  }


  public static void closeConnection() {
    try {
      conn.close();
      print("Connection Closed.");
    } catch(Exception e) {
      print("Connection NOT Closed.");
    }
  }


  public static void main(String args[]) {

  

    // _________setup the database_______
    setupDatabase();

    print("---- Input Beginning ----");

    //inputElementsIntoWeekOrders("./CSCE315-1/FourthWeekSales.csv");
    String[] menuItems;
    inputItemConversions("./CSCE315-1/menuItemConversion.csv");
    print("---- Input Finished ----");

  //  for (String s : menuItems) {
  //    print(menuItems);
  //  }


    // setup manager GUI frame and attach Manager class
    JFrame managerGUI = new JFrame();
    Manager manager = new Manager();

    JFrame serverGUI = new JFrame();
    Server server = new Server();

    serverGUI.setContentPane(server.getRootPanel());
    serverGUI.setSize(1280, 720);

    managerGUI.setContentPane(manager.getRootPanel());
    managerGUI.setSize(1280, 720);

    // fill data into manager GUI
    ArrayList<ArrayList<String>> inventoryDB = getDBInventory();
    ArrayList<ArrayList<String>> DTODB = getDBDTO();
    for (ArrayList<String> row : inventoryDB) {
      manager.addRowToInventoryTable(row.toArray());
    }
    for (ArrayList<String> row : DTODB) {
      manager.addRowToDTOTable(row.toArray());
    }

  // display manager GUI
    managerGUI.setVisible(true);
    serverGUI.setVisible(true);




    // ____________ button and table listeners _____________
    // DELETE currently selected row
    manager.deleteRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        String submitValue=manager.inventoryTextField.getText();
        Statement statement=null;
        try{
          statement=conn.createStatement();
          int result=statement.executeUpdate(submitValue);
          refreshTablesFromDB(manager);
        }catch(SQLException e){
          e.printStackTrace();
        }
      }
    });

    // UPDATE currently selected row view
    manager.getInventoryTable().addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        JTable target = (JTable)e.getSource();
        int row = target.getSelectedRow();
        int column = target.getSelectedColumn();
        print("Clicked Row:" + row);
        for (int i = 0; i < manager.editTableModel.getColumnCount(); i++) {
          manager.editTableModel.setValueAt(manager.inventoryTableModel.getValueAt(row, i), 0, i);
        }

      }
    });

    // ADD new row button
    manager.addRowButton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent ae){
            ArrayList<String> row = new ArrayList<String>(manager.getInventoryTable().getColumnCount());
            manager.addRowToInventoryTable(row.toArray());
        }
    });

    // EDIT currently selected row button
    manager.editRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(5);
        for (int j = 0; j < 5; j++) {
          row.add((String) manager.editTableModel.getValueAt(0, j));
        }

        // update database, then refresh page
        Statement statement = null;
        try {
          statement = conn.createStatement();
          //ResultSet rs = statement.executeQuery("DELETE FROM inventory WHERE ;");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });



  }

  public static void refreshTablesFromDB(Manager manager) {
    //INSERT INTO inventory VALUES ('Ice Cream', 9, 'Cold', 3, '12.55');

    ArrayList<ArrayList<String>> inventoryDB = getDBInventory();
    ArrayList<ArrayList<String>> DTODB = getDBDTO();
    manager.clearTables();
    for (ArrayList<String> row : inventoryDB) {
      manager.addRowToInventoryTable(row.toArray());
    }
    for (ArrayList<String> row : DTODB) {
      manager.addRowToDTOTable(row.toArray());
    }
  }

  public static void uploadTablesToDB(Manager manager) {

  }

  // print helper function
  public static void print(Object out) {
    System.out.println(out);
  }
}












/*
database communication psuedocode

week sales taking from menukey and itemconversion
  // item string example - sunday_501 or friday_512
  weeksales-> item.substring(item.length()-3); 
  match above to menukey to grab price
  match above to menuitemconversion to grab ingredients
    menuitemconversion conversion to ingredients
      description is in form string=amount; string=amount;
      use split ";" to grab string, then seperate by "="
        left side of = should match inventory ingredients (after tolowercase)
          right side of = is the quantity

*/