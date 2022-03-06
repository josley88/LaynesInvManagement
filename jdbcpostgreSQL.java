import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class jdbcpostgreSQL {

  // Commands to run this script
  // This will compile all java files in this directory
  // javac *.java
  // This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
  // Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL.java
  // Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL.java

  // Access Database: psql -h csce-315-db.engr.tamu.edu -U csce315904_21user csce315904_21db
  // run this program from CMD: java -classpath ".\out\production\CSCE315Project2Team21;postgresql-42.2.8.jar" jdbcpostgreSQL

  // MAKE SURE YOU ARE ON VPN or TAMU WI-FI TO ACCESS DATABASE

  // _________________Global Variables_________________
  public static Connection conn = null;
  public static String teamNumber = "21";
  public static String sectionNumber = "904";
  public static String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
  public static String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
  public static String userName = "csce315" + sectionNumber + "_" + teamNumber + "user";
  public static String userPassword = "315gang";
  //___________________________________________________


  // _______________________________________________ MAIN ____________________________________________________
  public static void main(String[] args) throws SQLException {

    // create connection -----------------------------------------------------
    openConnection();
    // -----------------------------------------------------------------------



    // populate database if empty --------------------------------------------
//    inputElementsIntoWeekOrders("./CSCE315-1/FirstWeekSales.csv");
//    inputElementsIntoWeekOrders("./CSCE315-1/SecondWeekSales.csv");
//    inputElementsIntoWeekOrders("./CSCE315-1/ThirdWeekSales.csv");
//    inputElementsIntoWeekOrders("./CSCE315-1/FourthWeekSales.csv");

    inputElementsIntoInventory("./CSCE315-1/First day order.csv");
    inputItemConversions("./CSCE315-1/menuItemConversion.csv");
    inputElementsIntoMenuTable("./CSCE315-1/MenuKey.csv");
    // -----------------------------------------------------------------------



    // setup manager and server GUI frame and attach Manager class -----------
    JFrame managerGUI = new JFrame();
    Manager manager = new Manager();
    JFrame serverGUI = new JFrame();
    Server server = new Server();

    serverGUI.setContentPane(server.getRootPanel());
    serverGUI.setSize(1280, 720);
    managerGUI.setContentPane(manager.getRootPanel());
    managerGUI.setSize(1280, 720);
    // -----------------------------------------------------------------------



    // fill tables and set up event listeners --------------------------------
    refreshTablesFromDB(manager);
    setupManagerEventListeners(manager);
    // -----------------------------------------------------------------------



    // display server and manager GUI ----------------------------------------
    managerGUI.setVisible(true);
    serverGUI.setVisible(true);
    // -----------------------------------------------------------------------
  }

  


  // ____________________________________________ FUNCTIONS __________________________________________________


  public static String changeDate(String fileName){
    int indexOfFirstBS = fileName.indexOf('/');
    String month = fileName.substring(fileName.indexOf('/')+1);
    int indexOf2ndBS = month.indexOf('/') + fileName.length() - month.length();
    month = fileName.substring(0,indexOfFirstBS);
    String day = fileName.substring(indexOfFirstBS + 1 , indexOf2ndBS);
    String year = fileName.substring(indexOf2ndBS + 1);
    int monthI = Integer.parseInt(month);
    int dayI = Integer.parseInt(day);
    int yearI = Integer.parseInt(year);
    
    if(monthI == 2){
        if(dayI == 28){
            dayI = 1;
            monthI = 3;
        }
        else{
            dayI++;
        }
    }else  if(monthI == 1){
      if(dayI == 31){
        dayI = 1;
        monthI = 2;
      }
      else{
        dayI++;
      }
    }
    else if(monthI == 3){
        if(dayI == 31){
            dayI = 1;
            monthI = 4;
        }
        else{
            dayI++;
        }
    }
    else{
        dayI++;
    }
    fileName = monthI + "/" + dayI + "/" + yearI;
    return fileName;
  }


  // function to input weekly purchase into daily order tables. It will take in the file name, and it will update the database directly
  public static void inputElementsIntoWeekOrders(String fileName) throws SQLException, FileNotFoundException {
    // skip if table already exists
    Boolean append = false;
    if (tableExist(conn, "weeksales")) {
      append = true;
    }

    Scanner sc;
    
    try{
      Statement stmt = conn.createStatement();
      String startDate = "01/30/2022";
      if(fileName.equals("./CSCE315-1/SecondWeekSales.csv")){
        startDate = "02/06/2022";
      }
      else if(fileName.equals("./CSCE315-1/ThirdWeekSales.csv")){
        startDate = "02/13/2022";
      }
      else if(fileName.equals("./CSCE315-1/FourthWeekSales.csv")){
        startDate = "02/20/2022";
      }

      sc = new Scanner(new File(fileName));
      String filler = sc.nextLine().replace("\'", "\'\'");
      String[] parseArr = filler.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    
      String tableName = sc.nextLine().replace("\'", "\'\'");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      String day = parseArr[0].strip();
      tableName =  fileName.substring((fileName.length()-13), (fileName.length()-4));
      String sqlStatement = "CREATE TABLE " + tableName + " ( ";
      // populates in the following order Item, Quantity, Total
      sqlStatement += parseArr[1].strip() + " TEXT, " + parseArr[2].strip() + " INT, " + parseArr[3].strip() + " TEXT, " + "DateOPurchase" + " DATE );";
      print(sqlStatement);
      // SQL side;
      int result = 0;
      if(!append){
        result = stmt.executeUpdate(sqlStatement);
        print(result);
      }


      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0){
          if(parseArr[1].equals("")!=true) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES (\'" + day + "_" + parseArr[1].strip() +  "\',\'" + parseArr[2].strip() + "\'" + "," + "\'" + parseArr[3].strip() + "\'" + "," + "\'" + startDate+ "\');";
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
              startDate = changeDate(startDate);
              parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
              day = parseArr[0].strip();
              print("NEW DAY IS " + day);
            }
          }
        }
      }




    } catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }
  }

  // function to input the elements into the inventory
  public static void inputElementsIntoInventory(String fileName) throws SQLException {
    // skip if table already exists
    if (tableExist(conn, "inventory")) {
      return;
    }

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
      sqlStatement += parseArr[1].strip() + " TEXT, " + parseArr[2].strip() + " TEXT PRIMARY KEY, "+ parseArr[3].strip() + " FLOAT, "+ "FillAmt" + " FLOAT, " + parseArr[4].strip() + " INT, "+ parseArr[5].strip() + " TEXT, "+ parseArr[6].strip() + " TEXT, "+ parseArr[7].strip() + " INT, "+ parseArr[8].strip() + " TEXT, "+ parseArr[9].strip() + " TEXT, "+ parseArr[10].strip() + " TEXT, "+ parseArr[11].strip() + " INT, "+ parseArr[12].strip() + " TEXT"+" );";
      print(sqlStatement);
      // SQL side;
      int result = stmt.executeUpdate(sqlStatement);
      print(result);
      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("\'", "\'\'").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0 && sc.hasNextLine()){
          if(parseArr[1].equals("")!=true) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES (\'" + parseArr[1].strip() + "\',\'" + parseArr[2].strip() + "\'," + "\'" + parseArr[3].strip() + "\'" + ","  + "\'" + parseArr[3].strip() + "\'" + ","+ "\'" + parseArr[4].strip() + "\'" + "," + "\'" + parseArr[5].strip() + "\'"+ "," + "\'" + parseArr[6].strip() + "\'" + "," + "\'" + parseArr[7].strip() + "\'" + "," + "\'" + parseArr[8].strip() + "\'" + "," + "\'" + parseArr[9].strip() + "\'" + "," + "\'" + parseArr[10].strip() + "\'" + "," + "\'" + parseArr[11].strip() + "\'" + "," + "\'" + parseArr[12].strip() + "\'" + ");";
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
  public static void inputElementsIntoMenuTable(String fileName) throws SQLException {
    // skip if table already exists
    if (tableExist(conn, "menu_key")) {
      return;
    }

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

  // function to input item conversions
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

  // helper function to determine if a table already exists in the database
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

  // gets Inventory from the database
  public static ArrayList<ArrayList<String>> getDBInventory () {
    try {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM inventory;");
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<String>();
        row.add(rs.getString("description"));
        row.add(rs.getString("sku"));
        row.add(rs.getString("Quantity"));
        row.add(rs.getString("delivered"));
        row.add(rs.getString("sold_by"));
        row.add(rs.getString("delivered_by"));
        row.add(rs.getString("quantity_multiplyer"));
        row.add(rs.getString("_price_"));
        row.add(rs.getString("_extended_"));
        row.add(rs.getString("category"));
        row.add(rs.getString("invoice_line"));
        row.add(rs.getString("detailed_description"));
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

  // gets Daily Total Orders from the database
  public static ArrayList<ArrayList<String>> getDBDTO () {
    try {
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM weeksales;");
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      //print("OPENED: " + rs.next());
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

  // gets Menu Items from the database
  public static ArrayList<ArrayList<String>> getDBMenuItems () {
    try {
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM menu_key;");
      ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
      //print("OPENED: " + rs.next());
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<String>();
        row.add(rs.getString("item"));
        row.add(rs.getString("name"));
        row.add(rs.getString("description"));
        row.add(rs.getString("price"));
        result.add(row);
//        print(row.toString());
      }


      rs.close();
      return result;

    } catch (SQLException e) {
      e.printStackTrace();
    }


    return null;
  }

  // open database connection
  public static void openConnection() {

   try {
      conn = DriverManager.getConnection(dbConnectionString,userName, userPassword);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    print("Opened database successfully");
  }

  // close database connection
  public static void closeConnection() {
    try {
      conn.close();
      print("Connection Closed.");
    } catch(Exception e) {
      print("Connection NOT Closed.");
    }
  }

  // refresh tables in manager GUI
  public static void refreshTablesFromDB(Manager manager) {
    ArrayList<ArrayList<String>> inventoryDB = getDBInventory();
    ArrayList<ArrayList<String>> DTODB = getDBDTO();
    ArrayList<ArrayList<String>> menuItemsDB = getDBMenuItems();
    manager.clearTables();

    for (ArrayList<String> row : inventoryDB) {
      manager.addRowToInventoryTable(row.toArray());
    }
    for (ArrayList<String> row : DTODB) {
      manager.addRowToDTOTable(row.toArray());
    }
    for (ArrayList<String> row : menuItemsDB) {
      manager.addRowTomMenuItemsTable(row.toArray());
    }
  }

  // following functions set up the event listeners for buttons and tables in manager GUI
  public static void setupManagerEventListeners(Manager manager) {
    setupInventoryEventListeners(manager);
    setupDTOEventListeners(manager);
    setupMenuItemsListeners(manager);
  }
  public static void setupInventoryEventListeners(Manager manager) {
    // DELETE currently selected row
    manager.invDeleteRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        try {
          Statement statement = conn.createStatement();
          String currentSKU = (String) manager.invEditTableModel.getValueAt(0, 1);
//          print(currentSKU);
          String sqlStatement = "DELETE FROM inventory WHERE sku='" + currentSKU + "';";
//          print(sqlStatement);
          int rs = statement.executeUpdate(sqlStatement);
//          print("Delete result: " + rs);
          refreshTablesFromDB(manager);
        }catch(SQLException e){
          e.printStackTrace();
        }
      }
    });

    // UPDATE currently selected row view
    manager.invTable.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        JTable target = (JTable)e.getSource();
        int row = target.getSelectedRow();

        for (int i = 0; i < manager.invEditTableModel.getColumnCount(); i++) {
          manager.invEditTable.setValueAt(manager.invTable.getValueAt(row, i), 0, i);
        }

      }
    });

    // ADD new row from editTable button
    manager.invAddRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(manager.invEditTableModel.getColumnCount());
        for (int j = 0; j < manager.invEditTableModel.getColumnCount(); j++) {
          row.add((String) manager.invEditTableModel.getValueAt(0, j));
        }

        String sqlStatement =
                "INSERT INTO inventory (description, sku, quantity, delivered, sold_by, delivered_by, quantity_multiplyer, _price_, _extended_, category, invoice_line, detailed_description) " +
                        "VALUES ('" + row.get(0) + "', '" + row.get(1) + "', " + row.get(2) + ", " + row.get(3) + ", '" + row.get(4) + "', '" + row.get(5) + "', " + row.get(6) + ", '" + row.get(7) + "', '" + row.get(8) + "', '" + row.get(9) + "', " + row.get(10) + ", '" + row.get(11) + "');";

//        print(sqlStatement);

        try {
          Statement stmt = conn.createStatement();
          int rs = stmt.executeUpdate(sqlStatement);
//          print("Result Add Row: " + rs);
          refreshTablesFromDB(manager);
        } catch (SQLException e) {
          e.printStackTrace();
        }

      }
    });

    // EDIT currently selected row button
    manager.invEditRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(manager.invEditTableModel.getColumnCount());
        for (int j = 0; j < manager.invEditTableModel.getColumnCount(); j++) {
          row.add((String) manager.invEditTableModel.getValueAt(0, j));
        }

        // update database, then refresh page
        Statement statement = null;
        try {
          statement = conn.createStatement();
          String currentSKU = row.get(0);

          // build sql updateQuery
          String sqlStatement =
                  "UPDATE inventory " +
                          "SET " +
                          "description = '" + row.get(0) +
                          "', sku = '" + row.get(1) +
                          "', quantity = " + row.get(2) +
                          ", delivered = " + row.get(3) +
                          ", sold_by = '" + row.get(4) +
                          "', delivered_by = '" + row.get(5) +
                          "', quantity_multiplyer = " + row.get(6) +
                          ", _price_ = '" + row.get(7) +
                          "', _extended_ = '" + row.get(8) +
                          "', category = '" + row.get(9) +
                          "', invoice_line = " + row.get(10) +
                          ", detailed_description = '" + row.get(11) + "' " +
                          "WHERE sku = '" + row.get(1) + "';";

//          print(sqlStatement);
          int rs = statement.executeUpdate(sqlStatement);
//          print("Update result: " + rs);
          refreshTablesFromDB(manager);
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    // REFRESH button
    manager.invRefreshButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        refreshTablesFromDB(manager);
      }
    });
  }
  public static void setupDTOEventListeners(Manager manager) {
    // DELETE currently selected row
    manager.DTODeleteRowButton.addActionListener(ae -> {
      try {
        Statement statement = conn.createStatement();
        String currentItem = (String) manager.DTOEditTableModel.getValueAt(0, 0);
//        print(currentItem);
        String sqlStatement = "DELETE FROM weeksales WHERE item='" + currentItem + "';";
//        print(sqlStatement);
        int rs = statement.executeUpdate(sqlStatement);
//        print("Delete result: " + rs);
        refreshTablesFromDB(manager);
      }catch(SQLException e){
        e.printStackTrace();
      }
    });

    // UPDATE currently selected row view
    manager.DTOTable1.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        JTable target = (JTable)e.getSource();
        int row = target.getSelectedRow();

        for (int i = 0; i < manager.DTOEditTableModel.getColumnCount(); i++) {
          manager.DTOEditTable.setValueAt(manager.DTOTable1.getValueAt(row, i), 0, i);
        }

      }
    });

    // ADD new row from editTable button
    manager.DTOAddRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(manager.DTOEditTableModel.getColumnCount());
        for (int j = 0; j < manager.DTOEditTableModel.getColumnCount(); j++) {
          row.add((String) manager.DTOEditTableModel.getValueAt(0, j));
        }

        String sqlStatement =
                "INSERT INTO weeksales (item, quantity, total) " +
                        "VALUES ('" + row.get(0) + "', " + row.get(1) + ", 0);";

//        print(sqlStatement);

        try {
          Statement stmt = conn.createStatement();
          int rs = stmt.executeUpdate(sqlStatement);
//          print("Result Add Row: " + rs);
          refreshTablesFromDB(manager);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });

    // EDIT currently selected row button
    manager.DTOEditRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(manager.DTOEditTableModel.getColumnCount());
        for (int j = 0; j < manager.DTOEditTableModel.getColumnCount(); j++) {
          row.add((String) manager.DTOEditTableModel.getValueAt(0, j));
        }

        // update database, then refresh page
        Statement statement = null;
        try {
          statement = conn.createStatement();
          String currentItem = row.get(0);

          // build sql updateQuery
          String sqlStatement =
                  "UPDATE weeksales " +
                          "SET " +
                          "item = '" + row.get(0) +
                          "', quantity = " + row.get(1) + ", total = 0 " +
                          "WHERE item = '" + row.get(0) + "';";

//          print(sqlStatement);
          int rs = statement.executeUpdate(sqlStatement);
//          print("Update result: " + rs);
          refreshTablesFromDB(manager);
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    // REFRESH button
    manager.DTORefreshButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        refreshTablesFromDB(manager);
      }
    });
  }
  public static void setupMenuItemsListeners(Manager manager) {
    // MENU ITEMS -------- ---------------------------------------------------------------------------------------
    // DELETE currently selected row
    manager.menuItemsDeleteRowButton.addActionListener(ae -> {
      try {
        Statement statement = conn.createStatement();
        String currentItem = (String) manager.menuItemsEditTableModel.getValueAt(0, 0);
//        print(currentItem);
        String sqlStatement = "DELETE FROM menu_key WHERE item = " + currentItem + ";";
//        print(sqlStatement);
        int rs = statement.executeUpdate(sqlStatement);
//        print("Delete result: " + rs);
        refreshTablesFromDB(manager);
      }catch(SQLException e){
        e.printStackTrace();
      }
    });

    // UPDATE currently selected row view
    manager.menuItemsTable.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        JTable target = (JTable)e.getSource();
        int row = target.getSelectedRow();

        for (int i = 0; i < manager.menuItemsEditTableModel.getColumnCount(); i++) {
          manager.menuItemsEditTable.setValueAt(manager.menuItemsTable.getValueAt(row, i), 0, i);
        }

      }
    });

    // ADD new row from editTable button
    manager.menuItemsAddRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(manager.menuItemsEditTableModel.getColumnCount());
        for (int j = 0; j < manager.menuItemsEditTableModel.getColumnCount(); j++) {
          row.add((String) manager.menuItemsEditTableModel.getValueAt(0, j));
        }

        String sqlStatement =
                "INSERT INTO menu_key (item, name, description, price) " +
                        "VALUES (" + row.get(0) + ", '" + row.get(1) + "', '" + row.get(2) + "', '" + row.get(3) + "');";

//        print(sqlStatement);

        try {
          Statement stmt = conn.createStatement();
          int rs = stmt.executeUpdate(sqlStatement);
//          print("Result Add Row: " + rs);
          refreshTablesFromDB(manager);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    });

    // EDIT currently selected row button
    manager.menuItemsEditRowButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        ArrayList<String> row = new ArrayList<String>(manager.menuItemsEditTableModel.getColumnCount());
        for (int j = 0; j < manager.menuItemsEditTableModel.getColumnCount(); j++) {
          row.add((String) manager.menuItemsEditTableModel.getValueAt(0, j));
        }

        Statement statement;
        try {
          statement = conn.createStatement();
          String currentItem = row.get(0);
          String sqlStatement =
                  "UPDATE menu_key " +
                          "SET " +
                          "item = " + row.get(0) +
                          ", name = '" + row.get(1) +
                          "', description = '" + row.get(2) +
                          "', price = '" + row.get(3) + "' " +
                          "WHERE item = " + row.get(0) + ";";

//          print(sqlStatement);
          int rs = statement.executeUpdate(sqlStatement);
//          print("Update result: " + rs);
          refreshTablesFromDB(manager);
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    });

    // REFRESH button
    manager.menuItemsRefreshButton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        refreshTablesFromDB(manager);
      }
    });
  }
  // _____________________________________________________________________________________

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