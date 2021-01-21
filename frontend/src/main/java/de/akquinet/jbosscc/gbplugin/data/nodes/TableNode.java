package de.akquinet.jbosscc.gbplugin.data.nodes;


import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;

/**
 * @author siraj
 */
public class TableNode extends MyDataNode {

    private final TableMetaData tableMetaData;

    public TableNode(TableMetaData tableMetaData) {
        super(tableMetaData.getTableName(), tableMetaData.getColumnCount(), null, Boolean.FALSE, "", null);
        this.tableMetaData = tableMetaData;
    }

    public TableMetaData getTableMetaData() {
        return tableMetaData;
    }

}
