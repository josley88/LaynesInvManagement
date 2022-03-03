import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Manager {

    private JTabbedPane tabbedPane;

    public JTable invTable;
    public JTable invEditTable;
    public JTable DTOTable;
    public JTable DTOEditTable;
    public JTable menuItemsTable;
    public JTable menuItemsEditTable;

    public DefaultTableModel invTableModel;
    public DefaultTableModel invEditTableModel;

    public DefaultTableModel DTOTableModel;
    public DefaultTableModel DTOEditTableModel;

    public DefaultTableModel menuItemsTableModel;
    public DefaultTableModel menuItemsEditTableModel;

    public JPanel rootPanel;
    public JPanel inventoryPanel;
    public JPanel DTOPanel;
    public JPanel menuItemsPanel;

    private JScrollPane invScroll;
    private JScrollPane invEditScroll;
    private JScrollPane DTOScroll;
    private JScrollPane DTOEditScroll;
    public JScrollPane menuItemsScroll;
    public JScrollPane menuItemsEditScroll;

    public JButton invAddRowButton;
    public JButton invEditRowButton;
    public JButton invDeleteRowButton;
    public JButton invRefreshButton;

    public JButton DTOAddRowButton;
    public JButton DTOEditRowButton;
    public JButton DTODeleteRowButton;
    public JButton DTORefreshButton;

    public JButton menuItemsAddRowButton;
    public JButton menuItemsDeleteRowButton;
    public JButton menuItemsEditRowButton;
    public JButton menuItemsRefreshButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JButton toggleTrendsButton;
    private JCheckBox range1CheckBox;
    private JCheckBox range2CheckBox;

    final private String[] invCol = {"Description", "SKU", "Quantity", "Delivered", "Sold By", "Delivered By", "Quantity Multiplier", "Price", "Extended", "Category", "Invoice Line", "Detailed Description"};
    final private String[] DTOCol = {"Item", "Quantity"};
    final private String[] menuItemsCol = {"Item", "Name", "Description", "Price"};

    public Manager() {

        // setup table models -----------------------------------------------
        invTableModel = new DefaultTableModel(invCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        invEditTableModel = new DefaultTableModel(invCol, 1);

        DTOTableModel = new DefaultTableModel(DTOCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        DTOEditTableModel = new DefaultTableModel(DTOCol, 1);

        menuItemsTableModel = new DefaultTableModel(menuItemsCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        menuItemsEditTableModel = new DefaultTableModel(menuItemsCol, 1);
        // ------------------------------------------------------------------


        // setup column identifiers ------------------------------------------
        invTableModel.setColumnIdentifiers(invCol);
        invEditTableModel.setColumnIdentifiers(invCol);

        DTOTableModel.setColumnIdentifiers(DTOCol);
        DTOEditTableModel.setColumnIdentifiers(DTOCol);

        menuItemsTableModel.setColumnIdentifiers(menuItemsCol);
        menuItemsEditTableModel.setColumnIdentifiers(menuItemsCol);
        // --------------------------------------------------------------------


        // setup headers ------------------------------------------------------
        invScroll.setColumnHeaderView(invTable.getTableHeader());
        invEditScroll.setColumnHeaderView(invEditTable.getTableHeader());

        DTOScroll.setColumnHeaderView(DTOTable.getTableHeader());
        DTOEditScroll.setColumnHeaderView(DTOEditTable.getTableHeader());

        menuItemsScroll.setColumnHeaderView(menuItemsTable.getTableHeader());
        menuItemsEditScroll.setColumnHeaderView(menuItemsEditTable.getTableHeader());
        // --------------------------------------------------------------------


        // set models for tables ----------------------------------------------
        invTable.setModel(invTableModel);
        invEditTable.setModel(invEditTableModel);

        DTOTable.setModel(DTOTableModel);
        DTOEditTable.setModel(DTOEditTableModel);

        menuItemsTable.setModel(menuItemsTableModel);
        menuItemsEditTable.setModel(menuItemsEditTableModel);
        // --------------------------------------------------------------------

        // set table not editable so users dont mess up table -----------------
        invTable.setRowSelectionAllowed(true);
        invTable.setFocusable(false);
        invTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DTOTable.setRowSelectionAllowed(true);
        DTOTable.setFocusable(false);
        DTOTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        menuItemsTable.setRowSelectionAllowed(true);
        menuItemsTable.setFocusable(false);
        menuItemsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // ---------------------------------------------------------------------


    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addRowToInventoryTable(Object[] row) {
        invTableModel.addRow(row);
    }

    public void addRowToDTOTable(Object[] row) {
        DTOTableModel.addRow(row);
    }

    public void addRowTomMenuItemsTable(Object[] row) {
        menuItemsTableModel.addRow(row);
    }

    public void clearTables() {
        invTableModel.setRowCount(0);
        DTOTableModel.setRowCount(0);
        menuItemsTableModel.setRowCount(0);
    }
}