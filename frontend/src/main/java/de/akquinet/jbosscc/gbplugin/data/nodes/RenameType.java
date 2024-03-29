package de.akquinet.jbosscc.gbplugin.data.nodes;

/**
 * @author siraj
 */
public enum RenameType {
    REPLACE("replace"),
    ADD_PREFIX("add prefix"),
    ADD_SUFFIX("add suffix");

    private String name;

    RenameType(String name) {
        this.name = name;
    }
}
