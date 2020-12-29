package de.akquinet.jbosscc.gbplugin.ui.migrate.overview;

import com.intellij.icons.AllIcons;
import com.intellij.ui.TreeTableSpeedSearch;
import com.intellij.ui.dualView.TreeTableView;
import de.akquinet.jbosscc.gbplugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.DatabaseNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.TableNode;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class OverviewTreeTable extends TreeTableView {
    public OverviewTreeTable(OverviewTreeTableModel treeTableModel) {
        super(treeTableModel);

        setRowHeight(20);//FIXME
        setTreeCellRenderer(new MyTreeCellRenderer());
        setRootVisible(true);

        //getColumnModel().getColumn(0).getCellRenderer().getTableCellRendererComponent(this, );
        //getColumnModel().getColumn(0).setTreeCellRenderer(createDisplayNameCellRenderer());

        TableColumn typeColumn = getColumnModel().getColumn(treeTableModel.TYPE_COLUMN);
        TableColumn sizeColumn = getColumnModel().getColumn(treeTableModel.SIZE_COLUMN);

        UIManager.put("Tree.leafIcon", AllIcons.Nodes.DataColumn);


        String maxName = "123456789 123456789";

        int preferred = (int)(new JLabel(maxName, SwingConstants.LEFT).getPreferredSize().width * 1.1);
        getColumnModel().getColumn(0).setMinWidth(preferred);
        getColumnModel().getColumn(0).setPreferredWidth(preferred);

        new TreeTableSpeedSearch(this);


    }


    private class MyTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            if (selected) {
            setBackground(getSelectionBackground());
            setForeground(getSelectionForeground());
            }
            else {
                final Color bg = tree.getBackground();
                setBackground(tree.getBackground());
                setForeground(tree.getForeground());
            }
            setOpaque(selected);
            JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            //your setIcon() / setText() code according to the value
            if (value instanceof DatabaseNode) {
                DatabaseNode schemaNode = (DatabaseNode) value;
                label.setIcon(AllIcons.Nodes.DataSchema);
            } else if (value instanceof TableNode) {
                TableNode tableNode = (TableNode) value;
                label.setIcon(AllIcons.Nodes.DataTables);
            } else if (value instanceof ColumnNode) {
                ColumnNode columnNode = (ColumnNode) value;
                label.setIcon(AllIcons.Nodes.DataColumn);
            }
            return label;
        }
    }


}
