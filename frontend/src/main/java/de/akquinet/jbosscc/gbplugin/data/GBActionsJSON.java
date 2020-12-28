package de.akquinet.jbosscc.gbplugin.data;

import java.util.List;

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
