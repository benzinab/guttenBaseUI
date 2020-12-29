package de.akquinet.jbosscc.gbplugin.ui.migrate.resultview;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.TableUtil;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.data.GBActionType;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultView extends AbstractView {

    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JButton cancelButton;
    private JButton submitButton;
    private JButton backButton;
    private JPanel actionsPanel;

    private final Migration migration;
    private GBActionsTable myGBActionsTable;
    private ToolbarDecorator decorator;
    private JPanel myRoot;

    public ResultView(Migration migration) {
        this.migration = migration;
        backButton.addActionListener(e -> backTo(content, "2"));
        cancelButton.addActionListener(e -> close(content));
        submitButton.addActionListener(e -> submit(e));


    }

    public void submit(ActionEvent e) {
        migration.getGbActions().forEach(gbAction -> System.out.println(gbAction.getName() + " " + gbAction.getSource().getName()));

        try {
            migration.migrate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //DataContext dataContext = DataManager.getInstance().getDataContext();
        //Project project = (Project) dataContext.getData(DataConstants.PROJECT);
        //ProgressManager.getInstance().run(new Task.Backgroundable(project, "Migration progress") {
        //    @Override
        //    public void run(@NotNull ProgressIndicator indicator) {
        //        indicator.setFraction(0.1);
        //        indicator.setText("Migrating is starting.");
        //        indicator.setFraction(0.9);
        //        indicator.setText("Migrating has been successfully done.");
        //          //
        //    }
        //});
        close(content);
    }

    public ColumnInfo[] createInfoColumns() {
        final ColumnInfo[] columnInfos = {new ColumnInfo<GBAction, String>("Name") {
            @Override
            public @Nullable String valueOf(GBAction action) {
                return action.getName();
            }
        }, new ColumnInfo<GBAction, String>(("Source")) {
            @Override
            public String valueOf(final GBAction action) {
                return action.getSource().getName();
            }
        }, new ColumnInfo<GBAction, GBActionType>(("Type")) {
            @Override
            public GBActionType valueOf(final GBAction action) {
                return action.getGBActionType();
            }
        }};
        return columnInfos;
    }


    private void createActions(ToolbarDecorator decorator) {       decorator.disableUpDownActions();
        decorator.setAddAction(this::performAdd);
        decorator.setRemoveAction(this::performRemove);
    }

    private void performAdd(AnActionButton e) {
        backTo(content, "2");
    }

    private void performRemove(AnActionButton e) {
        int confirmed = Messages.showConfirmationDialog(myGBActionsTable, "Do you really want to delete this item?",
                "Confirmation", "Delete", "Cancel");
        if (confirmed == 1) {
            return;
        }

        final int selectedRow = myGBActionsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        final List<GBAction> selected = getSelectedActions();
        for (GBAction GBAction : selected) {
            migration.getGbActions().remove(GBAction);
        }
        myGBActionsTable.getListTableModel().setItems(migration.getGbActions());
        final int index = Math.min(myGBActionsTable.getListTableModel().getRowCount() - 1, selectedRow);
        myGBActionsTable.getSelectionModel().setSelectionInterval(index, index);
        TableUtil.scrollSelectionToVisible(myGBActionsTable);
    }

    private List<GBAction> getSelectedActions() {
        List<GBAction> toRemove = new ArrayList<>();
        for (int row : myGBActionsTable.getSelectedRows()) {
            toRemove.add(myGBActionsTable.getItems().get(myGBActionsTable.convertRowIndexToModel(row)));
        }
        return toRemove;
    }


    public JPanel getContent() {
        return content;
    }

    private void createUIComponents() {
        myGBActionsTable = new GBActionsTable(migration.getGbActions(), createInfoColumns());
        decorator = ToolbarDecorator.createDecorator(myGBActionsTable);
        createActions(decorator);

        myRoot = new JPanel(new BorderLayout());
        myRoot.add(new TitledSeparator("Actions Places"), BorderLayout.NORTH);
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        actionsPanel = decorator.createPanel();
    }
}
