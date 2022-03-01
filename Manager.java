import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Manager {

    private JTabbedPane tabbedPane1;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;
    private JPanel rootPanel;

    public Manager() {
        tableModel = new DefaultTableModel()
        inventoryTable = new JTable(tableModel);
    }

    public JTable getInventoryTable() {
        return inventoryTable;
    }
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
