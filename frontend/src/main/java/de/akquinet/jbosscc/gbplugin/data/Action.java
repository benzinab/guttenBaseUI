package de.akquinet.jbosscc.gbplugin.data;

/**
 * Represents a configuration step for the  migration
 * applied on the database elements to migrate.
 */
public class Action {

    /**
     * The name of the action.
     */
    private String name;

    /**
     * The regular expression of the action.
     */
    private String regex;

    /**
     * the source name of the action.
     */
    private String replace;

    /**
     * The type of the action.
     */
    private ActionType actionType;

    public Action(String name, String regExp, ActionType type) {
        this.name = name;
        this.regex = regExp;
        this.actionType = type;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
