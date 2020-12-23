package de.akquinet.jbosscc.guttenBasePlugin.migration;

import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;

import java.sql.SQLException;

public class Migration {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public static void migrate(ConnectorRepository connectorRepository) throws SQLException {
        new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        final SchemaCompatibilityIssues schemaCompatibilityIssues = new
                SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
        if (schemaCompatibilityIssues.isSevere()) {
            try {
                throw new SQLException(schemaCompatibilityIssues.toString());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        try {
            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,
                    TARGET);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        new CheckEqualTableDataTool(connectorRepository).checkTableData(SOURCE,
                TARGET);
    }
}
