package de.akquinet.jbosscc.gbplugin.ui.migrate;

import com.intellij.database.model.RawConnectionConfig;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.gbplugin.ui.migrate.overview.OverView;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;

public class GeneralView extends AbstractView {
    private JTabbedPane tabbedPane1;
    private JPanel generalPanel;
    private JPanel sourcePanel;
    private JLabel sourceUserLabel;
    private JLabel sourcePasswordLabel;
    private JFormattedTextField sourceUserTextField;
    private JPasswordField sourcePasswordField;
    private JLabel sourceDatabaseLabel;
    private JComboBox sourceDatabaseBox;
    private JSeparator generalPanelSeperator;
    private JPanel targetPanel;
    private JLabel targetPasswordLabel;
    private JLabel targetUserLabel;
    private JLabel targetDatabaseTypeLabel;
    private JFormattedTextField targetUserTextField;
    private JComboBox targetDatabaseBox;
    private JPasswordField targetPasswordField;
    private JButton cancelButton;
    private JButton nextButton;
    private JPanel content;
    private JLabel sourceSchemaLabel;
    private JLabel targetSchemaLabel;
    private JComboBox sourceSchema;
    private JComboBox targetSchema;

    private final List<DbDataSource> dataSources;
    private final String currentDataSource;
    private boolean isDialog;
    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    private final String inputFieldError = "Please fill out all input fields!";

    public GeneralView(List<DbDataSource> dataSources, String currentDataSource) {
        this.currentDataSource = currentDataSource;
        this.dataSources = dataSources;
        if (currentDataSource != null) {
            //select database from dbdatasource.
            sourceDatabaseBox.setSelectedItem(currentDataSource);
            setDialog(true);
        }
        else  {
            cancelButton.setEnabled(false);
            setDialog(false);
        }

        nextButton.addActionListener(e -> {
            System.out.println(sourceUserTextField.getText());
            next();
        });
        cancelButton.addActionListener(e -> close(content));
        sourceDatabaseBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectSchemas(e.getItem().toString(), sourceSchema);
            }
        });
        targetDatabaseBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectSchemas(e.getItem().toString(), targetSchema);
            }
        });
    }

    public void selectSchemas(String item, JComboBox<String> box) {
        box.removeAllItems();
        Optional<DbDataSource> optionalSource = dataSources.stream().filter(dbDataSource -> dbDataSource.getName().equals(item)).findAny();
        optionalSource.ifPresent(source -> DasUtil.getSchemas(source).forEach(schema -> box.addItem(schema.getName())));
    }

    public void next() {
            //check syntax
            if (!isValidInput()) {
                return;
            }
            DbDataSource currentDB = dataSources.stream().filter(source -> source.getName().equals(sourceDatabaseBox.getSelectedItem())).findAny().get();
            RawConnectionConfig sourceConnectionConfig = currentDB.getConnectionConfig();
            DbDataSource targetDB = dataSources.stream().filter(source -> source.getName().equals(targetDatabaseBox.getSelectedItem())).findAny().get();
            RawConnectionConfig targetConnectionConfig = targetDB.getConnectionConfig();
            URLConnectorInfoImpl sourceConnectorInfo = new URLConnectorInfoImpl(sourceConnectionConfig.getUrl(), sourceUserTextField.getText(),
                    new String(sourcePasswordField.getPassword()), sourceConnectionConfig.getDriverClass(), sourceSchema.getSelectedItem().toString(), DatabaseType.POSTGRESQL);
            URLConnectorInfoImpl targetConnectorInfo = new URLConnectorInfoImpl(targetConnectionConfig.getUrl(), targetUserTextField.getText(),
                    new String(targetPasswordField.getPassword()), targetConnectionConfig.getDriverClass(), targetSchema.getSelectedItem().toString(), DatabaseType.MYSQL);

            // create repo
            final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
            connectorRepository.addConnectionInfo(SOURCE, sourceConnectorInfo);
            connectorRepository.addConnectionInfo(TARGET, targetConnectorInfo);

        //go to configuration
        Migration migration = new Migration(connectorRepository);
        OverView overView = new OverView(migration);
        if (isDialog()){
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
            Container contentPane = parent.getContentPane();
            contentPane.add(overView.getContent(), "2");
            CardLayout cl = (CardLayout) (contentPane.getLayout());
            cl.show(contentPane, "2");
            SwingUtilities.updateComponentTreeUI(parent);
        }
    }

    private boolean isValidInput() {
        if (sourceDatabaseBox.getSelectedItem().equals("") || sourceSchema.getSelectedItem().equals("")
                || sourceUserTextField.getText().equals("") || sourcePasswordField.getPassword().length == 0
                || targetDatabaseBox.getSelectedItem().equals("") || targetSchema.getSelectedItem().equals("")
                || targetUserTextField.getText().equals("") || targetPasswordField.getPassword().length == 0) {
            Messages.showErrorDialog(inputFieldError, "Error!");
            return false;
        }
        return true;
    }

    private void createUIComponents() {
        sourceDatabaseBox = new ComboBox<String>();
        targetDatabaseBox = new ComboBox<String>();
        sourceSchema = new ComboBox<String>();
        targetSchema = new ComboBox<String>();

        dataSources.forEach(dataSource -> sourceDatabaseBox.addItem(dataSource.getName()));
        dataSources.forEach(dataSource -> targetDatabaseBox.addItem(dataSource.getName()));
        sourceSchema.setEditable(true);
        targetSchema.setEditable(true);
    }

    public JPanel getContent(){
        return content;
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }
}
