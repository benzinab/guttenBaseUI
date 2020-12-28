package de.akquinet.jbosscc.gbplugin.ui.migrate.overview;

import com.intellij.database.psi.DbDataSource;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.DumbAwareActionButton;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.PlatformIcons;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.DatabaseNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.TableNode;
import de.akquinet.jbosscc.gbplugin.helper.GsonHelper;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.gbplugin.ui.migrate.resultview.ResultView;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
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
    private OverviewTreeTable overviewTreeTable;
    private ToolbarDecorator decorator;
    private JPanel myRoot;
    private JLabel myCountLabel;

    private final List<DbDataSource> dataSources;
    private Migration migration;
    private DatabaseMetaData metaData;
    private List<GBAction> myGBActions;

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public OverView(Migration migration, List<DbDataSource> dataSources) {
        this.dataSources = dataSources;
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

    }

    public void next() {
            ResultView resultView = new ResultView(migration);
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
            Container contentPane = parent.getContentPane();
            contentPane.add(resultView.getContent(), "3");
            CardLayout cl = (CardLayout) (contentPane.getLayout());
            cl.show(contentPane, "3");
            SwingUtilities.updateComponentTreeUI(parent);
    }

    public void fillData() {
        try {
            this.metaData = migration.getConnectorRepository().getDatabaseMetaData("source");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
            return;
        }
        MyDataNode root;
        try {
            root = new DatabaseNode(metaData);
        } catch (SQLException throwables) {
            Messages.showErrorDialog("Cannot get database name!", "Error!");
            throwables.printStackTrace();
            return;
        }
        MyDataNode finalRoot = root;
        metaData.getTableMetaData().forEach(table -> {
                TableNode tableNode = new TableNode(table);
                table.getColumnMetaData().forEach(column -> tableNode.addNode(new ColumnNode(column)));
                finalRoot.addNode(tableNode);
            });

        OverviewTreeTableModel treeTableModel = new OverviewTreeTableModel(root);
        overviewTreeTable = new OverviewTreeTable(treeTableModel);

        decorator = ToolbarDecorator.createDecorator(overviewTreeTable);
        createActions(decorator);

        myRoot = new JPanel(new BorderLayout());
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        myCountLabel = new JLabel();
        myCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        myCountLabel.setForeground(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES.getFgColor());
        myRoot.add(myCountLabel, BorderLayout.SOUTH);
        decorator.getActionsPanel().setMinimumSize(new Dimension(600, 600));
        tableContainer = decorator.createPanel();
    }

    private void createActions(ToolbarDecorator decorator) {
        try {
            myGBActions = GsonHelper.importJSON("actions.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            myGBActions = new ArrayList<>();
        }
        myGBActions.forEach(gbAction -> {
            OverviewAddAction action = new OverviewAddAction(gbAction);
            decorator.addExtraAction(action);
        });

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
            int row = overviewTreeTable.getSelectedRow();
            MyDataNode node = (MyDataNode) overviewTreeTable.getValueAt(row, 0);
            int confirmed = Messages.showConfirmationDialog(overviewTreeTable,
                    String.format("Do you really want to add the action %s to the database element %s", gbAction.getName(), node.getName()),
                    "Confirmation",
                    "Add",
                    "Cancel"
                    );
            if (confirmed == 1) {
                return;
            }
            gbAction.setSource(node);
            migration.addGBAction(gbAction);
        }

        @Override
        public boolean displayTextInToolbar() {
            return true; // show action names
        }

        private boolean isEnabled(GBAction gbAction) {
            if (overviewTreeTable.getSelectedRows().length>1) {
                return false;
            }
            int row = overviewTreeTable.getSelectedRow();
            String type = (String) overviewTreeTable.getValueAt(row, 1);
            return gbAction.getGBActionType() != null && type != null && gbAction.getGBActionType().toString() == type;
        }
    }
}
