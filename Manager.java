import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Manager {

    private JTabbedPane tabbedPane1;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private DefaultTableModel dailyTotalOrdersTableModel;
    private JPanel rootPanel;
    private JTable dailyTotalOrdersTable;
    private JScrollPane inventoryScroll;
    private JScrollPane dailyTotalOrdersScroll;
    private JTextField textField1;
    final private String inventoryCol[] = {"Description", "Quantity", "Category", "Delivered?", "Price"};
    final private String dailyTotalOrdersCol[] = {"Item", "Quantity"};

    public Manager() {
        inventoryTableModel = new DefaultTableModel(inventoryCol, 0);
        dailyTotalOrdersTableModel = new DefaultTableModel(dailyTotalOrdersCol, 0);
        dailyTotalOrdersTableModel.setColumnIdentifiers(dailyTotalOrdersCol);
        inventoryTableModel.setColumnIdentifiers(inventoryCol);

        // setup headers
        inventoryScroll.setColumnHeaderView(inventoryTable.getTableHeader());
        dailyTotalOrdersScroll.setColumnHeaderView(dailyTotalOrdersTable.getTableHeader());


        inventoryTable.setModel(inventoryTableModel);
        dailyTotalOrdersTable.setModel(dailyTotalOrdersTableModel);



    }

    public JTable getInventoryTable() {
        return inventoryTable;
    }
    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addRowToInventoryTable(Object[] row) {
        inventoryTableModel.addRow(row);
    }

    public void addRowToDTOTable(Object[] row) {
        dailyTotalOrdersTableModel.addRow(row);
    }
}
