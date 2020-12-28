package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.data.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsTable;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.RenameView;

import java.util.List;

public class OpenRenameAction extends DumbAwareAction {
    private final GBActionsTable myGBActionsTable;
    private List<GBAction> myGBActions;
    RenameGBAction renameGBAction;

    public OpenRenameAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions) {
        super("Rename Action");
        this.myGBActionsTable = myGBActionsTable;
        this.myGBActions = myGBActions;
    }

    public OpenRenameAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions, RenameGBAction GBAction) {
        super("Rename Action");
        this.myGBActionsTable = myGBActionsTable;
        this.myGBActions = myGBActions;
        this.renameGBAction = GBAction;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        RenameView renameView = renameGBAction == null ? new RenameView() : new RenameView(renameGBAction);
        DialogBuilder builder = new DialogBuilder();
        builder.setCenterPanel(renameView.getContent());
        builder.setTitle("Create Rename Action");
        builder.showModal(true);
        if (builder.getDialogWrapper().isOK()) {
            RenameGBAction newGBAction = renameView.getAction();
            if (renameGBAction != null) {
                myGBActions.set(myGBActions.indexOf(renameGBAction), newGBAction);
                myGBActionsTable.getListTableModel().setItems(myGBActions);
                return;
            }
            for (GBAction act : myGBActions) {
                if (act.getName().equals(newGBAction.getName())) {
                    int confirmed = Messages.showConfirmationDialog(renameView.getContent(), "This action already exists. " +
                            "Do you want to replace it?", "Replace Confirmation", "Replace", "Cancel");
                    if (confirmed == 1) {
                        return;
                    }
                    myGBActions.set(myGBActions.indexOf(act), newGBAction);
                    myGBActionsTable.getListTableModel().setItems(myGBActions);
                    return;
                }
            }
            myGBActionsTable.getListTableModel().setItems(myGBActions);
            myGBActions.add(newGBAction);
        }

    }
}
