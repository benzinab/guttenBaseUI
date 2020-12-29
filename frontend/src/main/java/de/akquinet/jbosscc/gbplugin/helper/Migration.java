package de.akquinet.jbosscc.gbplugin.helper;

import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.mappers.ColumnRenameMapper;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Migration {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    private List<GBAction> gbActions;
    private ColumnRenameMapper renameMapper;
    private final ConnectorRepository connectorRepository;

    public Migration(ConnectorRepository connectorRepository) {
        gbActions = new ArrayList<>();
        this.connectorRepository = connectorRepository;
        renameMapper = new ColumnRenameMapper();
    }

    public void migrate() throws SQLException {
        gbActions.forEach(gbAction -> gbAction.execute(renameMapper));
        connectorRepository.addConnectorHint("target", new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return renameMapper;
            }
        });
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

    public void addGBAction(GBAction gbAction) {
        //todo add action for real!! hh
        gbActions.add(gbAction);
    }

    public ConnectorRepository getConnectorRepository() {
        return connectorRepository;
    }

    public List<GBAction> getGbActions() {
        return gbActions;
    }
}
