package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.gbactions.ExcludeColumnGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddExcludeColumnAction extends DumbAwareAction {
    private final GBActionsTable myGBActionsTable;
    private List<GBAction> myGBActions;
    public AddExcludeColumnAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions) {
        super("Exclude Column");
        this.myGBActionsTable = myGBActionsTable;
        this.myGBActions = myGBActions;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ExcludeColumnGBAction newGBAction = new ExcludeColumnGBAction();
        for (GBAction act : myGBActions) {
            if (act.getName().equals(newGBAction.getName())) {
                Messages.showErrorDialog("This action already exists!", "Error" );
                return;
            }
        }
        myGBActions.add(newGBAction);
        myGBActionsTable.getListTableModel().setItems(myGBActions);
    }
}
