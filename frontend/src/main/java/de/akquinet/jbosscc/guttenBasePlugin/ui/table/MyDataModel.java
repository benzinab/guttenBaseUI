package de.akquinet.jbosscc.guttenBasePlugin.ui.table;


import de.akquinet.jbosscc.guttenBasePlugin.data.nodes.MyDataNode;

public class MyDataModel extends MyAbstractTreeTableModel {
    // Spalten Name.
    static protected String[] columnNames = {"Name", "Size", "Actions", ""};

    // Spalten Typen.
    static protected Class<?>[] columnTypes = {MyTreeTableModel.class, Integer.class, Action.class, Boolean.class };

    public MyDataModel(MyDataNode rootNode) {
        super(rootNode);
        root = rootNode;
    }

    public Object getChild(Object parent, int index) {
        return ((MyDataNode) parent).getChildren().get(index);
    }


    public int getChildCount(Object parent) {
        return ((MyDataNode) parent).getChildren().size();
    }


    public int getColumnCount() {
        return columnNames.length;
    }


    public String getColumnName(int column) {
        return columnNames[column];
    }


    public Class<?> getColumnClass(int column) {
        return columnTypes[column];
    }

    public Object getValueAt(Object node, int column) {
        switch (column) {
            case 0:
                return ((MyDataNode) node).getName();
            case 1:
                return ((MyDataNode) node).getSize();
            case 2:
                return ((MyDataNode) node).getActions();
            case 3:
                return ((MyDataNode) node).isChecked();
            default:
                break;
        }
        return null;
    }

    public boolean isCellEditable(Object node, int column) {
        return column == 0;
        // Important to activate TreeExpandListener
    }

    public void setValueAt(Object aValue, Object node, int column) {
    }

}