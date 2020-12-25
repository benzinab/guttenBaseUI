package de.akquinet.jbosscc.gbplugin.ui.gbactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.*;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.data.Action;
import de.akquinet.jbosscc.gbplugin.data.ActionType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GBActionsUI {
    private final JPanel myRoot;
    private final JLabel myCountLabel;
    private final GBActionsTable myGBActionsTable;
    private final List<AnAction> myAddActions = new ArrayList<>();
    private ToolbarDecorator decorator;
    private List<Action> myActions;

    public GBActionsUI(List<Action> actions) {
        myActions = actions;
        myGBActionsTable = new GBActionsTable(actions, createInfoColumns());
        decorator = ToolbarDecorator.createDecorator(myGBActionsTable);
        createActions(decorator);

        myRoot = new JPanel(new BorderLayout());
        myRoot.add(new TitledSeparator("actions places"), BorderLayout.NORTH);
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        myCountLabel = new JLabel();
        myCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        myCountLabel.setForeground(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES.getFgColor());
        myRoot.add(myCountLabel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return decorator.createPanel();
    }
    private void createActions(ToolbarDecorator decorator) {
        myAddActions.add(new CreateRenameAction());
        myAddActions.add(new CreateRenameAction());//TODO add other hints
        myAddActions.add(new CreateRenameAction());
        myAddActions.add(new CreateRenameAction());
        myAddActions.sort((o1, o2) -> Comparing.compare(o1.getTemplatePresentation().getText(), o2.getTemplatePresentation().getText()));
        decorator.disableUpDownActions();
        decorator.setAddActionUpdater(e -> !myAddActions.isEmpty());
        decorator.setAddAction(this::performAdd);
        decorator.setRemoveAction(this::performRemove);
        decorator.setEditActionUpdater(e -> myGBActionsTable.getSelectedRows().length == 1);
        decorator.setEditAction(this::performEdit);
    }

    private void performAdd(AnActionButton e) {
        DefaultActionGroup group = new DefaultActionGroup(myAddActions);
        JBPopupFactory.getInstance()
                .createActionGroupPopup("Add New Action", group, e.getDataContext(),
                        JBPopupFactory.ActionSelectionAid.NUMBERING, true, null, -1)
                .show(e.getPreferredPopupPoint());
    }

    private void performRemove(AnActionButton e) {
        int confirmed = Messages.showConfirmationDialog(myGBActionsTable, "Do you really want to delete this item?",
                "Confirmation", "Delete", "Cancel");
        if (confirmed == 1) {
            return;
        }
        final int selectedRow = myGBActionsTable.getSelectedRow();
        if (selectedRow < 0) return;
        final List<Action> selected = getSelectedActions();
        for (Action action : selected) {
            myActions.remove(action);
        }
        myGBActionsTable.getListTableModel().setItems(myActions);
        final int index = Math.min(myGBActionsTable.getListTableModel().getRowCount() - 1, selectedRow);
        myGBActionsTable.getSelectionModel().setSelectionInterval(index, index);
        TableUtil.scrollSelectionToVisible(myGBActionsTable);
    }

    private void performEdit(AnActionButton e) {
        int row = myGBActionsTable.getSelectedRow();
        Action action = myGBActionsTable.getItems().get(myGBActionsTable.convertRowIndexToModel(row));
        new CreateRenameAction(action).actionPerformed(null);

    }

    private List<Action> getSelectedActions() {
        List<Action> toRemove = new ArrayList<>();
        for (int row : myGBActionsTable.getSelectedRows()) {
            toRemove.add(myGBActionsTable.getItems().get(myGBActionsTable.convertRowIndexToModel(row)));
        }
        return toRemove;
    }


    public ColumnInfo[] createInfoColumns() {
        final ColumnInfo[] columnInfos = {new ColumnInfo<Action, String>("Name") {
            @Override
            public @Nullable String valueOf(Action action) {
                return action.getName();
            }
        }, new ColumnInfo< Action, ActionType>(("Type")) {
            @Override
            public ActionType valueOf(final Action action) {
                return action.getActionType();
            }
        }};
        return columnInfos;
    }

    private class CreateRenameAction extends DumbAwareAction {
        Action action;
        CreateRenameAction() {
            super("Rename Action");
        }
        CreateRenameAction(Action action) {
            super("Rename Action");
            this.action = action;
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            RenameView renameView = action == null ? new RenameView() : new RenameView(action);
            DialogBuilder builder = new DialogBuilder();
            builder.setCenterPanel(renameView.getContent());
            builder.setTitle("Create Rename Action");
            builder.showModal(true);
            if (builder.getDialogWrapper().isOK()) {
                Action newAction = renameView.getAction();
                if (action != null) {
                    myActions.set(myActions.indexOf(action), newAction);
                    myGBActionsTable.getListTableModel().setItems(myActions);
                    return;
                }
                for (Action act : myActions) {
                    if (act.getName().equals(newAction.getName())) {
                        int confirmed = Messages.showConfirmationDialog(renameView.getContent(), "This action already exists. " +
                                "Do you want to replace it?","Replace Confirmation", "Replace", "Cancel");
                        if (confirmed == 1) {
                            return;
                        }
                        myActions.set(myActions.indexOf(act), newAction);
                        myGBActionsTable.getListTableModel().setItems(myActions);
                        return;
                    }
                }
                myGBActionsTable.getListTableModel().setItems(myActions);
                myActions.add(newAction);
            }

        }
    }

    public List<Action> getActions() {
        return myActions;
    }


}

