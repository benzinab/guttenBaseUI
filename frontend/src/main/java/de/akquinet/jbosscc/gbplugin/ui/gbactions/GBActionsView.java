package de.akquinet.jbosscc.gbplugin.ui.gbactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.*;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.actions.AddExcludeColumnAction;
import de.akquinet.jbosscc.gbplugin.actions.AddExcludeTableAction;
import de.akquinet.jbosscc.gbplugin.actions.OpenChangeTypeAction;
import de.akquinet.jbosscc.gbplugin.actions.OpenRenameAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.ChangeTypeGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBActionType;
import de.akquinet.jbosscc.gbplugin.data.gbactions.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.helper.GsonHelper;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GBActionsView extends AbstractView {
    private JPanel content;
    private JButton cancelButton;
    private JButton saveButton;
    private JScrollPane scrollPane1;
    private JPanel actionsPanel;
    private List<GBAction> gbActions;
    private GBActionsTable myGBActionsTable;
    private List<AnAction> myAddActions;
    private ToolbarDecorator decorator;

    public GBActionsView() {
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> close(content));
    }

    private void createUIComponents() {
        gbActions = new ArrayList<>();
        try {
            gbActions = GsonHelper.importJSON("actions.json");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "Error!");
        }
        init();
        actionsPanel = decorator.createPanel();
    }

    private void init() {
        myGBActionsTable = new GBActionsTable(gbActions, createInfoColumns());
        decorator = ToolbarDecorator.createDecorator(myGBActionsTable);
        createActions(decorator);

        JPanel myRoot = new JPanel(new BorderLayout());
        myRoot.add(new TitledSeparator("actions places"), BorderLayout.NORTH);
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        JLabel myCountLabel = new JLabel();
        myCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        myCountLabel.setForeground(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES.getFgColor());
        myRoot.add(myCountLabel, BorderLayout.SOUTH);
    }

    private void createActions(ToolbarDecorator decorator) {
        myAddActions = new ArrayList<>();
        myAddActions.add(new OpenRenameAction(myGBActionsTable, gbActions));
        myAddActions.add(new OpenChangeTypeAction(myGBActionsTable, gbActions));
        myAddActions.add(new AddExcludeColumnAction(myGBActionsTable, gbActions));
        myAddActions.add(new AddExcludeTableAction(myGBActionsTable, gbActions));
        myAddActions.sort((o1, o2) -> Comparing.compare(o1.getTemplatePresentation().getText(), o2.getTemplatePresentation().getText()));
        decorator.disableUpDownActions();
        decorator.setAddActionUpdater(e -> !myAddActions.isEmpty());
        decorator.setAddAction(this::performAdd);
        decorator.setRemoveAction(this::performRemove);
        decorator.setEditActionUpdater(this::enableEdit);
        decorator.setEditAction(this::performEdit);
    }

    public ColumnInfo<?, ?>[] createInfoColumns() {
        return new ColumnInfo<?, ?>[]{new ColumnInfo<GBAction, String>("Name") {
            @Override
            public @Nullable String valueOf(GBAction action) {
                return action.getName();
            }
        }, new ColumnInfo<GBAction, String>(("Type")) {
            @Override
            public String valueOf(final GBAction action) {
                return action.getGBActionType().getName();
            }
        }};
    }

    private void performAdd(AnActionButton e) {
        DefaultActionGroup group = new DefaultActionGroup(myAddActions);
        JBPopupFactory.getInstance()
                .createActionGroupPopup("Add New Action", group, e.getDataContext(),
                        JBPopupFactory.ActionSelectionAid.NUMBERING, true, null, -1)
                .show(Objects.requireNonNull(e.getPreferredPopupPoint()));
    }

    private void performRemove(AnActionButton e) {
        int confirmed = Messages.showConfirmationDialog(myGBActionsTable, "Do you really want to delete the selected item(s)?",
                "Confirmation", "Delete", "Cancel");
        if (confirmed == 1) {
            return;
        }
        final int selectedRow = myGBActionsTable.getSelectedRow();
        if (selectedRow < 0) return;
        final List<GBAction> selected = getSelectedActions();
        for (GBAction GBAction : selected) {
            gbActions.remove(GBAction);
        }
        myGBActionsTable.getListTableModel().setItems(gbActions);
        final int index = Math.min(myGBActionsTable.getListTableModel().getRowCount() - 1, selectedRow);
        myGBActionsTable.getSelectionModel().setSelectionInterval(index, index);
        TableUtil.scrollSelectionToVisible(myGBActionsTable);
    }

    private void performEdit(AnActionButton e) {
        int row = myGBActionsTable.getSelectedRow();
        GBAction gbAction = myGBActionsTable.getItems().get(myGBActionsTable.convertRowIndexToModel(row));
        switch (gbAction.getGBActionType()) {
            case RENAME:
            case RENAME_COLUMN:
            case RENAME_TABLE:
                new OpenRenameAction(myGBActionsTable, gbActions, (RenameGBAction) gbAction).actionPerformed(null);
                break;
            case CHANGE_COLUMN_TYPE:
                new OpenChangeTypeAction(myGBActionsTable, gbActions, (ChangeTypeGBAction) gbAction).actionPerformed(null);
        }

    }

    private boolean enableEdit(@NotNull AnActionEvent anActionEvent){
        return myGBActionsTable.getSelectedRows().length == 1
                && !gbActions.get(myGBActionsTable.getSelectedRow()).getGBActionType().equals(GBActionType.EXCLUDE_COLUMN)
                && !gbActions.get(myGBActionsTable.getSelectedRow()).getGBActionType().equals(GBActionType.EXCLUDE_TABLE);
    }

    public void save() {
        String path;
        try {
            path = GsonHelper.exportJSON(gbActions, "actions.json");
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "Error While Saving Actions!");
            return;
        }
        close(content);
        Messages.showInfoMessage("Actions are successfully saved in: " + path, "File Saved!");
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
}
