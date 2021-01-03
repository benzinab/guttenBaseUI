package de.akquinet.jbosscc.gbplugin.data.gbactions;

import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.TableNode;

public class ExcludeTableGBAction extends GBAction{
    public ExcludeTableGBAction() {
        super("Exclude Table", GBActionType.EXCLUDE_TABLE, "After applying this action to a column, " +
                "the  selected column will not be copied to the target database.");
    }


    public ExcludeTableGBAction(GBAction gbAction, MyDataNode node) {
        super(gbAction, node);
    }

    @Override
    public boolean matches(MyDataNode node) {
        return node instanceof TableNode;
    }

    @Override
    public void execute() {
    }

}
