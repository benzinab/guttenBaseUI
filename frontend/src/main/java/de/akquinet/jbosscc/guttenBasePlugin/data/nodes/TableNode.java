package de.akquinet.jbosscc.guttenBasePlugin.data.nodes;


import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

public class TableNode extends MyDataNode {

    private final TableMetaData tableMetaData;

    public TableNode(TableMetaData tableMetaData) {
        super(tableMetaData.getTableName(), tableMetaData.getColumnCount(), null, Boolean.FALSE, null);
        this.tableMetaData = tableMetaData;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

}
