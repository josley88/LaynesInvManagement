import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.time.*;
import java.util.Locale;

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
    private DefaultTableModel serverTableModel;
    final private String serverTicket[] = {"Item", "Amount", "Price", "ID"};
    private String insertion[] = {"Blank", "0"};
    private ArrayList<JButton> buttonList = new ArrayList<JButton>();
    private boolean plusMode = true; //true means in plus mode, false means in minus mode. default to plus mode
    LocalDate dt = LocalDate.now();
    String currDay = dt.getDayOfWeek().toString().substring(0,1) + dt.getDayOfWeek().toString().substring(1,dt.getDayOfWeek().toString().length()).toLowerCase();


    public Server(){
        serverTableModel = new DefaultTableModel(serverTicket, 0);
        serverTableModel.setColumnIdentifiers(serverTicket);
        ticketScroll.setColumnHeaderView(ticket.getTableHeader());
        ticket.setModel(serverTableModel);
        buttonSetup();
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
    }

    public int alreadyInTicket(String item)
    {
        for(int i = 0; i < serverTableModel.getRowCount(); i++){
            if((String)serverTableModel.getDataVector().get(i).get(0) == item)
                return i; //if in ticket return index where
        }
        return -1; //return -1 if not in ticket
    }
    public void clearServerTable() {
        while(serverTableModel.getRowCount() != 0) {
            serverTableModel.removeRow(0);
        }
    }
    public void updateDataBase() {
        while(serverTableModel.getRowCount() != 0) {
            serverTableModel.removeRow(0);
        }
    }
    public void actionPerformed(ActionEvent e) {
        if(((JButton)e.getSource()).getName() == "+")
            plusMode = true;
        if(((JButton)e.getSource()).getName() == "-")
            plusMode = false;
        String currButton = ((JButton)e.getSource()).getName();
        //System.out.print(currButton);
        String price = "";
        if(currButton != "+" || currButton != "-" || currButton != "finalize"){
            try {
                Statement statement = jdbcpostgreSQL.conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT item, price FROM menu_key;");
                while (rs.next()) {
                    if(currButton.equals(rs.getString("item"))){
                        price = rs.getString("price");
                        System.out.println(price);
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        if(currButton.equals("finalize")) {
            //System.out.println("enter finalize");
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
                    System.out.println(result);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            clearServerTable();
        }



        if(((JButton)e.getSource()).getName() == "501") {
            int index = alreadyInTicket("5 Finger Original");
            if(index == -1 && plusMode) {
                String fiveFingerOriginal[] = {"5 Finger Original", "1", price, ((JButton)e.getSource()).getName()};
                System.out.println(fiveFingerOriginal.length);
                serverTableModel.addRow(fiveFingerOriginal);
                System.out.println(serverTableModel.getDataVector().get(0).size());
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "502") {
            int index = alreadyInTicket("4 Finger Meal");

            if(index == -1 && plusMode) {
                String fourFingerMeal[] = {"4 Finger Meal", "1", price};
                serverTableModel.addRow(fourFingerMeal);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "503") {
            int index = alreadyInTicket("3 Finger Meal");

            if(index == -1 && plusMode) {
                String threeFingerMeal[] = {"3 Finger Meal", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(threeFingerMeal);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "504") {
            int index = alreadyInTicket("Kids Meal");

            if(index == -1 && plusMode) {
                String kidsMeal[] = {"Kids Meal", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(kidsMeal);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "505") {
            int index = alreadyInTicket("Gallon Of Tea");

            if(index == -1 && plusMode) {
                String gallonTea[] = {"Gallon Of Tea", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(gallonTea);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "506") {
            int index = alreadyInTicket("Family Pack");

            if(index == -1 && plusMode) {
                String familyPack[] = {"Family Pack", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(familyPack);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "507") {
            int index = alreadyInTicket("Club Sandwich Meal");

            if(index == -1 && plusMode) {
                String clubSandwichMeal[] = {"Club Sandwich Meal", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(clubSandwichMeal);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "508") {
            int index = alreadyInTicket("Club Sandwich Only");

            if(index == -1 && plusMode) {
                String clubSandwichOnly[] = {"Club Sandwich Only", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(clubSandwichOnly);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "509") {
            int index = alreadyInTicket("Sandwich Meal Combo");

            if(index == -1 && plusMode) {
                String sandwichMealCombo[] = {"Sandwich Meal Combo", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(sandwichMealCombo);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "510") {
            int index = alreadyInTicket("Sandwich Only");

            if(index == -1 && plusMode) {
                String sandwichOnly[] = {"Sandwich Only", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(sandwichOnly);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "511") {
            int index = alreadyInTicket("Grill Cheese Meal Combo");

            if(index == -1 && plusMode) {
                String grillCheeseMealCombo[] = {"Grill Cheese Meal Combo", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(grillCheeseMealCombo);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "512") {
            int index = alreadyInTicket("Grill Cheese Sandwich Only");

            if(index == -1 && plusMode) {
                String grillCheeseSandwichOnly[] = {"Grill Cheese Sandwich Only", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(grillCheeseSandwichOnly);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "513") {
            int index = alreadyInTicket("Layne's Sauce");

            if(index == -1 && plusMode) {
                String laynesSauce[] = {"Layne's Sauce", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(laynesSauce);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "514") {
            int index = alreadyInTicket("Chicken Finger");

            if(index == -1 && plusMode) {
                String chickenFinger[] = {"Chicken Finger", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(chickenFinger);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "515") {
            int index = alreadyInTicket("Texas Toast");

            if(index == -1 && plusMode) {
                String texasToast[] = {"Texas Toast", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(texasToast);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "516") {
            int index = alreadyInTicket("Potato Salad");

            if(index == -1 && plusMode) {
                String potatoSalad[] = {"Potato Salad", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(potatoSalad);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "517") {
            int index = alreadyInTicket("Crinkle Cut Fries");

            if(index == -1 && plusMode) {
                String crinkleCutFries[] = {"Crinkle Cut Fries", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(crinkleCutFries);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "518") {
            int index = alreadyInTicket("Fountain Drink");

            if(index == -1 && plusMode) {
                String fountainDrink[] = {"Fountain Drink", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(fountainDrink);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }else

        if(((JButton)e.getSource()).getName() == "519") {
            int index = alreadyInTicket("Bottle Drink");

            if(index == -1 && plusMode) {
                String bottleDrink[] = {"Bottle Drink", "1", price, ((JButton)e.getSource()).getName()};
                serverTableModel.addRow(bottleDrink);
            }

            else if(index != -1) { //updates current amount
                String stringAmountBefore = serverTableModel.getValueAt(index,1).toString();
                int intAmountBefore = Integer.parseInt(stringAmountBefore);
                if(plusMode)
                    intAmountBefore++;
                else
                    intAmountBefore--;

                if(intAmountBefore <= 0) //if now has 0 items
                    serverTableModel.removeRow(index);
                else {
                    String newAmount = String.valueOf(intAmountBefore);
                    serverTableModel.setValueAt(newAmount, index, 1);
                }
            }
        }




    }



    public JPanel getRootPanel() {
        return main;
    }
}
