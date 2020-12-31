package de.akquinet.jbosscc.gbplugin.data.gbactions;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the type of migration actions
 */
public enum GBActionType {

    @SerializedName("RENAME")
    RENAME("Rename"),

    @SerializedName("RENAME_COLUMN")
    RENAME_COLUMN("Rename Column"),

    @SerializedName("TABLE_RENAME")
    RENAME_TABLE("Rename Table"),

    @SerializedName("CHANGE_COLUMN_TYPE_ACTION")
    CHANGE_COLUMN_TYPE("Change Column Type");


    private final String name;

    GBActionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
