package de.akquinet.jbosscc.gbplugin.ui.migration.overview;

import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author siraj
 */
public class OverviewTreeTableModel extends ListTreeTableModelOnColumns implements TreeTableModel {
    public static String[] columnNames = {"Name", "Type", "Size"};
    public final int TREE_COLUMN = 0;
    public final int TYPE_COLUMN = 1;
    public final int SIZE_COLUMN = 2;
    private TreeTable myTreeTable;

    public OverviewTreeTableModel(MyDataNode root, ColumnInfo[] columnInfos) {
        super(root, columnInfos);
    }


    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Nullable
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case TREE_COLUMN:
                return TreeTableModel.class;
            case TYPE_COLUMN:
            case SIZE_COLUMN:
                return String.class;
        }
        throw new IllegalArgumentException();
    }

    @Nullable
    @Override
    public Object getValueAt(Object node, int column) {
        switch (column) {
            case TREE_COLUMN:
                return node;
            case TYPE_COLUMN:
                return ((MyDataNode) node).getType();
            case SIZE_COLUMN:
                return ((MyDataNode) node).getSize();
            default:
                break;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return column == TREE_COLUMN;
    }

    @Override
    public void setValueAt(Object aValue, Object node, int column) {

    }

    @Override
    public void setTree(JTree tree) {
        myTreeTable = ((TreeTableTree)tree).getTreeTable();
    }
}
