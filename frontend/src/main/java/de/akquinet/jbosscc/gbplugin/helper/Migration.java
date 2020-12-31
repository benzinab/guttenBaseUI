package de.akquinet.jbosscc.gbplugin.helper;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.gbactions.ChangeTypeGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBActionType;
import de.akquinet.jbosscc.gbplugin.data.gbactions.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.mapping.ColumnRenameMapper;
import de.akquinet.jbosscc.gbplugin.mapping.TableRenameMapper;
import de.akquinet.jbosscc.gbplugin.utils.UIScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.hints.ColumnMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ColumnTypeMapperHint;
import de.akquinet.jbosscc.guttenbase.hints.ScriptExecutorProgressIndicatorHint;
import de.akquinet.jbosscc.guttenbase.hints.TableMapperHint;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnMapper;
import de.akquinet.jbosscc.guttenbase.mapping.ColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.DefaultColumnTypeMapper;
import de.akquinet.jbosscc.guttenbase.mapping.TableMapper;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.tools.CheckEqualTableDataTool;
import de.akquinet.jbosscc.guttenbase.tools.DefaultTableCopyTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.CopySchemaTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaComparatorTool;
import de.akquinet.jbosscc.guttenbase.tools.schema.comparison.SchemaCompatibilityIssues;
import de.akquinet.jbosscc.guttenbase.utils.LoggingScriptExecutorProgressIndicator;
import de.akquinet.jbosscc.guttenbase.utils.ScriptExecutorProgressIndicator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Migration extends SwingWorker<Void, Void> {

    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    private final String ERROR_TITLE = "Error!";
    private List<GBAction> gbActions;
    private ColumnRenameMapper columnRenameMapper;
    private TableRenameMapper tableRenameMapper;
    private DefaultColumnTypeMapper columnTypeMapper;
    private final ConnectorRepository connectorRepository;
    public Migration(ConnectorRepository connectorRepository) {
        gbActions = new ArrayList<>();
        this.connectorRepository = connectorRepository;
        columnRenameMapper = new ColumnRenameMapper();
        tableRenameMapper = new TableRenameMapper();
        columnTypeMapper = new DefaultColumnTypeMapper();
    }

    public void migrate() throws SQLException {
        BasicConfigurator.configure();
        Logger logger = Logger.getLogger(LoggingScriptExecutorProgressIndicator.class);

        JFrame frame = new JFrame("LoggingWindow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.
        JTextArea newContentPane = new JTextArea(50, 100);
        LoggingAppender appender = new LoggingAppender(newContentPane);
        logger.addAppender(appender);
        logger.setLevel(Level.ALL);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        //Display the window.
        frame.pack();
        frame.setVisible(true);

        updateMappers();
        addConnectors();
        connectorRepository.addConnectorHint(SOURCE, new ScriptExecutorProgressIndicatorHint() {
            @Override
            public ScriptExecutorProgressIndicator getValue() {
                return new UIScriptExecutorProgressIndicator();
            }
        });
        //TimingProgressIndicator timeDelegate =  connectorRepository
        //        .getConnectorHint(TARGET, UIScriptExecutorProgressIndicator.class)
        //        .getValue()
        //        .getTimingDelegate();
        new CopySchemaTool(connectorRepository).copySchema(SOURCE, TARGET);
        checkCompatibilityIssues();
        try {
            new DefaultTableCopyTool(connectorRepository).copyTables(SOURCE,
                    TARGET);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), ERROR_TITLE);
        }
        new CheckEqualTableDataTool(connectorRepository).checkTableData(SOURCE,
                TARGET);
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

    @Override
    protected Void doInBackground() {
        int progress = 0;



        Random random = new Random();
        //Initialize progress property.
        setProgress(0);
        while (progress < 100) {
            //Sleep for up to one second.
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException ignore) {}
            //Make random progress.
            progress += random.nextInt(10);
            setProgress(Math.min(progress, 100));
        }
        return null;
    }
}
