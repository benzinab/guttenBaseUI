package de.akquinet.jbosscc.gbplugin.ui.showactions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.*;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.gbplugin.actions.CreateRenameAction;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
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
    private List<GBAction> myGBActions;

    public GBActionsUI(List<GBAction> GBActions) {
        myGBActions = GBActions;
        myGBActionsTable = new GBActionsTable(GBActions, createInfoColumns());
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
        myAddActions.add(new CreateRenameAction(myGBActionsTable, myGBActions));
        myAddActions.add(new CreateRenameAction(myGBActionsTable, myGBActions));//TODO add other hints
        myAddActions.add(new CreateRenameAction(myGBActionsTable, myGBActions));
        myAddActions.add(new CreateRenameAction(myGBActionsTable, myGBActions));
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
        GBAction GBAction = myGBActionsTable.getItems().get(myGBActionsTable.convertRowIndexToModel(row));
        new CreateRenameAction(myGBActionsTable, myGBActions, GBAction).actionPerformed(null);

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
        }, new ColumnInfo<GBAction, ActionType>(("Type")) {
            @Override
            public ActionType valueOf(final GBAction action) {
                return action.getActionType();
            }
        }};
        return columnInfos;
    }

    public List<GBAction> getActions() {
        return myGBActions;
    }


}

