package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsTable;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.View;

import java.util.List;

public abstract class OpenGBAction extends DumbAwareAction {

    private final GBActionsTable myGBActionsTable;
    private List<GBAction> myGBActions;
    private View view;
    private GBAction gbAction;


    public OpenGBAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions, String title) {
        super(title);
        this.myGBActionsTable = myGBActionsTable;
        this.myGBActions = myGBActions;
    }

    public OpenGBAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions, String title, GBAction gbAction) {
        super(title);
        this.myGBActionsTable = myGBActionsTable;
        this.myGBActions = myGBActions;
        this.gbAction = gbAction;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        view = createView(gbAction);
        DialogBuilder builder = new DialogBuilder();
        builder.setCenterPanel(view.getContent());
        builder.setTitle("Rename Action");
        builder.showModal(true);
        if (builder.getDialogWrapper().isOK()) {
            GBAction newGBAction = view.getAction();
            if (gbAction != null) {
                myGBActions.set(myGBActions.indexOf(gbAction), newGBAction);
                myGBActionsTable.getListTableModel().setItems(myGBActions);
                return;
            }
            for (GBAction act : myGBActions) {
                if (act.getName().equals(newGBAction.getName())) {
                    int confirmed = Messages.showConfirmationDialog(view.getContent(), "This action already exists. " +
                            "Do you want to replace it?", "Replace Confirmation", "Replace", "Cancel");
                    if (confirmed == 1) {
                        return;
                    }
                    myGBActions.set(myGBActions.indexOf(act), newGBAction);
                    myGBActionsTable.getListTableModel().setItems(myGBActions);
                    return;
                }
            }
            myGBActions.add(newGBAction);
            myGBActionsTable.getListTableModel().setItems(myGBActions);
        }

    }
    public abstract View createView(GBAction gbAction);
}
