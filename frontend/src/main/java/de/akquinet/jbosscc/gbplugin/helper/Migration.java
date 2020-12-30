package de.akquinet.jbosscc.gbplugin.helper;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.data.GBActionType;
import de.akquinet.jbosscc.gbplugin.mappers.ColumnRenameMapper;
import de.akquinet.jbosscc.gbplugin.mappers.TableRenameMapper;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
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
    private ColumnRenameMapper columnRenameMapper;
    private TableRenameMapper tableRenameMapper;
    private final ConnectorRepository connectorRepository;

    public Migration(ConnectorRepository connectorRepository) {
        gbActions = new ArrayList<>();
        this.connectorRepository = connectorRepository;
        columnRenameMapper = new ColumnRenameMapper();
        tableRenameMapper = new TableRenameMapper();
    }

    public void migrate() throws SQLException {
        gbActions.forEach(gbAction -> {
            GBActionType type = gbAction.getGBActionType();
            if (type.equals(GBActionType.COLUMN_RENAME_ACTION)) {
                gbAction.execute(columnRenameMapper);
            }
            else if (type.equals(GBActionType.TABLE_RENAME_ACTION)) {
                gbAction.execute(tableRenameMapper);
            }
        });
        connectorRepository.addConnectorHint("target", new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return columnRenameMapper;
            }
        });
        connectorRepository.addConnectorHint("target", new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return tableRenameMapper;
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
                Messages.showErrorDialog(throwables.getMessage(), "Error!");
            }
        }
        try {
            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,
                    TARGET);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
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
