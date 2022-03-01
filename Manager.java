import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Manager {

    private JTabbedPane tabbedPane1;
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JPanel rootPanel;
    final private String inventoryCol[] = {"Ingredient", "Quantity", "Location", "Refrigerate", "Daily Change"};

    public Manager() {
        inventoryTableModel = new DefaultTableModel(inventoryCol, 0);
        inventoryTable.setModel(inventoryTableModel);
        inventoryTable.setTableHeader(header);

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
}
