import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
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
  //current date: meant to aid in the prgram in updating the inventory 
//  public static String currentDate = "2022-01-30";


  public static Manager manager;
  //___________________________________________________


  // _______________________________________________ MAIN ____________________________________________________
  public static void main(String[] args) throws SQLException {
    
    //currentDate = "2022-02-26";


    // create connection -----------------------------------------------------
    openConnection();
    // -----------------------------------------------------------------------

    // setup manager and server GUI frame and attach Manager class -----------
    JFrame managerGUI = new JFrame("Layne's Manager");
    manager = new Manager();
    JFrame serverGUI = new JFrame("Layne's Server");
    Server server = new Server();

    serverGUI.setContentPane(server.getRootPanel());
    serverGUI.setSize(1280, 720);
    managerGUI.setContentPane(manager.getRootPanel());
    managerGUI.setSize(1280, 720);
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

    // fill tables and set up event listeners --------------------------------
    refreshTablesFromDB();
    setupManagerEventListeners();
    // -----------------------------------------------------------------------



    // display server and manager GUI ----------------------------------------
    managerGUI.setVisible(true);
    serverGUI.setVisible(true);
    // -----------------------------------------------------------------------

    log("Initialized");
  }

  


  // ____________________________________________ FUNCTIONS __________________________________________________

  // helper function for changing date in inputElementsIntoWeekOrders
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

// purpose of this command is to get the most up-to-date version of weeksales database from db
  public static ArrayList<ArrayList<String>> getweeksales() {
    try {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM weeksales;");
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<>();
        row.add(rs.getString("item"));
        row.add(rs.getString("quantity"));
        row.add(rs.getString("total"));
        row.add(rs.getString("dateofpurchase"));
        
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

// purpose of this command is to get the most up-to-date version of itemconversion from db

  public static ArrayList<ArrayList<String>> getItemConversion() {
    try {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM itemconversion;");
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<>();
        row.add(rs.getString("item"));
        row.add(rs.getString("description"));
        
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
  // purpose of this command is to get the most up-to-date version of menukey database from table

  public static ArrayList<ArrayList<String>> getMenuKey() {
    try {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM menu_key;");
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<>();
        row.add(rs.getString("item"));
        row.add(rs.getString("name"));
        row.add(rs.getString("description"));
        row.add(rs.getString("price"));
        
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
  public static void executeSingleInvUpdate(String item, double oldTotal,  double amt){

    try {
      Statement stmt = jdbcpostgreSQL.conn.createStatement();
      double newVal = oldTotal - amt;
      stmt.executeUpdate("UPDATE inventory SET quantity=" + newVal + " WHERE description='" + item + "';");

    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public static void updateInventoryGivenRange(String dateA, String dateB) throws SQLException {
    ArrayList<ArrayList<String>> invUpdates = getItemConversionsFromDateRange(dateA, dateB);
    try{
      Statement stmt = jdbcpostgreSQL.conn.createStatement();
      ResultSet result = stmt.executeQuery("SELECT description,quantity FROM inventory;");
      Statement stmt2 = jdbcpostgreSQL.conn.createStatement();
      while(result.next()){
        for (ArrayList<String> invUpdate : invUpdates) {
          //print(invUpdate.get(0) + " : " + result.getString("description"));
          if (invUpdate.get(0).equals(result.getString("description"))) {
            double amt = Double.parseDouble(result.getString("quantity"));
            //print(amt + " " + Double.parseDouble(invUpdate.get(1)));
            amt = amt - Double.parseDouble(invUpdate.get(1));
            String sqlstmt = "UPDATE inventory SET quantity='" + amt + "' WHERE description='" + result.getString("description") + "';";
            print(sqlstmt);
            int rs = stmt2.executeUpdate(sqlstmt);
            if(rs == -123012) {
              print(rs);
            }
          }
        }
      }
    }catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }
  }

  // purpse of the func is to update inven database given global vars date.
  public static void updateInventoryGivenDate(String date){
    try{
      ArrayList<ArrayList<String>> weeksales =  getweeksales();
      ArrayList<ArrayList<String>> MenuItemConvert =  getItemConversion();
      ArrayList<ArrayList<String>> menuKey =  getMenuKey();
      ArrayList<ArrayList<String>> itemsToUpdate = new ArrayList<>();
      ArrayList<ArrayList<String>> inventory = getDBInventory();
        if(weeksales == null){
         return;
        }

        //sets up function to fill with items to update
      for (ArrayList<String> weeksale : weeksales) {
        if (weeksale.get(3).trim().equals(date.trim())) {
          itemsToUpdate.add(weeksale);
        }
      }

        ArrayList<ArrayList<String>> temp = new ArrayList<>();
      for (ArrayList<String> items : itemsToUpdate) {
        ArrayList<String> tempLine = new ArrayList<>();
        //templine = food number, base ingredients, base additionals, quantity, oldAmt

        tempLine.add(items.get(0).substring(items.get(0).indexOf('_') + 1));

        for (int menuNumber = 0; menuNumber < (MenuItemConvert != null ? MenuItemConvert.size() : 0); menuNumber++) {
          if (tempLine.get(0).trim().equals(MenuItemConvert.get(menuNumber).get(0).trim())) {
            tempLine.add(MenuItemConvert.get(menuNumber).get(1)); // adds base ingrediants
          }
        }
        //print("starting here\n" + itemsToUpdate + "\n\n");

        for (int menuNumber = 0; menuNumber < Objects.requireNonNull(menuKey).size(); menuNumber++) {
          if (tempLine.get(0).equals(menuKey.get(menuNumber).get(0))) {
            tempLine.add(menuKey.get(menuNumber).get(2)); // adds additionals
          }
        }

        tempLine.add(items.get(1)); // adds quantity

        for (int oldAmt = 0; oldAmt < Objects.requireNonNull(inventory).size(); oldAmt++) {
          if (tempLine.get(1).split(";")[0].split("=")[0].trim().equals(inventory.get(oldAmt).get(0))) {
            tempLine.add(inventory.get(oldAmt).get(2)); // adds old total
          }
        }

        temp.add(tempLine);
      }
        itemsToUpdate = temp;

//       print("starting here\n" + itemsToUpdate + "\n\n");
//        public static String currentDate = "2022-03-01";

      //description how much to change by
      temp = getItemConversionsFromDateRange(date, date);
      print("starting here\n" + itemsToUpdate + "\n\n");
      for(int i = 0; i < temp.size(); i++){
       // print("starting here\n" + itemsToUpdate.get(i).get(4).split(";")[0] + "\n\n");
        double oldAmt = 0;
        String item = temp.get(i).get(0);
        for(int oldAmtt = 0; oldAmtt < Objects.requireNonNull(inventory).size(); oldAmtt++){
          if(item.strip().equals(inventory.get(oldAmtt).get(0))){
            oldAmt = Double.parseDouble(inventory.get(oldAmtt).get(2)); // adds old total
          }
        }
        double changeBy = Double.parseDouble(temp.get(i).get(1));

        executeSingleInvUpdate(item,oldAmt,changeBy);

      }

      
    
    } catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }
  }



  // function to input weekly purchase into daily order tables. It will take in the file name, and it will update the database directly
  public static void inputElementsIntoWeekOrders(String fileName) throws SQLException {
    // skip if table already exists
    boolean append = tableExist(conn, "weeksales");

    Scanner sc;
    
    try{
      Statement stmt = conn.createStatement();
      String startDate = switch (fileName) {
        case "./CSCE315-1/SecondWeekSales.csv" -> "02/06/2022";
        case "./CSCE315-1/ThirdWeekSales.csv" -> "02/13/2022";
        case "./CSCE315-1/FourthWeekSales.csv" -> "02/20/2022";
        default -> "01/30/2022";
      };

      sc = new Scanner(new File(fileName));
      String[] parseArr;
    
      String tableName = sc.nextLine().replace("'", "''");
      parseArr = tableName.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      String day = parseArr[0].strip();
      tableName =  fileName.substring((fileName.length()-13), (fileName.length()-4));
      String sqlStatement = "CREATE TABLE " + tableName + " ( ";
      // populates in the following order Item, Quantity, Total
      sqlStatement += parseArr[1].strip() + " TEXT, " + parseArr[2].strip() + " INT, " + parseArr[3].strip() + " TEXT, " + "DateOfPurchase" + " DATE );";
      print(sqlStatement);
      // SQL side;
      int result;
      if(!append){
        result = stmt.executeUpdate(sqlStatement);
        print(result);
      }


      while(sc.hasNextLine()){
        
        //creates array of elements in a line
        parseArr = sc.nextLine().replace("'", "''").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0){
          if(!parseArr[1].equals("")) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES ('" + day + "_" + parseArr[1].strip() +  "','" + parseArr[2].strip() + "'" + "," + "'" + parseArr[3].strip() + "'" + "," + "'" + startDate+ "');";
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
              parseArr = sc.nextLine().replace("'", "''").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
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
      String[] parseArr;
    
      String tableName = sc.nextLine().replace("'", "''").replace(" ", "_").replace("_#", "");
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
        parseArr = sc.nextLine().replace("'", "''").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if(parseArr.length>0 && sc.hasNextLine()){
          if(!parseArr[1].equals("")) {
            sqlStatement = "INSERT INTO " + tableName + " VALUES ('" + parseArr[1].strip() + "','" + parseArr[2].strip() + "'," + "'" + parseArr[3].strip() + "'" + ","  + "'" + parseArr[3].strip() + "'" + ","+ "'" + parseArr[4].strip() + "'" + "," + "'" + parseArr[5].strip() + "'"+ "," + "'" + parseArr[6].strip() + "'" + "," + "'" + parseArr[7].strip() + "'" + "," + "'" + parseArr[8].strip() + "'" + "," + "'" + parseArr[9].strip() + "'" + "," + "'" + parseArr[10].strip() + "'" + "," + "'" + parseArr[11].strip() + "'" + "," + "'" + parseArr[12].strip() + "'" + ");";
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
      String tableName;
      String[] parseArr;
      
      tableName = "Menu_Key";
      String sqlStatement = "CREATE TABLE " + "Menu_Key" + " ( ";
      // populates in the following order Item, name, Description, price 
      String tableFormatting = sc.nextLine().replace("'", "''");
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
        parseArr = sc.nextLine().replace("'", "''").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + ",'" + parseArr[2].strip() + "'," + "'" + parseArr[3].strip() + "'" + ",'" + parseArr[4].strip() + "');";
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
      String tableName;
      
      // only create table if it doesn't already exist
      if (!tableExist(conn, "itemconversion")) {
        print("Table doesn't exist");
        tableName = "itemConversion";
        String sqlStatement = "CREATE TABLE " + tableName + " ( ";
        // populates in the following order Item, Description 
        String tableFormatting = sc.nextLine().replace("'", "''");
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
          parseArr = sc.nextLine().replace("'", "''").split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
          sqlStatement = "INSERT INTO " + tableName + " VALUES (" + parseArr[1].strip() + ",'" + parseArr[2].strip() + "');";
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
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<>();
        row.add(rs.getString("description"));
        row.add(rs.getString("sku"));
        row.add(rs.getString("Quantity"));
        row.add(rs.getString("fillamt"));
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

  // gets MenuItem to Inventory Conversions from the database
  public static ArrayList<ArrayList<String>> getItemConversionsFromDateRange(String dateA, String dateB) throws SQLException {
    // psuedo-psuedo code for item conversions --> THIS CODE HAS NOT BEEN TESTED, BUT LOGIC IS THERE <--

    // populate array of item conversions
    Statement stmt = jdbcpostgreSQL.conn.createStatement();
    String[] converArr = new String[20];


    ResultSet result = stmt.executeQuery("SELECT * FROM itemconversion;");
    while(result.next()){

      // first value is the # of that item used, then its description
      int i = result.getInt("item")-501;
      converArr[i] = "0;" + result.getString("description");
      print("Conversion Array: " + converArr[i]);
    }
    //grab resultset for weeksales, grab total # of each item used in timeframe
    String sqlStmt = "SELECT * FROM weeksales WHERE dateofpurchase >= '" + dateA + "' AND dateofpurchase <= '" + dateB + "';";
    result = stmt.executeQuery(sqlStmt);
    // SELECT * FROM weeksales WHERE dateofpurchase > '2022-01-30' AND dateofpurchase < '2022-02-01';
    print("ParseArr: ");
    while(result.next()){
      String item = result.getString("item");

      //splits the description into an array, recombine later
      int i = Integer.parseInt(item.substring(item.length() - 3)) - 501;
      String convertItem = converArr[i];
      String[] parseArr = convertItem.split(";");

      //the # of used updated
      parseArr[0] = String.valueOf(Integer.parseInt(parseArr[0]) + result.getInt("quantity"));

      //put string back together
      converArr[i] = String.join(";",parseArr);

      for (String s : parseArr) {
        System.out.print(s + " | ");
      }
      print("");
    }

    // now converArr has: AMOUNT;.....restofdescription..... on each index
    // final part
    print("Amount Used: ");
    ArrayList<ArrayList<String>> finalArr = new ArrayList<>();
    for(String convItem : converArr){
      String[] parseArr = convItem.split(";");
      int multiplier = Integer.parseInt(parseArr[0]);

      // might need an extra column in inventory for 'used'
      // or some temporary column in the manager gui for that date range


      // iterate the new parsed array from 2nd index onwards
      // each is 'description=amount'

      String[] descArray = Arrays.copyOfRange(parseArr, 1, parseArr.length);
      for(String desc : descArray){
        String[] parseDesc = desc.split("=");
        double invUsed = Double.parseDouble(parseDesc[1]) * multiplier; // use this for the column

        boolean added = false;
        for(int i = 0; i < finalArr.size(); i++){
          if(finalArr.get(i).get(0).equals(parseDesc[0])){
            added = true;
            //change to new update number
            double newNum = Double.parseDouble(finalArr.get(i).get(1)) + invUsed;
            finalArr.get(i).set(1,Double.toString(newNum));
          }
        }
        if(!added){
          ArrayList<String> k = new ArrayList<>();
          k.add(parseDesc[0]);
          k.add(Double.toString(invUsed));
          finalArr.add(k);
        }
//        print(parseDesc[0] + ": " + invUsed + "    |    multiplier: " + multiplier);
        // parseDesc[0] will match the description of the inventory item in a query,
        // e.g. "UPDATE inventory SET 'blah=" + invUsed + "' WHERE description='" + parseDesc[0] + "';"
      }
    }

    finalArr = sortInventoryPopularity(finalArr);
    for(ArrayList<String> k : finalArr){
      print(k.toString());
    }
    return finalArr;
  }

  public static ArrayList<ArrayList<String>> sortInventoryPopularity(ArrayList<ArrayList<String>> list) {
    ArrayList<ArrayList<String>> sortedList = new ArrayList<>();

    while(list.size() > 0) {
      ArrayList<String> row = new ArrayList<>();
      double max = Double.parseDouble(list.get(0).get(1));
      int maxIndex = 0;
      for(int i = 0; i < list.size(); i++) {
        if(Double.parseDouble(list.get(i).get(1)) > max) {
          max = Double.parseDouble(list.get(i).get(1));
          maxIndex = i;
        }
      }
      row.add(list.get(maxIndex).get(0));
      row.add(list.get(maxIndex).get(1));
      list.remove(maxIndex);
      sortedList.add(row);
    }
    print(sortedList);
    return sortedList;
  }

  // gets Daily Total Orders from the database
  public static ArrayList<ArrayList<String>> getDBDTO () {
    try {
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM weeksales;");
      ArrayList<ArrayList<String>> result = new ArrayList<>();
      //print("OPENED: " + rs.next());
      while (rs.next()) {
        ArrayList<String> row = new ArrayList<>();
        row.add(rs.getString("item"));
        row.add(rs.getString("quantity"));
        row.add(rs.getString("dateofpurchase"));
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
  public static ArrayList < ArrayList < String >> getDBMenuItems() {
    try {
      Statement statement = conn.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM menu_key;");
      ArrayList < ArrayList < String >> result = new ArrayList < > ();
      //print("OPENED: " + rs.next());
      while (rs.next()) {
        ArrayList < String > row = new ArrayList < > ();
        row.add(rs.getString("item"));
        row.add(rs.getString("name"));
        row.add(rs.getString("description"));
        row.add(rs.getString("price"));
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

  // get Daily Order Trends from ranges selected in table 1 and table 2
  public static ArrayList<ArrayList<Object>> getOrderTrends() {

    // this will hold our menu items. (Item, Quantity, Price, Revenue, Trend %). Two arrays because we have two date ranges.
    ArrayList<String> itemNames = new ArrayList<>();
    int numItems = manager.menuItemsTableModel.getRowCount();
    ArrayList<ArrayList<Object>> trendTableList = new ArrayList<>();
    for (int i = 0; i < numItems; i++) {
      ArrayList<Object> tempList = new ArrayList<>(7);
      tempList.add("");
      tempList.add("");
      tempList.add("");
      tempList.add("");
      tempList.add("");
      tempList.add("");
      tempList.add("");
      trendTableList.add(tempList);
    }



    // table 1 and 2 need to be populated first
    if (manager.DTOTable1.getRowCount() > 1 && manager.DTOTable2.getRowCount() > 1) {
      JTable table1 = manager.DTOTable1;
      JTable table2 = manager.DTOTable2;

      // fill trend list with the appropriate data from list 1
      for (int i = 0; i < table1.getRowCount(); i++) {

        // get item name minus day of week
        String itemName = (String) table1.getValueAt(i, 0);
        itemName = itemName.substring(itemName.indexOf("_") + 1);

        // get quantity
        String quantity = (String) table1.getValueAt(i, 1);

        // check the items list and see if any new menu items were added from this row. If so, add to the trendTableList
        if (!itemNames.contains(itemName)) {
          itemNames.add(itemName);

          // add new item price from menuItems
          int index = Integer.parseInt(itemName.substring(1)) - 1;
          String price = (String) manager.menuItemsTableModel.getValueAt(index, 3);

          double revenue1 = Double.parseDouble(price.substring(1)) * Integer.parseInt(quantity);

          ArrayList<Object> newRow = new ArrayList<>(7);
          newRow.add(itemName);
          newRow.add(quantity);
          newRow.add("0");
          newRow.add(price);
          newRow.add("$" + revenue1);
          newRow.add("$0");
          newRow.add("0");
          trendTableList.set(index, newRow);

        } else {
          // index is whatever the name is without the 5 and leading 0's, - 1 (503 -> 2)
          int index = Integer.parseInt(itemName.substring(1)) - 1;
          print("SIZE: " + trendTableList.size());
          print("INDEX: " + index);
          ArrayList<Object> currentRow = trendTableList.get(index);

          // get current row quantity and add new quantity (basically | row[1] = row[1] + quantity | in String version)
          int currentRowQuantity = Integer.parseInt((String) currentRow.get(1));
          int rowQuantityToAdd = Integer.parseInt(quantity);
          int newQuantity = currentRowQuantity + rowQuantityToAdd;
          currentRow.set(1, Integer.toString(currentRowQuantity + rowQuantityToAdd));

          // update revenue
          double price = Double.parseDouble(((String) currentRow.get(3)).substring(1));
          double revenue1 = price * newQuantity;
          currentRow.set(4, "$" + revenue1);
          trendTableList.set(index, currentRow);
        }


      }

      // fill trend list with the appropriate data from list 2
      for (int i = 0; i < table2.getRowCount(); i++) {

        // get item name minus day of week
        String itemName = (String) table2.getValueAt(i, 0);
        itemName = itemName.substring(itemName.indexOf("_") + 1);

        // get quantity
        String quantity = (String) table2.getValueAt(i, 1);

        // check the items list and see if any new menu items were added from this row. If so, add to the trendTableList
        if (!itemNames.contains(itemName)) {
          itemNames.add(itemName);
          // add new item price from menuItems
          int index = Integer.parseInt(itemName.substring(1)) - 1;
          String price = (String) manager.menuItemsTableModel.getValueAt(index, 3);

          ArrayList<Object> newRow = new ArrayList<>(7);
          newRow.add(itemName);
          newRow.add("0");
          newRow.add(quantity);
          newRow.add(price);
          newRow.add("0");
          newRow.add("0");
          newRow.add("0");
          trendTableList.set(index, newRow);
        } else {
          // index is whatever the name is without the 5 and leading 0's, - 1 (503 -> 2)
          int index = Integer.parseInt(itemName.substring(1)) - 1;

          ArrayList<Object> currentRow = trendTableList.get(index);

          // get current row quantity and add new quantity (basically | row[2] = row[2] + quantity | in String version)
          int currentRowQuantity = Integer.parseInt((String) currentRow.get(2));
          int rowQuantityToAdd = Integer.parseInt(quantity);
          int newQuantity = currentRowQuantity + rowQuantityToAdd;
//          print(index + " " + itemName + " " + newQuantity);
          currentRow.set(2, Integer.toString(newQuantity));

          // update revenue
          double price = Double.parseDouble(((String) currentRow.get(3)).substring(1));
          double revenue2 = price * newQuantity;
          currentRow.set(5, "$" + revenue2);
          trendTableList.set(index, currentRow);
        }


      }

      // set Total Revenue
      double totalRevenue1 = 0;
      double totalRevenue2 = 0;
      for (int i = 0; i < numItems; i++) {
        totalRevenue1 += Double.parseDouble(((String) trendTableList.get(i).get(4)).substring(1));
      }
      for (int i = 0; i < numItems; i++) {
        totalRevenue2 += Double.parseDouble(((String) trendTableList.get(i).get(5)).substring(1));
      }
      manager.revenue1TextBox.setText("$" + totalRevenue1);
      manager.revenue2TextBox.setText("$" + totalRevenue2);

      // set Trend %
      double trendPercent;
      double revenue1;
      double revenue2;
      for (int i = 0; i < numItems; i++) {
        revenue1 = Double.parseDouble(((String) trendTableList.get(i).get(4)).substring(1));
        revenue2 = Double.parseDouble(((String) trendTableList.get(i).get(5)).substring(1));

        double revenuePercent1 = revenue1 / totalRevenue1;
        double revenuePercent2 = revenue2 / totalRevenue2;

        trendPercent = revenuePercent2 - revenuePercent1;
        trendPercent *= 100; // change from decimal to percentage
        ArrayList<Object> tempRow = trendTableList.get(i);
        Double trendPercentFormatted = trendPercent;
        tempRow.set(6, trendPercentFormatted);
        trendTableList.set(i, tempRow);
      }

    }

    return trendTableList;
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

  // refresh tables in manager GUI
  public static void refreshTablesFromDB() {
    ArrayList<ArrayList<String>> inventoryDB = getDBInventory();
    ArrayList<ArrayList<String>> DTODB = getDBDTO();
    ArrayList<ArrayList<String>> menuItemsDB = getDBMenuItems();

    manager.clearTables();

    assert inventoryDB != null;
    for (ArrayList<String> row : inventoryDB) {
      manager.addRowToInventoryTable(row.toArray());
    }
    assert DTODB != null;
    for (ArrayList<String> row : DTODB) {
      manager.addRowToDTOTable(row.toArray());
    }
    assert menuItemsDB != null;
    for (ArrayList<String> row : menuItemsDB) {
      manager.addRowTomMenuItemsTable(row.toArray());
    }
    refreshOrderPopularity();

  }

  public static void refreshOrderPopularity() {
    manager.clearTable(manager.orderPopTableModel);
    ArrayList<ArrayList<Object>> orderPopularityDB = getOrderPopularity();
    for (ArrayList<Object> row : orderPopularityDB) {
      manager.orderPopTableModel.addRow(row.toArray());
    }
  }

  // refreshes the table model given with the date range given (Inclusive)
  public static void refreshDTOTableFromRange(String dateA, String dateB, DefaultTableModel tableModel) throws SQLException {
    Statement stmt = conn.createStatement();
    String sqlStatement = "SELECT * FROM weeksales WHERE dateofpurchase >= '" + dateA + "' AND dateofpurchase <= '" + dateB + "';";
    ResultSet rs = stmt.executeQuery(sqlStatement);
    print(sqlStatement);
    // SELECT * FROM weeksales WHERE dateofpurchase >= '2022-01-30' AND dateofpurchase <= '2022-02-01';

    ArrayList<ArrayList<String>> result = new ArrayList<>();
    log("Got data from " + dateA + " to " + dateB);
    while (rs.next()) {
      ArrayList<String> row = new ArrayList<>();
      row.add(rs.getString("item"));
      row.add(rs.getString("quantity"));
      row.add(rs.getString("dateofpurchase"));
      result.add(row);
    }
    manager.clearTable(tableModel);

    for (ArrayList<String> row : result) {
      tableModel.addRow(row.toArray());

    }
  }

  // refreshes the inv table model given with the date range given (Inclusive)
  public static void refreshInvTableFromRange(String dateA, String dateB, DefaultTableModel tableModel) throws SQLException {

    ArrayList<ArrayList<String>> result = getItemConversionsFromDateRange(dateA, dateB);
    manager.clearTable(tableModel);

    for (ArrayList<String> row : result) {
      tableModel.addRow(row.toArray());
    }
  }



   /*public static void updateTrends()
  {
    ArrayList<ArrayList<String>> idWithPricesFromMenuKey = getIdWithPricesFromMenuKey(); //gets item,price,name from db

    //sets everything to 0
    for(int i = 0; i < idWithPricesFromMenuKey.size(); i++){
      ArrayList<String> row = new ArrayList<String>();
      manager.DTOTableModel3.addRow(row.toArray());
      manager.DTOTableModel3.setValueAt(idWithPricesFromMenuKey.get(i).get(0).toString(),i,0);
      manager.DTOTableModel3.setValueAt("0",i,2);
      manager.DTOTableModel3.setValueAt("0",i,1);
    }

    //find total revenue of range 1
    //also stores total amount of revenue for each item in last column of trend result
    double totalRevenue1 = 0.0;
    for(int i = 0; i < manager.DTOTable1.getRowCount(); i++)
    {
      System.out.println(manager.DTOTable1.getValueAt(i,0)+"  "+manager.DTOTable1.getValueAt(i,1)+"  "+manager.DTOTable1.getValueAt(i,2)+"  ");
    }

    for(int i = 0; i < manager.DTOTable1.getRowCount(); i++) {
      String itemID = manager.DTOTable1.getValueAt(i,0).toString().substring(manager.DTOTable1.getValueAt(i,0).toString().indexOf("_")+1);
      for(int j = 0; j < idWithPricesFromMenuKey.size(); j++) {
        if(itemID.equals(idWithPricesFromMenuKey.get(j).get(0))) {
          double price = Double.parseDouble(idWithPricesFromMenuKey.get(j).get(1));
          int quantity = Integer.parseInt(manager.DTOTable1.getValueAt(i,1).toString());
          double totalForThisItem = price * quantity;

          double currTotalForThisItem = Double.parseDouble(manager.DTOTableModel3.getValueAt(j,2).toString());
          double newTotalForThisItem = currTotalForThisItem + totalForThisItem;
          manager.DTOTableModel3.setValueAt(String.valueOf(newTotalForThisItem),j,2);
          totalRevenue1 += (totalForThisItem);
        }
      }
    }

    //finds revenue for second range
    //also stores total amount of revenue for each item in middle column of trend result
    double totalRevenue2 = 0.0;
    for(int i = 0; i < manager.DTOTable2.getRowCount(); i++)
    {
      System.out.println(manager.DTOTable2.getValueAt(i,0)+"  "+manager.DTOTable2.getValueAt(i,1)+"  "+manager.DTOTable2.getValueAt(i,2)+"  ");
    }

    for(int i = 0; i < manager.DTOTable2.getRowCount(); i++) {
      String itemID = manager.DTOTable2.getValueAt(i,0).toString().substring(manager.DTOTable2.getValueAt(i,0).toString().indexOf("_")+1);
      for(int j = 0; j < idWithPricesFromMenuKey.size(); j++) {
        if(itemID.equals(idWithPricesFromMenuKey.get(j).get(0))) {
          double price = Double.parseDouble(idWithPricesFromMenuKey.get(j).get(1));
          int quantity = Integer.parseInt(manager.DTOTable2.getValueAt(i,1).toString());
          double totalForThisItem = price * quantity;

          double currTotalForThisItem = Double.parseDouble(manager.DTOTableModel3.getValueAt(j,1).toString());
          double newTotalForThisItem = currTotalForThisItem + totalForThisItem;
          manager.DTOTableModel3.setValueAt(String.valueOf(newTotalForThisItem),j,1);
          totalRevenue2 += (totalForThisItem);
        }
      }
    }

    System.out.println("total rev: " + totalRevenue1);

    System.out.println("total rev: " + totalRevenue2);

    //converts middle(range 2) and last(range 1) column of trend result to that items percentage of total revenue
    for(int i = 0; i < manager.DTOTableModel3.getRowCount(); i++) {
      double percentage = Double.parseDouble(manager.DTOTableModel3.getValueAt(i,2).toString()) / totalRevenue1 * 100;
      String inputPercentage = String.valueOf(percentage);
      manager.DTOTableModel3.setValueAt(inputPercentage,i,2);
    }

    for(int i = 0; i < manager.DTOTableModel3.getRowCount(); i++) {
      double percentage = Double.parseDouble(manager.DTOTableModel3.getValueAt(i,1).toString()) / totalRevenue2 * 100;
      String inputPercentage = String.valueOf(percentage);
      manager.DTOTableModel3.setValueAt(inputPercentage,i,1);
    }

    //finds difference by subtracting last(range 1) column from middle(range 2) column and stores in last column in trend result
    ArrayList<ArrayList<Double>> list = new ArrayList<ArrayList<Double>>();
    for(int i = 0; i < manager.DTOTableModel3.getRowCount(); i++) {
      double rangeOnePercentage = Double.parseDouble(manager.DTOTableModel3.getValueAt(i,2).toString());
      double rangeTwoPercentage = Double.parseDouble(manager.DTOTableModel3.getValueAt(i,1).toString());
      double trendPercentage = rangeTwoPercentage - rangeOnePercentage;
      ArrayList<Double> row = new ArrayList<Double>();
      row.add(Double.parseDouble(manager.DTOTableModel3.getValueAt(i,0).toString()));
      row.add(trendPercentage);
      list.add(row);
    }


    //sorts the percentages in descending order
    ArrayList<ArrayList<Double>> sortedList = new ArrayList<ArrayList<Double>>();
    while(list.size() > 0) {
      ArrayList<Double> row = new ArrayList<Double>();
      double min = list.get(0).get(1);
      int minIndex = 0;
      for(int i = 0; i < list.size(); i++) {
        if(list.get(i).get(1) < min) {
          min = list.get(i).get(1);
          minIndex = i;
        }
      }
      row.add(list.get(minIndex).get(0));
      row.add(list.get(minIndex).get(1));
      list.remove(minIndex);
      sortedList.add(row);
    }

    //reverses list to put in ascending
    Collections.reverse(sortedList);

    //rewrites first and last column using sorted list
    //rewrites middle column to be name of the item
    for(int i = 0; i < manager.DTOTableModel3.getRowCount(); i++) {
      manager.DTOTableModel3.setValueAt(String.valueOf(sortedList.get(i).get(0).intValue()),i,0);
      manager.DTOTableModel3.setValueAt(String.valueOf(sortedList.get(i).get(1)),i,2);
      for(int j = 0; j < idWithPricesFromMenuKey.size(); j++) {
        if(manager.DTOTableModel3.getValueAt(i,0).toString().equals(idWithPricesFromMenuKey.get(j).get(0))) {
          manager.DTOTableModel3.setValueAt(idWithPricesFromMenuKey.get(j).get(2),i,1);
        }
      }
    }
  }*/

  public static ArrayList<ArrayList<String>> getIdWithPricesFromMenuKey() {
    ResultSet rs1;
    ArrayList<ArrayList<String>> idWithPricesFromMenuKey = new ArrayList<>();
    try {
      Statement stmt = conn.createStatement();
      rs1 = stmt.executeQuery("SELECT item,price,name FROM menu_key;");
      while(rs1.next()) {
        ArrayList<String> row = new ArrayList<>();
        row.add(rs1.getString("item"));
        row.add(rs1.getString("price").substring(1));
        row.add(rs1.getString("name"));
        idWithPricesFromMenuKey.add(row);
      }
    }catch (SQLException ex) {
      ex.printStackTrace();
    }
    return idWithPricesFromMenuKey;
  }

  public static ArrayList<ArrayList<Object>> getOrderPopularity(){
    ArrayList<ArrayList<String>> idWithPricesMenuKey = getIdWithPricesFromMenuKey();
    System.out.println(idWithPricesMenuKey.size());

    ArrayList<ArrayList<Object>> list = new ArrayList<>();
    for (ArrayList<String> strings : idWithPricesMenuKey) {
      ArrayList<Object> row = new ArrayList<>();
      row.add(strings.get(0));
      row.add(0);
      list.add(row);
    }

    for(int i = 0; i < manager.DTOTable1.getRowCount(); i++) {
      int indexAfterUnderscore = manager.DTOTable1.getValueAt(i,0).toString().indexOf('_');
      String itemID = manager.DTOTable1.getValueAt(i,0).toString().substring(indexAfterUnderscore+1);
      for (ArrayList<Object> objects : list) {
        if (itemID.equals(objects.get(0))) {
          int currQuantity = Integer.parseInt(objects.get(1).toString());
          int addQuantity = Integer.parseInt(manager.DTOTable1.getValueAt(i, 1).toString());
          int newQuantity = currQuantity + addQuantity;
          objects.set(1, newQuantity);
        }
      }
    }
    return list;

  }

  // following functions set up the event listeners for buttons and tables in manager GUI
  public static void setupManagerEventListeners() {
    setupInventoryEventListeners();
    setupDTOEventListeners();
    setupMenuItemsListeners();
  }
  public static void setupInventoryEventListeners() {
    // DELETE currently selected row
    manager.invDeleteRowButton.addActionListener(ae -> {
      try {
        Statement statement = conn.createStatement();
        String currentSKU = (String) manager.invEditTableModel.getValueAt(0, 1);
//          print(currentSKU);
        String sqlStatement = "DELETE FROM inventory WHERE sku='" + currentSKU + "';";
//          print(sqlStatement);
        statement.executeUpdate(sqlStatement);
//          print("Delete result: " + rs);
        refreshTablesFromDB();
      }catch(SQLException e){
        e.printStackTrace();
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
    manager.invAddRowButton.addActionListener(ae -> {
      ArrayList<String> row = new ArrayList<>(manager.invEditTableModel.getColumnCount());
      for (int j = 0; j < manager.invEditTableModel.getColumnCount(); j++) {
        row.add((String) manager.invEditTableModel.getValueAt(0, j));
      }

      String sqlStatement =
              "INSERT INTO inventory (description, sku, quantity, fillamt, delivered, sold_by, delivered_by, quantity_multiplyer, _price_, _extended_, category, invoice_line, detailed_description) " +
                      "VALUES ('" + row.get(0) + "', '" + row.get(1) + "', " + row.get(2) + ", " + row.get(3) + ", " + row.get(4) + ", '" + row.get(5) + "', '" + row.get(6) + "', " + row.get(7) + ", '" + row.get(8) + "', '" + row.get(9) + "', '" + row.get(10) + "', " + row.get(11) + ", '" + row.get(12) + "');";

      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sqlStatement);
//          print("Result Add Row: " + rs);
        refreshTablesFromDB();
      } catch (SQLException e) {
        e.printStackTrace();
      }

    });

    // EDIT currently selected row button
    manager.invEditRowButton.addActionListener(ae -> {
      ArrayList<String> row = new ArrayList<>(manager.invEditTableModel.getColumnCount());
      for (int j = 0; j < manager.invEditTableModel.getColumnCount(); j++) {
        row.add((String) manager.invEditTableModel.getValueAt(0, j));
      }

      // update database, then refresh page
      Statement statement;
      try {
        statement = conn.createStatement();

        // build sql updateQuery
        String sqlStatement =
                "UPDATE inventory " +
                        "SET " +
                        "description = '" + row.get(0) +
                        "', sku = '" + row.get(1) +
                        "', quantity = " + row.get(2) +
                        ", fillamt = " + row.get(3) +
                        ", delivered = " + row.get(4) +
                        ", sold_by = '" + row.get(5) +
                        "', delivered_by = '" + row.get(6) +
                        "', quantity_multiplyer = " + row.get(7) +
                        ", _price_ = '" + row.get(8) +
                        "', _extended_ = '" + row.get(9) +
                        "', category = '" + row.get(10) +
                        "', invoice_line = " + row.get(11) +
                        ", detailed_description = '" + row.get(12) + "' " +
                        "WHERE sku = '" + row.get(1) + "';";

//          print(sqlStatement);
        statement.executeUpdate(sqlStatement);
//          print("Update result: " + rs);
        refreshTablesFromDB();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // DATE RANGE REFRESH -- This won't work until our inventory can track ingredient usage by date
    manager.updateInventoryButton.addActionListener(e -> {
      String dateA = manager.invUpdate_From_YYYY_Box.getSelectedItem() + "-" + manager.invUpdate_From_MM_Box.getSelectedItem() + "-" + manager.invUpdate_From_DD_Box.getSelectedItem();
      String dateB = manager.invUpdate_To_YYYY_Box.getSelectedItem() + "-" + manager.invUpdate_To_MM_Box.getSelectedItem() + "-" + manager.invUpdate_To_DD_Box.getSelectedItem();
      log("Selecting date range from " + dateA + " to " + dateB);
      print("Selecting date range from " + dateA + " to " + dateB);
      try {
        updateInventoryGivenRange(dateA, dateB);
        refreshTablesFromDB();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // REFRESH button
    manager.invRefreshButton.addActionListener(ae -> refreshTablesFromDB());
  }
  public static void setupDTOEventListeners() {
    // DELETE currently selected row
    manager.DTODeleteRowButton.addActionListener(ae -> {
      try {
        Statement statement = conn.createStatement();
        String currentItem = (String) manager.DTOEditTableModel.getValueAt(0, 0);
//        print(currentItem);
        String sqlStatement = "DELETE FROM weeksales WHERE item='" + currentItem + "';";
//        print(sqlStatement);
        statement.executeUpdate(sqlStatement);
//        print("Delete result: " + rs);
        refreshTablesFromDB();
      }catch(SQLException e){
        e.printStackTrace();
      }
    });

    // UPDATE currently selected row view
    manager.DTOTable1.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        // don't run if table empty
        if (manager.DTOTableModel1.getRowCount() > 1) {
          JTable target = (JTable)e.getSource();
          int row = target.getSelectedRow();

          for (int i = 0; i < manager.DTOEditTableModel.getColumnCount(); i++) {
            manager.DTOEditTable.setValueAt(manager.DTOTable1.getValueAt(row, i), 0, i);
          }
        }



      }
    });

    // ADD new row from editTable button
    manager.DTOAddRowButton.addActionListener(ae -> {
      ArrayList<String> row = new ArrayList<>(manager.DTOEditTableModel.getColumnCount());
      for (int j = 0; j < manager.DTOEditTableModel.getColumnCount(); j++) {
        row.add((String) manager.DTOEditTableModel.getValueAt(0, j));
      }

        String sqlStatement =
                "INSERT INTO weeksales (item, quantity, total, dateofpurchase) " +
                        "VALUES ('" + row.get(0) + "', " + row.get(1) + ", 0, '" + row.get(2) + "');";

//        print(sqlStatement);

      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sqlStatement);
//          print("Result Add Row: " + rs);
        refreshTablesFromDB();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    // EDIT currently selected row button
    manager.DTOEditRowButton.addActionListener(ae -> {
      ArrayList<String> row = new ArrayList<>(manager.DTOEditTableModel.getColumnCount());
      for (int j = 0; j < manager.DTOEditTableModel.getColumnCount(); j++) {
        row.add((String) manager.DTOEditTableModel.getValueAt(0, j));
      }

      // update database, then refresh page
      Statement statement;
      try {
        statement = conn.createStatement();

        // build sql updateQuery
        String sqlStatement =
                "UPDATE weeksales " +
                        "SET " +
                        "item = '" + row.get(0) +
                        "', quantity = " + row.get(1) + ", total = 0 " +
                        "WHERE item = '" + row.get(0) + "';";

//          print(sqlStatement);
        statement.executeUpdate(sqlStatement);
//          print("Update result: " + rs);
        refreshTablesFromDB();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // REFRESH button
    manager.DTORefreshButton.addActionListener(ae -> refreshTablesFromDB());

    // RANGE 1 REFRESH
    manager.DTORefreshRange1Button.addActionListener(e -> {
      String dateA = manager.DTO_R1From_YYYY_Box.getSelectedItem() + "-" + manager.DTO_R1From_MM_Box.getSelectedItem() + "-" + manager.DTO_R1From_DD_Box.getSelectedItem();
      String dateB = manager.DTO_R1To_YYYY_Box.getSelectedItem() + "-" + manager.DTO_R1To_MM_Box.getSelectedItem() + "-" + manager.DTO_R1To_DD_Box.getSelectedItem();
      print(dateA);
      print(dateB);
      try {
        refreshDTOTableFromRange(dateA, dateB, manager.DTOTableModel1);
        refreshOrderPopularity();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // RANGE 2 REFRESH
    manager.DTORefreshRange2Button.addActionListener(e -> {
      String dateA = manager.DTO_R2From_YYYY_Box.getSelectedItem() + "-" + manager.DTO_R2From_MM_Box.getSelectedItem() + "-" + manager.DTO_R2From_DD_Box.getSelectedItem();
      String dateB = manager.DTO_R2To_YYYY_Box.getSelectedItem() + "-" + manager.DTO_R2To_MM_Box.getSelectedItem() + "-" + manager.DTO_R2To_DD_Box.getSelectedItem();
      print(dateA);
      print(dateB);
      try {
        refreshDTOTableFromRange(dateA, dateB, manager.DTOTableModel2);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // TOGGLE TREND table visibility
    manager.enableTrendCheckBox.addItemListener(e -> {
      boolean isSelected = manager.enableTrendCheckBox.isSelected();
      manager.DTOTable3Panel.setVisible(isSelected);
      log("Toggled Trends");
      if (isSelected) {
        print("Toggled!");

        // clear trend table then populate it using getOrderTrends
        manager.DTOTableModel3.setRowCount(0);
        ArrayList<ArrayList<Object>> trends = getOrderTrends();
        for (ArrayList<Object> row : trends) {
          manager.DTOTableModel3.addRow(row.toArray());
        }

      }

    });

    // UPDATE INVENTORY button from date range

    manager.updateInventoryButton.addActionListener(e -> {
      String dateA = manager.invUpdate_From_YYYY_Box.getSelectedItem() + "-" + manager.invUpdate_From_MM_Box.getSelectedItem() + "-" + manager.invUpdate_From_DD_Box.getSelectedItem();
      String dateB = manager.invUpdate_To_YYYY_Box.getSelectedItem() + "-" + manager.invUpdate_To_MM_Box.getSelectedItem() + "-" + manager.invUpdate_To_DD_Box.getSelectedItem();
      log("Updating inventory with orders from " + dateA + " to " + dateB);
      // updateInventoryGivenDate(dateA);
    });
  }
  public static void setupMenuItemsListeners() {
    // MENU ITEMS -------- ---------------------------------------------------------------------------------------
    // DELETE currently selected row
    manager.menuItemsDeleteRowButton.addActionListener(ae -> {
      try {
        Statement statement = conn.createStatement();
        String currentItem = (String) manager.menuItemsEditTableModel.getValueAt(0, 0);
//        print(currentItem);
        String sqlStatement = "DELETE FROM menu_key WHERE item = " + currentItem + ";";
//        print(sqlStatement);
        statement.executeUpdate(sqlStatement);
//        print("Delete result: " + rs);
        refreshTablesFromDB();
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
    manager.menuItemsAddRowButton.addActionListener(ae -> {
      ArrayList<String> row = new ArrayList<>(manager.menuItemsEditTableModel.getColumnCount());
      for (int j = 0; j < manager.menuItemsEditTableModel.getColumnCount(); j++) {
        row.add((String) manager.menuItemsEditTableModel.getValueAt(0, j));
      }

      String sqlStatement =
              "INSERT INTO menu_key (item, name, description, price) " +
                      "VALUES (" + row.get(0) + ", '" + row.get(1) + "', '" + row.get(2) + "', '" + row.get(3) + "');";

//        print(sqlStatement);

      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sqlStatement);
//          print("Result Add Row: " + rs);
        refreshTablesFromDB();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });

    // EDIT currently selected row button
    manager.menuItemsEditRowButton.addActionListener(ae -> {
      ArrayList<String> row = new ArrayList<>(manager.menuItemsEditTableModel.getColumnCount());
      for (int j = 0; j < manager.menuItemsEditTableModel.getColumnCount(); j++) {
        row.add((String) manager.menuItemsEditTableModel.getValueAt(0, j));
      }

      Statement statement;
      try {
        statement = conn.createStatement();
        String sqlStatement =
                "UPDATE menu_key " +
                        "SET " +
                        "item = " + row.get(0) +
                        ", name = '" + row.get(1) +
                        "', description = '" + row.get(2) +
                        "', price = '" + row.get(3) + "' " +
                        "WHERE item = " + row.get(0) + ";";

//          print(sqlStatement);
        statement.executeUpdate(sqlStatement);
//          print("Update result: " + rs);
        refreshTablesFromDB();
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    });

    // REFRESH button
    manager.menuItemsRefreshButton.addActionListener(ae -> refreshTablesFromDB());
  }
  // _____________________________________________________________________________________

  // print helper function
  public static void print(Object out) {
    System.out.println(out);
  }

  public static void log(Object out) {
    manager.logTextField.setText((String) out);
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

/* psuedo-psuedo code for item conversions --> THIS CODE HAS NOT BEEN TESTED, BUT LOGIC IS THERE <--
    //populate array of item conversions

    Statement stmt = jdbcpostgreSQL.conn.createStatement();
    String converArr[] = new String[19];


    ResultSet result = stmt.executeQuery("SELECT * FROM itemconversion;");
    while(result.next()){
        //first value is the # of that item used, then its description
        converArr[result.getInt("item")-501] = "0;" + result.getString(description);
    }

    //grab resultset for weeksales, grab total # of each item used in timeframe

    result = stmt.executeQuery("SELECT * FROM weeksales WHERE date > a AND date < b;");
    while(result.next()){
        String item = result.getString("item");
        //splits the description into an array, recombine later
        String parseArr[] = converArr[Integer.parseInt(item.substring(item.length()-3)) - 501].split(";");
        //the # of used updated
        parseArr[0] = Integer.parseInt(parseArr[0]) + result.getInt("Quantity");

        //put string back together
        converArr[Integer.parseInt(item.substring(item.length()-3)) - 501] = String.join(";",parseArr);
    }

    //now converArr has: AMOUNT;.....restofdescription..... on each index
    //final part
    for(String convItem : converArr){
        String parseArr[] = convItem.split(";");
        int multiplier = Integer.parseInt(parseArr[0]);

        //might need an extra column in inventory for 'used'
            //or some temporary column in the manager gui for that date range


        //iterate the new parsed array from 2nd index onwards
        //each is 'description=amount'
        for(String desc : parseArr[].subList(1)){
            parseDesc = desc.split("=");
            double invUsed = parseDesc[1] * multiplier; //use this for the column
            //parseDesc[0] will match the description of the inventory item in a query,
                //e.g. "UPDATE inventory SET 'blah=" + invUsed + "' WHERE description='" + parseDesc[0] + "';"
        }

    }
*/