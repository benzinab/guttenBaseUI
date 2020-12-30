package de.akquinet.jbosscc.gbplugin.data;

import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.mappers.Mapper;

/**
 * Represents a configuration step for the  migration
 * applied on the database elements to migrate.
 */
public abstract class GBAction {

    /**
     * The name of the action.
     */
    private String name;

    /**
     * Explains the Action.
     */
    private String description;

    /**
     * The source database element of the action
     */
    private MyDataNode source;

    /**
     * The type of the action.
     */
    private GBActionType gbActionType;


    public GBAction(String name, GBActionType gbActionType) {
        this.name = name;
        this.gbActionType = gbActionType;
    }

    public GBAction(String name, GBActionType gbActionType, String description) {
        this.name = name;
        this.description = description;
        this.gbActionType = gbActionType;
    }

    public GBAction(GBAction gbAction, MyDataNode node) {
        this.name = gbAction.getName();
        this.description = gbAction.getDescription();
        this.gbActionType = gbAction.getGBActionType();
        this.source = node;
    }

    public boolean matches(MyDataNode node) {
        return false;
    }

    public void execute(Mapper renameMapper) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyDataNode getSource() {
        return source;
    }

    public void setSource(MyDataNode source) {
        this.source = source;
    }

    public GBActionType getGBActionType() {
        return gbActionType;
    }

    public void setGBActionType(GBActionType GBActionType) {
        this.gbActionType = GBActionType;
    }
}
