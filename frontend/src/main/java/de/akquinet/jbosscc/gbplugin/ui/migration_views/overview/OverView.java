package de.akquinet.jbosscc.gbplugin.ui.migration_views.overview;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.DumbAwareActionButton;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.actions.OpenGBActionsAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBActionType;
import de.akquinet.jbosscc.gbplugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.DatabaseNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.TableNode;
import de.akquinet.jbosscc.gbplugin.helper.GsonHelper;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.gbplugin.ui.migration_views.resultview.ResultView;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OverView extends AbstractView {

    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JButton nextButton;
    private JButton cancelButton;
    private JButton backButton;
    private JPanel tableContainer;
    private JButton plusButton;
    private OverviewTreeTable overviewTreeTable;

    private Migration migration;
    private List<GBAction> myGBActions;

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public OverView(Migration migration) {
        this.migration = migration;

        //action listeners
        nextButton.addActionListener(e -> next());
        cancelButton.addActionListener(e -> close(content));
        backButton.addActionListener(e -> {
            int confirmed = Messages.showConfirmationDialog(content,
                    "By stepping back, the current configuration will be deleted. Are you sure to go back?",
                    "Confirmation",
                    "Back",
                    "Cancel");
            if (confirmed == 1) {
                return;
            }
            backTo(content, "1");
        });

        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OpenGBActionsAction().actionPerformed(null);
            }
        });
    }

    public void next() {
            ResultView resultView = new ResultView(migration);
            JDialog parent = (JDialog) SwingUtilities.getWindowAncestor(this.content);
            Container contentPane = parent.getContentPane();
            contentPane.add(resultView.getContent(), "3");
            CardLayout cl = (CardLayout) (contentPane.getLayout());
            cl.show(contentPane, "3");
            SwingUtilities.updateComponentTreeUI(parent);
    }

    public void fillData() {
        DatabaseMetaData metaData;
        try {
            metaData = migration.getConnectorRepository().getDatabaseMetaData("source");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
            backTo(content, "1");
            return;
        }
        MyDataNode root;
        try {
            root = new DatabaseNode(metaData);
        } catch (SQLException throwables) {
            Messages.showErrorDialog("Cannot get database name!", "Error!");
            throwables.printStackTrace();
            backTo(content, "1");
            return;
        }
        MyDataNode finalRoot = root;
        metaData.getTableMetaData().forEach(table -> {
                TableNode tableNode = new TableNode(table);
                table.getColumnMetaData().forEach(column -> tableNode.addNode(new ColumnNode(column)));
                finalRoot.addNode(tableNode);
            });

        OverviewTreeTableModel treeTableModel = new OverviewTreeTableModel(root, createInfoColumns());
        overviewTreeTable = new OverviewTreeTable(treeTableModel);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(overviewTreeTable);
        createActions(decorator);

        JPanel myRoot = new JPanel(new BorderLayout());
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        JLabel myCountLabel = new JLabel();
        myCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        myCountLabel.setForeground(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES.getFgColor());
        myRoot.add(myCountLabel, BorderLayout.SOUTH);
        decorator.getActionsPanel().setMinimumSize(new Dimension(600, 600));
        tableContainer = decorator.createPanel();
    }

    private void updateActions() {
        try {
            myGBActions = GsonHelper.importJSON("actions.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            myGBActions = new ArrayList<>();
        }
    }

    private void createActions(ToolbarDecorator decorator) {
        updateActions();
        myGBActions.forEach(gbAction -> {
            OverviewAddAction action = new OverviewAddAction(gbAction);
            decorator.addExtraAction(action);
        });

    }

    public ColumnInfo[] createInfoColumns() {
        final ColumnInfo[] columnInfos = {new ColumnInfo<MyDataNode, MyDataNode>("Name") {
            @Override
            public @Nullable MyDataNode valueOf(MyDataNode node) {
                return node;
            }
        }, new ColumnInfo<MyDataNode, String>(("Type")) {
            @Override
            public String valueOf(final MyDataNode node) {
                return node.getType();
            }
        }, new ColumnInfo<MyDataNode, Integer>(("Type")) {
            @Override
            public Integer valueOf(final MyDataNode node) {
                return node.getSize();
            }
        }};
        return columnInfos;
    }


    private void createUIComponents() {
        fillData();
    }

    public JPanel getContent() {
        return content;
    }

    public class OverviewAddAction extends DumbAwareActionButton {

        private final GBAction gbAction;

        public OverviewAddAction(@NotNull GBAction gbAction) {
            super(gbAction.getName(), "", PlatformIcons.NEW_PARAMETER);
            this.gbAction = gbAction;
            addCustomUpdater(e-> isEnabled(gbAction));
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            int[] rows = overviewTreeTable.getSelectedRows();
            int confirmed = Messages.showConfirmationDialog(overviewTreeTable,
                    String.format("Do you really want to add the action %s to  selected item(s)", gbAction.getName()),
                    "Confirmation",
                    "Add",
                    "Cancel"
            );
            if (confirmed == 1) {
                return;
            }
            for (int row : rows) {
                MyDataNode node = (MyDataNode) overviewTreeTable.getValueAt(row, 0);
                GBAction newGBAction;
                try {
                    // create a new instance of the action (otherwise the same object will be overwritten).
                    newGBAction = (GBAction) gbAction.clone();
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    cloneNotSupportedException.printStackTrace();
                    Messages.showErrorDialog(cloneNotSupportedException.getMessage(), "Error!");
                    return;
                }
                newGBAction.setSource(node);
                if (node instanceof ColumnNode && newGBAction.getGBActionType().equals(GBActionType.RENAME)) {
                    newGBAction.setGBActionType(GBActionType.RENAME_COLUMN);
                }
                else if (node instanceof TableNode && newGBAction.getGBActionType().equals(GBActionType.RENAME)) {
                    newGBAction.setGBActionType(GBActionType.RENAME_TABLE);
                }
                migration.addGBAction(newGBAction);
            }
        }

        @Override
        public boolean displayTextInToolbar() {
            return true; // show action names
        }

        private boolean isEnabled(GBAction gbAction) {
            int[] rows = overviewTreeTable.getSelectedRows();
            for (int row : rows) {
                if (!singleCheck(row, gbAction)) {
                    return false;
                }
            }
            return true;
        }

        private boolean singleCheck(int row, GBAction gbAction) {
            MyDataNode node = (MyDataNode) overviewTreeTable.getValueAt(row, 0);
            if (node != null) {
                return gbAction.matches(node);
            }
            return false;
        }
    }


}
