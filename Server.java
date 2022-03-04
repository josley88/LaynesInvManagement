import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.*;

public class Server implements ActionListener{

    private JPanel main;
    private JPanel leftPane;
    private JButton fiveFinger;
    private JButton fourFinger;
    private JButton threeFingerMealButton;
    private JButton clubSandwichMealButton;
    private JButton kidsMealButton;
    private JButton sandwichMealComboButton;
    private JButton chickenFingerButton;
    private JButton fountainDrinkButton;
    private JButton minus;
    private JButton plus;
    private JButton bottleDrinkButton;
    private JButton gallonOfTeaButton;
    private JButton layneSSauceButton;
    private JButton texasToastButton;
    private JButton potatoSaladButton;
    private JButton crinkleCutFriesButton;
    private JButton familyPackButton;
    private JButton grillCheeseSandwichOnlyButton;
    private JButton sandwichOnlyButton;
    private JButton clubSandwichOnlyButton;
    private JButton grillCheeseMealComboButton;
    private JTable ticket;
    private JTextPane textPane1;
    private JButton finalizeOrderButton;
    private JScrollPane ticketScroll;
    private JButton extraButton1;
    private JButton extraButton2;
    private JButton extraButton3;
    private JButton extraButton4;
    private JButton refreshMenuButton;
    private DefaultTableModel serverTableModel;
    final private String serverTicket[] = {"Item", "Amount", "Price", "ID"};
    private ArrayList<JButton> buttonList = new ArrayList<JButton>();
    private double totalPrice = 0;
    private boolean plusMode = true; //true means in plus mode, false means in minus mode. default to plus mode
    LocalDate dt = LocalDate.now();
    String currDay = dt.getDayOfWeek().toString().charAt(0) + dt.getDayOfWeek().toString().substring(1).toLowerCase();


    public Server(){
        serverTableModel = new DefaultTableModel(serverTicket, 0);
        serverTableModel.setColumnIdentifiers(serverTicket);
        ticketScroll.setColumnHeaderView(ticket.getTableHeader());
        ticket.setModel(serverTableModel);
        buttonSetup();
        textPane1.setEditable(false);
        textPane1.setText("Total: $0.00");
        System.out.println(currDay);
        for(JButton b : buttonList){
            b.addActionListener(this);
        }
    }
    public void buttonSetup(){
        buttonList.add(fiveFinger);
        fiveFinger.setName("501");
        buttonList.add(fourFinger);
        fourFinger.setName("502");
        buttonList.add(threeFingerMealButton);
        threeFingerMealButton.setName("503");
        buttonList.add(kidsMealButton);
        kidsMealButton.setName("504");
        buttonList.add(gallonOfTeaButton);
        gallonOfTeaButton.setName("505");
        buttonList.add(familyPackButton);
        familyPackButton.setName("506");
        buttonList.add(clubSandwichMealButton);
        clubSandwichMealButton.setName("507");
        buttonList.add(clubSandwichOnlyButton);
        clubSandwichOnlyButton.setName("508");
        buttonList.add(sandwichMealComboButton);
        sandwichMealComboButton.setName("509");
        buttonList.add(sandwichOnlyButton);
        sandwichOnlyButton.setName("510");
        buttonList.add(grillCheeseMealComboButton);
        grillCheeseMealComboButton.setName("511");
        buttonList.add(grillCheeseSandwichOnlyButton);
        grillCheeseSandwichOnlyButton.setName("512");
        buttonList.add(layneSSauceButton);
        layneSSauceButton.setName("513");
        buttonList.add(chickenFingerButton);
        chickenFingerButton.setName("514");
        buttonList.add(texasToastButton);
        texasToastButton.setName("515");
        buttonList.add(potatoSaladButton);
        potatoSaladButton.setName("516");
        buttonList.add(crinkleCutFriesButton);
        crinkleCutFriesButton.setName("517");
        buttonList.add(fountainDrinkButton);
        fountainDrinkButton.setName("518");
        buttonList.add(bottleDrinkButton);
        bottleDrinkButton.setName("519");
        buttonList.add(minus);
        minus.setName("-");
        buttonList.add(plus);
        plus.setName("+");
        buttonList.add(finalizeOrderButton);
        finalizeOrderButton.setName("finalize");
        buttonList.add(refreshMenuButton);
        refreshMenuButton.setName("refresh");
        extraButton1.setName("520");
        extraButton2.setName("521");
        extraButton3.setName("522");
        extraButton4.setName("523");
        extraButton1.setVisible(false);
        extraButton2.setVisible(false);
        extraButton3.setVisible(false);
        extraButton4.setVisible(false);
       // System.out.println("20th index: " + buttonList.get(20).getName());
        buttonList.add(extraButton1);
        buttonList.add(extraButton2);
        buttonList.add(extraButton3);
        buttonList.add(extraButton4);
        refreshMenu();

    }

    public int alreadyInTicket(String item) {
        for(int i = 0; i < serverTableModel.getRowCount(); i++){
            if(((String)serverTableModel.getDataVector().get(i).get(0)).equals(item))
                return i; //if in ticket return index where
        }
        return -1; //return -1 if not in ticket
    }

    public void updatePrice(String itemPrice) {
        String priceNoSign = itemPrice;
        if(plusMode) {
            if(itemPrice.charAt(0) == '$')
                priceNoSign = itemPrice.substring(1);
            double doublePrice = Double.parseDouble(priceNoSign);
            totalPrice += doublePrice;
        }else {
            if(itemPrice.charAt(0) == '$')
                priceNoSign = itemPrice.substring(1);
            double doublePrice = Double.parseDouble(priceNoSign);
            totalPrice -= doublePrice;
        }

        if(totalPrice < 0.00)
            totalPrice = 0.0;
        String tempPrice = String.valueOf(String.format("%.2f",totalPrice));
        textPane1.setText("Total: $" + tempPrice);

    }
    public void clearServerTable() {
        while(serverTableModel.getRowCount() != 0) {
            serverTableModel.removeRow(0);
        }
    }
    public void clearPrice() {
        totalPrice = 0.0;
        textPane1.setText("Total: $0.00");
    }

    public void refreshMenu() {
        extraButton1.setVisible(false);
        extraButton2.setVisible(false);
        extraButton3.setVisible(false);
        extraButton4.setVisible(false);
        int numRows = 19;
        try{
            Statement stmt = jdbcpostgreSQL.conn.createStatement();
            Statement stmt2 = jdbcpostgreSQL.conn.createStatement();
            // SQL side;
            ResultSet result = stmt.executeQuery("SELECT COUNT(*) FROM menu_key");
            ResultSet resultButtonName = stmt2.executeQuery("SELECT item,name FROM menu_key WHERE item > 519");
            if(result.next()){
                numRows = result.getInt("count");
                System.out.print(numRows);
            }
            int i = 0;
            while(resultButtonName.next()){
                buttonList.get(i+23).setName(resultButtonName.getString("item"));
                buttonList.get(i+23).setText(resultButtonName.getString("name"));
                System.out.println(resultButtonName.getString("item") + "  " + resultButtonName.getString("name"));
                i++;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        numRows += 4; //+3 because of +, -, and finalize buttons
        for(int i = 23; i < numRows; i++) {
            buttonList.get(i).setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if((((JButton)e.getSource()).getName()).equals("+"))
            plusMode = true;
        if((((JButton)e.getSource()).getName()).equals("-"))
            plusMode = false;
        String currButton = ((JButton)e.getSource()).getName();
        String price = "";
        String itemName = "";
        String itemID = "";
        if(!currButton.equals("+") && !currButton.equals("-") && !currButton.equals("finalize")){
            try {
                Statement statement = jdbcpostgreSQL.conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT item, price, name FROM menu_key;");
                while (rs.next()) {
                    if(currButton.equals(rs.getString("item"))){
                        price = rs.getString("price");
                        itemName = rs.getString("name");
                        itemID = rs.getString("item");
                        System.out.println(itemName);
                        System.out.println(price);
                        System.out.println(itemID);
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        if(currButton.equals("finalize")) {
            try {
                Statement stmt = jdbcpostgreSQL.conn.createStatement();
                for(int i = 0; i < serverTableModel.getRowCount(); i++){
                    //grabs the current amount
                    int currQuant = 0;
                    ResultSet rs = stmt.executeQuery("SELECT quantity FROM weeksales WHERE item=\'" + currDay + "_" + (String)serverTableModel.getDataVector().get(i).get(3) + "\';");


                    while(rs.next()){
                        currQuant = Integer.parseInt(rs.getString("quantity"));
                        currQuant += Integer.parseInt((String)serverTableModel.getDataVector().get(i).get(1));
                    }
                    int result = stmt.executeUpdate("UPDATE weeksales SET quantity=" + currQuant+ " WHERE item=\'" + currDay + "_" + (String)serverTableModel.getDataVector().get(i).get(3) + "\';");
                    print("SERVER RESULTS: " + result);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            clearServerTable();
            clearPrice();
        }

        if(currButton.equals("refresh")) {
            refreshMenu();
        }

        if(currButton.equals(itemID)) {
            int index = alreadyInTicket(itemName);
            if(index == -1 && plusMode) {
                String inputArray[] = {itemName, "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(inputArray);
            }else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode) {
                    intAmountBefore++;
                }
                else {
                    intAmountBefore--;
                }

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
            updatePrice(price);
        }
    }



    public JPanel getRootPanel() {
        return main;
    }

    // helper print function
    public static void print(Object out) {
        System.out.println(out);
    }
}

