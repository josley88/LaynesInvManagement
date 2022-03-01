import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
    final private String serverTicket[] = {"Item", "Amount"};
    private String insertion[] = {"Blank", "0"};
    private ArrayList<JButton> buttonList = new ArrayList<JButton>();

    public Server(){
        serverTableModel = new DefaultTableModel(serverTicket, 0);
        serverTableModel.setColumnIdentifiers(serverTicket);
        ticketScroll.setColumnHeaderView(ticket.getTableHeader());
        ticket.setModel(serverTableModel);
        buttonSetup();
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

    public void actionPerformed(ActionEvent e) {
        if(insertion[0] == "Blank"){
            System.out.println(((JButton)e.getSource()).getName());
        }
    }



    public JPanel getRootPanel() {
        return main;
    }
}
