import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

    public JPanel rootPanel;
    public JPanel inventoryPanel;
    public JPanel DTOPanel;
    public JPanel menuItemsPanel;
    public JPanel DTOTableView;

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

    public JButton toggleTrendsButton;

    public JComboBox DTO_R1_MM_Box;
    public JComboBox DTO_R1_DD_Box;
    public JComboBox DTO_R1_YYYY_Box;
    public JComboBox DTO_R2_MM_Box;
    public JComboBox DTO_R2_DD_Box;
    public JComboBox DTO_R2_YYYY_Box;

    public JCheckBox range1CheckBox;
    public JCheckBox range2CheckBox;





    final private String[] invCol = {"Description", "SKU", "Quantity", "Delivered", "Sold By", "Delivered By", "Quantity Multiplier", "Price", "Extended", "Category", "Invoice Line", "Detailed Description"};
    final private String[] DTOCol = {"Item", "Quantity"};
    final private String[] menuItemsCol = {"Item", "Name", "Description", "Price"};
    final private String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
    final private String[] days = {"1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9"," 10"," 11"," 12"," 13"," 14"," 15"," 16"," 17"," 18"," 19"," 20"," 21"," 22"," 23"," 24"," 25"," 26"," 27"," 28"," 29"," 30"," 31"};
    final private String[] years = {"2021"," 2022"," 2023"," 2024"," 2025"," 2026"," 2027"," 2028"," 2029"," 2030"};

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
        DTOTableModel3 = new DefaultTableModel(DTOCol, 0) {
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

        // fill in combo box dates
//        DTO_R1_MM_Box = new JComboBox(months);
//        DTO_R1_DD_Box = new JComboBox(days);
//        DTO_R1_YYYY_Box = new JComboBox(years);
//        DTO_R2_MM_Box = new JComboBox(months);
//        DTO_R2_DD_Box = new JComboBox(days);
//        DTO_R2_YYYY_Box = new JComboBox(years);

        // setup column identifiers ------------------------------------------
        invTableModel.setColumnIdentifiers(invCol);
        invEditTableModel.setColumnIdentifiers(invCol);

        DTOTableModel1.setColumnIdentifiers(DTOCol);
        DTOTableModel2.setColumnIdentifiers(DTOCol);
        DTOTableModel3.setColumnIdentifiers(DTOCol);
        DTOEditTableModel.setColumnIdentifiers(DTOCol);

        menuItemsTableModel.setColumnIdentifiers(menuItemsCol);
        menuItemsEditTableModel.setColumnIdentifiers(menuItemsCol);
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
        // --------------------------------------------------------------------

        // set table not editable so users dont mess up table -----------------
        setNonFocusable(invTable);
        setNonFocusable(DTOTable1);
        setNonFocusable(DTOTable2);
        setNonFocusable(DTOTable3);
        setNonFocusable(menuItemsTable);
        // ---------------------------------------------------------------------

        // setup auto sort -----------------------------------------------------
        invTable.getRowSorter().toggleSortOrder(1);
        menuItemsTable.getRowSorter().toggleSortOrder(0);
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
    }

    public void setNonFocusable(JTable _table) {
        _table.setRowSelectionAllowed(true);
        _table.setFocusable(false);
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}


/* psuedo-psuedo code for item conversions --> THIS CODE HAS NOT BEEN TESTED, BUT LOGIC IS THERE <--
    //populate array of item conversions

    Statement stmt = jdbcpostgreSQL.conn.createStatement();
    String converArr[] = new String[19];
    

    ResultSet result = stmt.executeQuery("SELECT * FROM itemconversions;");
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