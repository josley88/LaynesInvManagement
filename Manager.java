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
    public JButton editRowButton;
    private JPanel inventoryPanel;
    private JScrollPane editScroll;
    final private String inventoryCol[] = {"SKU", "Description", "Quantity", "Category", "Delivered?", "Price"};
    final private String dailyTotalOrdersCol[] = {"Item", "Quantity"};

    public Manager() {
        inventoryTableModel = new DefaultTableModel(inventoryCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        editTableModel = new DefaultTableModel(inventoryCol, 1){
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {return false;}
                else {return true;}
            }
        };


        dailyTotalOrdersTableModel = new DefaultTableModel(dailyTotalOrdersCol, 0);


        inventoryTableModel.setColumnIdentifiers(inventoryCol);
        dailyTotalOrdersTableModel.setColumnIdentifiers(dailyTotalOrdersCol);
        editTableModel.setColumnIdentifiers(inventoryCol);


        // setup headers
        inventoryScroll.setColumnHeaderView(inventoryTable.getTableHeader());
        dailyTotalOrdersScroll.setColumnHeaderView(dailyTotalOrdersTable.getTableHeader());
        editScroll.setColumnHeaderView(editTable.getTableHeader());

        // set models for tables
        inventoryTable.setModel(inventoryTableModel);
        dailyTotalOrdersTable.setModel(dailyTotalOrdersTableModel);
        editTable.setModel(editTableModel);

        // set table not editable so users dont mess up table
        inventoryTable.setRowSelectionAllowed(true);
        inventoryTable.setFocusable(false);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


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