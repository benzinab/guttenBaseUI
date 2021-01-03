package de.akquinet.jbosscc.gbplugin.data.gbactions;

import de.akquinet.jbosscc.gbplugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;

public class ExcludeColumnGBAction extends GBAction {
    private final String NAME = "Exclude Column";
    private final String DESCRIPTION = "After applying this action to a column, the  selected column will not be copied to the target database.";
    public ExcludeColumnGBAction() {
        //TODO sauber
        super("Exclude Column", GBActionType.EXCLUDE_COLUMN, "After applying this action to a column, " +
                "the  selected column will not be copied to the target database.") ;
    }

    public ExcludeColumnGBAction(GBAction gbAction, MyDataNode node) {
        super(gbAction, node);
    }

    @Override
    public boolean matches(MyDataNode node) {
        return node instanceof ColumnNode;
    }

    @Override
    public void execute() {
    }

}
