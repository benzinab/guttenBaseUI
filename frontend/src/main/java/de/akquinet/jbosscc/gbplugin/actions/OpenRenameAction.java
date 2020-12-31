package de.akquinet.jbosscc.gbplugin.actions;

import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsTable;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.RenameView;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.View;

import java.util.List;

public class OpenRenameAction extends OpenGBAction {

    public OpenRenameAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions) {
        super(myGBActionsTable, myGBActions, "Rename Action");
    }

    public OpenRenameAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions, RenameGBAction renameGBAction) {
        super(myGBActionsTable, myGBActions, "Rename Action", renameGBAction);
    }

    @Override
    public View createView(GBAction gbAction) {
        return gbAction == null ? new RenameView() : new RenameView((RenameGBAction) gbAction);
    }
}
