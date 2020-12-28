package de.akquinet.jbosscc.gbplugin.data;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the type of migration actions
 */
public enum GBActionType {
    @SerializedName("COLUMN_RENAME_ACTION")
    COLUMN_RENAME_ACTION("COLUMN_RENAME_ACTION"),

    @SerializedName("TABLE_RENAME_ACTION")
    TABLE_RENAME_ACTION("TABLE_RENAME_ACTION"),

    @SerializedName("SCHEMA_ACTION")
    SCHEMA_ACTION("SCHEMA_ACTION");

    private final String name;

    GBActionType(String name) {
        this.name = name;
    }
}
