package de.akquinet.jbosscc.gbplugin.actions;

import de.akquinet.jbosscc.gbplugin.data.gbactions.ChangeTypeGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.ChangeTypeView;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsTable;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.View;

import java.util.List;

public class OpenChangeTypeAction extends OpenGBAction {

    public OpenChangeTypeAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions) {
        super(myGBActionsTable, myGBActions, "Change Column Type Action");
    }

    public OpenChangeTypeAction(GBActionsTable myGBActionsTable, List<GBAction> myGBActions, ChangeTypeGBAction changeTypeGBAction) {
        super(myGBActionsTable, myGBActions, "Change Column Type Action", changeTypeGBAction);
    }

    @Override
    public View createView(GBAction gbAction) {
        return gbAction == null ? new ChangeTypeView() : new ChangeTypeView((ChangeTypeGBAction) gbAction);
    }
}
