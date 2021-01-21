package de.akquinet.jbosscc.gbplugin.data.nodes;

import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;

import java.sql.SQLException;

/**
 * @author siraj
 */
public class DatabaseNode extends MyDataNode {

    private final DatabaseMetaData databaseMetaData;

    public DatabaseNode(DatabaseMetaData databaseMetaData) throws SQLException {
        super(databaseMetaData.getDatabaseMetaData().getDatabaseProductName(), databaseMetaData.getTableMetaData().size(), null, Boolean.FALSE, "", null);
        this.databaseMetaData = databaseMetaData;
    }

    public DatabaseMetaData getDatabaseMetaData() {
        return databaseMetaData;
    }
}
