import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Manager {

    private JTabbedPane tabbedPane;
    private JTable inventoryTable;
    public DefaultTableModel inventoryTableModel;
    public DefaultTableModel dailyTotalOrdersTableModel;
    public DefaultTableModel editTableModel;
    private JPanel rootPanel;
    private JTable dailyTotalOrdersTable;
    private JScrollPane inventoryScroll;
    private JScrollPane dailyTotalOrdersScroll;
    public JTextField inventoryTextField;
    public JButton deleteRowButton;
    public JButton addRowButton;
    public JTable editTable;
    private JButton changeRowButton;
    private JPanel inventoryPanel;
    final private String inventoryCol[] = {"Description", "Quantity", "Category", "Delivered?", "Price"};
    final private String dailyTotalOrdersCol[] = {"Item", "Quantity"};

    public Manager() {
        inventoryTableModel = new DefaultTableModel(inventoryCol, 0);
        dailyTotalOrdersTableModel = new DefaultTableModel(dailyTotalOrdersCol, 0);
        editTableModel = new DefaultTableModel(inventoryCol, 1);

        inventoryTableModel.setColumnIdentifiers(inventoryCol);
        dailyTotalOrdersTableModel.setColumnIdentifiers(dailyTotalOrdersCol);


        // setup headers
        inventoryScroll.setColumnHeaderView(inventoryTable.getTableHeader());
        dailyTotalOrdersScroll.setColumnHeaderView(dailyTotalOrdersTable.getTableHeader());


        inventoryTable.setModel(inventoryTableModel);
        dailyTotalOrdersTable.setModel(dailyTotalOrdersTableModel);
        editTable.setModel(editTableModel);


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

    public void clearTables() {
        for (int i = 0; i < inventoryTableModel.getRowCount(); i++) {
            inventoryTableModel.removeRow(i);
        }
        for (int i = 0; i < dailyTotalOrdersTableModel.getRowCount(); i++) {
            dailyTotalOrdersTableModel.removeRow(i);
        }

    }
}
