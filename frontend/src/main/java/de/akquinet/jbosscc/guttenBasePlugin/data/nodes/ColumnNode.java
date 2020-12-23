package de.akquinet.jbosscc.guttenBasePlugin.data.nodes;

import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;

public class ColumnNode extends MyDataNode {

    private final ColumnMetaData columnMetaData;
    public ColumnNode(ColumnMetaData columnMetaData) {
        super(columnMetaData.getColumnName(), null, null, Boolean.FALSE, null);
        this.columnMetaData = columnMetaData;
    }

    public ColumnMetaData getColumnMetaData() {
        return columnMetaData;
    }
}
