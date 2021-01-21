package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.gbactions.ExcludeTableGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.ui.gbactions.GBActionsTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a DumbAwareAction to add a filter action the gbActionTable {@link GBActionsTable}.
 * @author siraj
 */
public class AddExcludeTableAction extends DumbAwareAction {
    private final GBActionsTable myGBActionsTable;
    private List<GBAction> myGBActions;
    public AddExcludeTableAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions) {
        super("Exclude Table");
        this.myGBActionsTable = myGBActionsTable;
        this.myGBActions = myGBActions;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ExcludeTableGBAction newGBAction = new ExcludeTableGBAction();
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
