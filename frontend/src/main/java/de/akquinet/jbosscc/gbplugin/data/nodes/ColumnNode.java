package de.akquinet.jbosscc.gbplugin.data.nodes;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

public class ColumnNode extends MyDataNode {

    private final ColumnMetaData columnMetaData;
    public ColumnNode(ColumnMetaData columnMetaData) {
        super(columnMetaData.getColumnName(), null, null, Boolean.FALSE, "COLUMN_RENAME_ACTION", null);
        this.columnMetaData = columnMetaData;
    }

    public ColumnMetaData getColumnMetaData() {
        return columnMetaData;
    }
}
