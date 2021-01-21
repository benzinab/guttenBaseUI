package de.akquinet.jbosscc.gbplugin.data.nodes;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

/**
 * @author siraj
 */
public class ColumnNode extends MyDataNode {

    private final ColumnMetaData columnMetaData;
    private String type;
    public ColumnNode(ColumnMetaData columnMetaData) {
        super(columnMetaData.getColumnName(), null, null, Boolean.FALSE, "COLUMN_RENAME_ACTION", null);
        this.columnMetaData = columnMetaData;
        type = columnMetaData.getColumnTypeName();
    }

    public ColumnMetaData getColumnMetaData() {
        return columnMetaData;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
