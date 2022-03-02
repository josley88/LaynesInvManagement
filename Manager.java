import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Manager {

    private JTabbedPane tabbedPane;
    public JTable inventoryTable;
    public DefaultTableModel inventoryTableModel;
    public DefaultTableModel invEditTableModel;

    public DefaultTableModel dailyTotalOrdersTableModel;
    public DefaultTableModel DOTEditTableModel;

    private JPanel rootPanel;
    private JTable dailyTotalOrdersTable;
    private JScrollPane inventoryScroll;
    private JScrollPane DTOScroll;
    public JTextField inventoryTextField;
    public JButton invDeleteRowButton;
    public JButton invAddRowButton;
    public JTable invEditTable;
    public JButton invEditRowButton;
    public JPanel inventoryPanel;
    private JScrollPane invEditScroll;
    public JButton invRefreshButton;
    private JPanel DTOPanel;
    private JButton DTOAddRowButton;
    private JButton DTOEditRowButton;
    private JButton DTODeleteRowButton;
    private JButton DTORefreshButton;
    private JTable table1;
    final private String inventoryCol[] = {"Description", "SKU", "Quantity", "Delivered", "Sold By", "Delivered By", "Quantity Multiplier", "Price", "Extended", "Category", "Invoice Line", "Detailed Description"};
    final private String dailyTotalOrdersCol[] = {"Item", "Quantity"};

    public Manager() {
        inventoryTableModel = new DefaultTableModel(inventoryCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        invEditTableModel = new DefaultTableModel(inventoryCol, 1);


        dailyTotalOrdersTableModel = new DefaultTableModel(dailyTotalOrdersCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        DOTEditTableModel = new DefaultTableModel(dailyTotalOrdersCol, 1);

        // setup column identifiers
        inventoryTableModel.setColumnIdentifiers(inventoryCol);
        invEditTableModel.setColumnIdentifiers(inventoryCol);

        dailyTotalOrdersTableModel.setColumnIdentifiers(dailyTotalOrdersCol);
        DOTEditTableModel.setColumnIdentifiers(dailyTotalOrdersCol);


        // setup headers
        inventoryScroll.setColumnHeaderView(inventoryTable.getTableHeader());
        DTOScroll.setColumnHeaderView(dailyTotalOrdersTable.getTableHeader());
        invEditScroll.setColumnHeaderView(invEditTable.getTableHeader());

        // set models for tables
        inventoryTable.setModel(inventoryTableModel);
        dailyTotalOrdersTable.setModel(dailyTotalOrdersTableModel);
        invEditTable.setModel(invEditTableModel);

        // set table not editable so users dont mess up table
        inventoryTable.setRowSelectionAllowed(true);
        inventoryTable.setFocusable(false);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


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
        inventoryTableModel.setRowCount(0);
        dailyTotalOrdersTableModel.setRowCount(0);

    }
}