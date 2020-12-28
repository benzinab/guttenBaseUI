package de.akquinet.jbosscc.gbplugin.ui.migrate.overview;

import com.intellij.icons.AllIcons;
import com.intellij.ui.treeStructure.treetable.TreeTable;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class OverviewTreeTable extends TreeTable {
    public OverviewTreeTable(OverviewTreeTableModel treeTableModel) {
        super(treeTableModel);

        TableColumn typeColumn = getColumnModel().getColumn(treeTableModel.TYPE_COLUMN);
        TableColumn sizeColumn = getColumnModel().getColumn(treeTableModel.SIZE_COLUMN);
        UIManager.put("Tree.leafIcon", AllIcons.Nodes.DataColumn);
    }
}
