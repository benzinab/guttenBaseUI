package de.akquinet.jbosscc.gbplugin.data;

import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;

import java.util.List;

/**
 * @author siraj
 */
public class GBActionsJSON {

    public GBActionsJSON(List<GBAction> gbActions) {
        this.gbActions = gbActions;
    }

    private List<GBAction> gbActions;

    public List<GBAction> getGbActions() {
        return gbActions;
    }

    public void setGbActions(List<GBAction> gbActions) {
        this.gbActions = gbActions;
    }
}
