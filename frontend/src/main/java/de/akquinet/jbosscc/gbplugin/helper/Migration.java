package de.akquinet.jbosscc.gbplugin.helper;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.gbactions.ChangeTypeGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBActionType;
import de.akquinet.jbosscc.gbplugin.data.gbactions.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.TableNode;
import de.akquinet.jbosscc.gbplugin.mapping.ColumnRenameMapper;
import de.akquinet.jbosscc.gbplugin.mapping.TableRenameMapper;
import de.akquinet.jbosscc.gbplugin.ui.migrate.progressview.ProgressView;
import de.akquinet.jbosscc.gbplugin.utils.UIScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.hints.*;
import de.akquinet.jbosscc.guttenbase.hints.impl.DefaultRepositoryTableFilterHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.meta.ColumnMetaData;
import de.akquinet.jbosscc.guttenbase.meta.TableMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryColumnFilter;
import de.akquinet.jbosscc.guttenbase.repository.RepositoryTableFilter;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Migration extends Thread {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    private final String ERROR_TITLE = "Error!";
    private List<GBAction> gbActions;
    private final ColumnRenameMapper columnRenameMapper;
    private final TableRenameMapper tableRenameMapper;
    private final DefaultColumnTypeMapper columnTypeMapper;
    private final ConnectorRepository connectorRepository;
    private ProgressView progressView;
    List<ColumnMetaData> excludedColumns = new ArrayList<>();
    List<TableMetaData> excludedTables = new ArrayList<>();

    public Migration(ConnectorRepository connectorRepository) {
        gbActions = new ArrayList<>();
        this.connectorRepository = connectorRepository;
        columnRenameMapper = new ColumnRenameMapper();
        tableRenameMapper = new TableRenameMapper();
        columnTypeMapper = new DefaultColumnTypeMapper();
    }

    @Override
    public void run() {
        updateMappers();
        addConnectors();

        //TimingProgressIndicator timeDelegate =  connectorRepository
        //        .getConnectorHint(TARGET, UIScriptExecutorProgressIndicator.class)
        //        .getValue()
        //        .getTimingDelegate();
        try {
            new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), ERROR_TITLE);
        }
        checkCompatibilityIssues();
        try {
            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,
                    TARGET);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), ERROR_TITLE);
        }
        try {
            new CheckEqualTableDataTool(connectorRepository).checkTableData(SOURCE,
                    TARGET);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), ERROR_TITLE);
        }
    }

    private void updateMappers() {
        gbActions.forEach(gbAction -> {
            GBActionType type = gbAction.getGBActionType();
            if (type.equals(GBActionType.RENAME_COLUMN)) {
                ((RenameGBAction) gbAction).setMapper(columnRenameMapper);
            }
            else if (type.equals(GBActionType.RENAME_TABLE)) {
                ((RenameGBAction) gbAction).setMapper(tableRenameMapper);
            }
            else if (type.equals(GBActionType.CHANGE_COLUMN_TYPE)) {
                ((ChangeTypeGBAction) gbAction).setMapper(columnTypeMapper);
            }
            else if (type.equals(GBActionType.EXCLUDE_COLUMN)) {
                excludedColumns.add(((ColumnNode) gbAction.getSource()).getColumnMetaData());
            }
            else if (type.equals(GBActionType.EXCLUDE_TABLE)) {
                excludedTables.add(((TableNode) gbAction.getSource()).getTableMetaData());
            }
            gbAction.execute();
        });
    }

    private void addConnectors() {
        connectorRepository.addConnectorHint(TARGET, new ColumnMapperHint() {
            @Override
            public ColumnMapper getValue() {
                return columnRenameMapper;
            }
        });
        connectorRepository.addConnectorHint(TARGET, new TableMapperHint() {
            @Override
            public TableMapper getValue() {
                return tableRenameMapper;
            }
        });
        connectorRepository.addConnectorHint(TARGET, new ColumnTypeMapperHint() {
            @Override
            public ColumnTypeMapper getValue() {
                return columnTypeMapper;
            }
        });
        connectorRepository.addConnectorHint(SOURCE, new RepositoryColumnFilterHint() {
            @Override
            public RepositoryColumnFilter getValue() {
                return column -> !excludedColumns.contains(column);
            }
        });
        connectorRepository.addConnectorHint(SOURCE, new DefaultRepositoryTableFilterHint() {
            @Override
            public RepositoryTableFilter getValue() {
                return table -> !excludedTables.contains(table);
            }
        });
        connectorRepository.addConnectorHint(SOURCE, new ScriptExecutorProgressIndicatorHint() {
            @Override
            public ScriptExecutorProgressIndicator getValue() {
                return new UIScriptExecutorProgressIndicator(progressView);
            }
        });
        connectorRepository.addConnectorHint(TARGET, new ScriptExecutorProgressIndicatorHint() {
            @Override
            public ScriptExecutorProgressIndicator getValue() {
                return new UIScriptExecutorProgressIndicator(progressView);
            }
        });
    }

    private void checkCompatibilityIssues() {
        final SchemaCompatibilityIssues schemaCompatibilityIssues;
        try {
            schemaCompatibilityIssues = new
                    SchemaComparatorTool(connectorRepository).check(SOURCE, TARGET);
            if (schemaCompatibilityIssues.isSevere()) {
                try {
                    throw new SQLException(schemaCompatibilityIssues.toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    Messages.showErrorDialog(throwables.getMessage(), ERROR_TITLE);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), ERROR_TITLE);
        }
    }

    public void addGBAction(GBAction gbAction) {
        gbActions.add(gbAction);
    }

    public ConnectorRepository getConnectorRepository() {
        return connectorRepository;
    }

    public List<GBAction> getGbActions() {
        return gbActions;
    }

    public ProgressView getProgressView() {
        return progressView;
    }

    public void setProgressView(ProgressView progressView) {
        this.progressView = progressView;
    }

}
