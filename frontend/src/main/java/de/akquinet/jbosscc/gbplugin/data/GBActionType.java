package de.akquinet.jbosscc.gbplugin.data;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the type of migration actions
 */
public enum GBActionType {

    @SerializedName("RENAME_ACTION")
    RENAME_ACTION("Rename Action"),

    @SerializedName("COLUMN_RENAME_ACTION")
    COLUMN_RENAME_ACTION("Column Rename Action"),

    @SerializedName("TABLE_RENAME_ACTION")
    TABLE_RENAME_ACTION("Table Rename Action");

    private final String name;

    GBActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
