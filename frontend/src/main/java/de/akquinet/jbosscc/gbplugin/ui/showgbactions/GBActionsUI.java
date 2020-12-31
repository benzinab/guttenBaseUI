package de.akquinet.jbosscc.gbplugin.ui.showgbactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.*;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.actions.OpenChangeTypeAction;
import de.akquinet.jbosscc.gbplugin.actions.OpenRenameAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.ChangeTypeGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBActionType;
import de.akquinet.jbosscc.gbplugin.data.gbactions.RenameGBAction;
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
    private List<GBAction> myGBActions;

    public GBActionsUI(List<GBAction> gbActions) {
        myGBActions = gbActions;
        myGBActionsTable = new GBActionsTable(gbActions, createInfoColumns());
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
        myAddActions.add(new OpenRenameAction(myGBActionsTable, myGBActions));
        myAddActions.add(new OpenChangeTypeAction(myGBActionsTable, myGBActions));
        //TODO add other hints
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
        final List<GBAction> selected = getSelectedActions();
        for (GBAction GBAction : selected) {
            myGBActions.remove(GBAction);
        }
        myGBActionsTable.getListTableModel().setItems(myGBActions);
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
                new OpenRenameAction(myGBActionsTable, myGBActions, (RenameGBAction) gbAction).actionPerformed(null);
            case CHANGE_COLUMN_TYPE:
                new OpenChangeTypeAction(myGBActionsTable, myGBActions, (ChangeTypeGBAction) gbAction).actionPerformed(null);
        }

    }

    private List<GBAction> getSelectedActions() {
        List<GBAction> toRemove = new ArrayList<>();
        for (int row : myGBActionsTable.getSelectedRows()) {
            toRemove.add(myGBActionsTable.getItems().get(myGBActionsTable.convertRowIndexToModel(row)));
        }
        return toRemove;
    }


    public ColumnInfo[] createInfoColumns() {
        final ColumnInfo[] columnInfos = {new ColumnInfo<GBAction, String>("Name") {
            @Override
            public @Nullable String valueOf(GBAction action) {
                return action.getName();
            }
        }, new ColumnInfo<GBAction, GBActionType>(("Type")) {
            @Override
            public GBActionType valueOf(final GBAction action) {
                return action.getGBActionType();
            }
        }};
        return columnInfos;
    }

    public List<GBAction> getActions() {
        return myGBActions;
    }


}

