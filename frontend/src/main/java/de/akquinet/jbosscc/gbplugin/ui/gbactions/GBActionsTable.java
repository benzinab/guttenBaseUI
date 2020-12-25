package de.akquinet.jbosscc.gbplugin.ui.gbactions;

import com.intellij.ui.TableViewSpeedSearch;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import de.akquinet.jbosscc.gbplugin.data.Action;
import de.akquinet.jbosscc.gbplugin.data.ActionType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class GBActionsTable extends TableView<Action> {
    public GBActionsTable(List<Action> actions, ColumnInfo[] columnInfos){
        super(new ListTableModel<>(columnInfos, actions, 0));
        setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
        getColumnModel().getColumn(0).setCellRenderer(createDisplayNameCellRenderer());
        getColumnModel().getColumn(1).setCellRenderer(creatTypeCellRenderer());
        getColumnModel().getColumn(0).setResizable(true);
        setShowGrid(false);
        setShowVerticalLines(false);
        setGridColor(getForeground());

        String maxName = "123456789 123456789";

        int preferred = (int)(new JLabel(maxName, SwingConstants.LEFT).getPreferredSize().width * 1.1);
        getColumnModel().getColumn(0).setMinWidth(preferred);
        getColumnModel().getColumn(0).setPreferredWidth(preferred);

        new TableViewSpeedSearch<>(this) {
            @Override
            protected String getItemText(@NotNull Action element) {
                return element.getName();
            }
        };

    }
    private TableCellRenderer createDisplayNameCellRenderer() {
        return new TableCellRenderer() {
            final JLabel myLabel = new JLabel();
            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
                                                           final int row,
                                                           final int column) {
                final String name = (String) value;
                // fix for a marvellous Swing peculiarity: AccessibleJTable likes to pass null here
                myLabel.setText(name);
                setLabelColors(myLabel, table, isSelected, row);
                return myLabel;
            }
        };
    }

    private TableCellRenderer creatTypeCellRenderer() {
        return new TableCellRenderer() {
            final JLabel myLabel = new JLabel();
            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
                                                           final int row,
                                                           final int column) {
                final ActionType actionType = (ActionType)value;
                // fix for a marvellous Swing peculiarity: AccessibleJTable likes to pass null here
                myLabel.setText(actionType.toString());
                setLabelColors(myLabel, table, isSelected, row);
                return myLabel;
            }
        };
    }

    private static Component setLabelColors(final Component label, final JTable table, final boolean isSelected, final int row) {
        if (label instanceof JComponent) {
            ((JComponent)label).setOpaque(true);
        }
        label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return label;
    }
}
