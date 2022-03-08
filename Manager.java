import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Manager {

    public JTabbedPane tabbedPane;

    public JTable invTable;
    public JTable invEditTable;
    public JTable DTOTable1;
    public JTable DTOTable2;
    public JTable DTOTable3;
    public JTable DTOEditTable;
    public JTable menuItemsTable;
    public JTable menuItemsEditTable;


    public DefaultTableModel invTableModel;
    public DefaultTableModel invEditTableModel;

    public DefaultTableModel DTOTableModel1;
    public DefaultTableModel DTOTableModel2;
    public DefaultTableModel DTOTableModel3;
    public DefaultTableModel DTOEditTableModel;

    public DefaultTableModel menuItemsTableModel;
    public DefaultTableModel menuItemsEditTableModel;

    public DefaultTableModel orderPopTableModel;
    public DefaultTableModel invPopTableModel;

    public JPanel rootPanel;
    public JPanel inventoryPanel;
    public JPanel DTOPanel;
    public JPanel menuItemsPanel;
    public JPanel DTOTableView;
    public JPanel DTOTable3Panel;

    public JScrollPane invScroll;
    public JScrollPane invEditScroll;
    public JScrollPane DTOScroll1;
    public JScrollPane DTOScroll2;
    public JScrollPane DTOScroll3;
    public JScrollPane DTOEditScroll;
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

    // date selection for range 1 (From, To)
    public JComboBox<String> DTO_R1From_YYYY_Box;
    public JComboBox<String> DTO_R1From_MM_Box;
    public JComboBox<String> DTO_R1From_DD_Box;
    public JComboBox<String> DTO_R1To_YYYY_Box;
    public JComboBox<String> DTO_R1To_MM_Box;
    public JComboBox<String> DTO_R1To_DD_Box;

    // date selection for range 2 (From, To)
    public JComboBox<String> DTO_R2From_YYYY_Box;
    public JComboBox<String> DTO_R2From_MM_Box;
    public JComboBox<String> DTO_R2From_DD_Box;
    public JComboBox<String> DTO_R2To_YYYY_Box;
    public JComboBox<String> DTO_R2To_MM_Box;
    public JComboBox<String> DTO_R2To_DD_Box;

    public JButton DTORefreshRange1Button;
    public JButton DTORefreshRange2Button;

    public JCheckBox enableTrendCheckBox;

    public JTextField logTextField;

    public JTextField revenue2TextBox;
    public JTextField revenue1TextBox;

    public JButton updateInventoryButton;
    public JButton invRefreshRangeButton;

    public JComboBox<String> inv_From_YYYY_Box;
    public JComboBox<String> inv_From_MM_Box;
    public JComboBox<String> inv_From_DD_Box;
    public JComboBox<String> inv_To_YYYY_Box;
    public JComboBox<String> inv_To_MM_Box;
    public JComboBox<String> inv_To_DD_Box;

    public JPanel orderPopPanel;
    public JTable orderPopTable;

    public JScrollPane orderPopScroll;
    public JPanel invPopPanel;
    public JScrollPane invPopScroll;
    public JTable invPopTable;

    public JComboBox<String> invUpdate_From_YYYY_Box;
    public JComboBox<String> invUpdate_From_MM_Box;
    public JComboBox<String> invUpdate_From_DD_Box;
    public JComboBox<String> invUpdate_To_YYYY_Box;
    public JComboBox<String> invUpdate_To_MM_Box;
    public JComboBox<String> invUpdate_To_DD_Box;


    final private String[] invCol = {"Description", "SKU", "Quantity", "Fill Amt", "Delivered", "Sold By", "Delivered By", "Quantity Multiplier", "Price", "Extended", "Category", "Invoice Line", "Detailed Description"};
    final private String[] DTOCol = {"Item", "Quantity", "Date of Purchase"};
    //final private String[] DTOTrend = {"Item", "Name", "Trend (%)"};
    final private String[] menuItemsCol = {"Item", "Name", "Description", "Price"};
    final private String[] DTOTrendCol = {"Item", "Quant. 1", "Quant. 2", "Price", "Rev. 1", "Rev. 2", "Trend %"};
    final private String[] orderPopCol = {"Item ID", "Quantity"};
    final private String[] invPopCol = {"Description", "Qty Used"};
    public Manager() {

        // setup table models, most without directly editable cells -----------------------------------------------
        invTableModel = new DefaultTableModel(invCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        invEditTableModel = new DefaultTableModel(invCol, 1);


        DTOTableModel1 = new DefaultTableModel(DTOCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        DTOTableModel2 = new DefaultTableModel(DTOCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        DTOTableModel3 = new DefaultTableModel(DTOTrendCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return Double.class;
                } else {
                    return String.class;
                }
            }
        };
        DTOEditTableModel = new DefaultTableModel(DTOCol, 1);

        orderPopTableModel = new DefaultTableModel(orderPopCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class;
                } else {
                    return String.class;
                }
            }
        };

        invPopTableModel = new DefaultTableModel(invPopCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };

        menuItemsTableModel = new DefaultTableModel(menuItemsCol, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        menuItemsEditTableModel = new DefaultTableModel(menuItemsCol, 1);
        // ------------------------------------------------------------------

        // fill in combo box dates ------------------------------------------
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        for (String month : months) {
            DTO_R1From_MM_Box.addItem(month);
            DTO_R2From_MM_Box.addItem(month);
            DTO_R1To_MM_Box.addItem(month);
            DTO_R2To_MM_Box.addItem(month);
            inv_From_MM_Box.addItem(month);
            inv_To_MM_Box.addItem(month);
            invUpdate_From_MM_Box.addItem(month);
            invUpdate_To_MM_Box.addItem(month);

        }
        String[] days = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        for (String day : days) {
            DTO_R1From_DD_Box.addItem(day);
            DTO_R2From_DD_Box.addItem(day);
            DTO_R1To_DD_Box.addItem(day);
            DTO_R2To_DD_Box.addItem(day);
            inv_From_DD_Box.addItem(day);
            inv_To_DD_Box.addItem(day);
            invUpdate_From_DD_Box.addItem(day);
            invUpdate_To_DD_Box.addItem(day);
        }
        String[] years = {"2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"};
        for (String year : years) {
            DTO_R1From_YYYY_Box.addItem(year);
            DTO_R2From_YYYY_Box.addItem(year);
            DTO_R1To_YYYY_Box.addItem(year);
            DTO_R2To_YYYY_Box.addItem(year);
            inv_From_YYYY_Box.addItem(year);
            inv_To_YYYY_Box.addItem(year);
            invUpdate_From_YYYY_Box.addItem(year);
            invUpdate_To_YYYY_Box.addItem(year);
        }

        // setup column identifiers ------------------------------------------
        invTableModel.setColumnIdentifiers(invCol);
        invEditTableModel.setColumnIdentifiers(invCol);

        DTOTableModel1.setColumnIdentifiers(DTOCol);
        DTOTableModel2.setColumnIdentifiers(DTOCol);
        DTOTableModel3.setColumnIdentifiers(DTOTrendCol);
        DTOEditTableModel.setColumnIdentifiers(DTOCol);

        menuItemsTableModel.setColumnIdentifiers(menuItemsCol);
        menuItemsEditTableModel.setColumnIdentifiers(menuItemsCol);
        orderPopTableModel.setColumnIdentifiers(orderPopCol);
        invPopTableModel.setColumnIdentifiers(invPopCol);
        // --------------------------------------------------------------------


        // setup headers ------------------------------------------------------
        invScroll.setColumnHeaderView(invTable.getTableHeader());
        invEditScroll.setColumnHeaderView(invEditTable.getTableHeader());

        DTOScroll1.setColumnHeaderView(DTOTable1.getTableHeader());
        DTOScroll2.setColumnHeaderView(DTOTable2.getTableHeader());
        DTOScroll3.setColumnHeaderView(DTOTable3.getTableHeader());
        DTOEditScroll.setColumnHeaderView(DTOEditTable.getTableHeader());

        menuItemsScroll.setColumnHeaderView(menuItemsTable.getTableHeader());
        menuItemsEditScroll.setColumnHeaderView(menuItemsEditTable.getTableHeader());

        orderPopScroll.setColumnHeaderView(orderPopTable.getTableHeader());
        invPopScroll.setColumnHeaderView(invPopTable.getTableHeader());
        // --------------------------------------------------------------------


        // set models for tables ----------------------------------------------
        invTable.setModel(invTableModel);
        invEditTable.setModel(invEditTableModel);

        DTOTable1.setModel(DTOTableModel1);
        DTOTable2.setModel(DTOTableModel2);
        DTOTable3.setModel(DTOTableModel3);
        DTOEditTable.setModel(DTOEditTableModel);

        menuItemsTable.setModel(menuItemsTableModel);
        menuItemsEditTable.setModel(menuItemsEditTableModel);

        orderPopTable.setModel(orderPopTableModel);
        invPopTable.setModel(invPopTableModel);
        // --------------------------------------------------------------------

        // set table not editable so users dont mess up table -----------------
        setNonFocusable(invTable);
        setNonFocusable(DTOTable1);
        setNonFocusable(DTOTable2);
        setNonFocusable(DTOTable3);
        setNonFocusable(menuItemsTable);
        setNonFocusable(orderPopTable);
        setNonFocusable(invPopTable);
        // ---------------------------------------------------------------------

        // setup auto sort -----------------------------------------------------
        invTable.getRowSorter().toggleSortOrder(1);
        menuItemsTable.getRowSorter().toggleSortOrder(0);
        DTOTable3.getRowSorter().toggleSortOrder(6);
        orderPopTable.getRowSorter().toggleSortOrder(1);
//        invPopTable.getRowSorter().toggleSortOrder(1);
        // ---------------------------------------------------------------------

        // setup tabbed pane font size -----------------------------------------
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        // ---------------------------------------------------------------------


    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void addRowToInventoryTable(Object[] row) {
        invTableModel.addRow(row);
    }

    public void addRowToDTOTable(Object[] row) {
        DTOTableModel1.addRow(row);
    }

    public void addRowTomMenuItemsTable(Object[] row) {
        menuItemsTableModel.addRow(row);
    }

    public void clearTables() {
        invTableModel.setRowCount(0);
        DTOTableModel1.setRowCount(0);
        menuItemsTableModel.setRowCount(0);
        orderPopTableModel.setRowCount(0);
    }

    public void clearTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
    }

    public void setNonFocusable(JTable _table) {
        _table.setRowSelectionAllowed(true);
        _table.setFocusable(false);
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}