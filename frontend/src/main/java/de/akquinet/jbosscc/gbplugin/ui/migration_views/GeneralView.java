package de.akquinet.jbosscc.gbplugin.ui.migration_views;

import com.intellij.database.model.RawConnectionConfig;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.DatabaseTypeMatcher;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.gbplugin.ui.migration_views.overview.OverView;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.connector.impl.URLConnectorInfoImpl;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;
import de.akquinet.jbosscc.guttenbase.repository.impl.ConnectorRepositoryImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Objects;
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
    private JComboBox<String> sourceDatabaseBox;
    private JSeparator generalPanelSeparator;
    private JPanel targetPanel;
    private JLabel targetPasswordLabel;
    private JLabel targetUserLabel;
    private JLabel targetDatabaseTypeLabel;
    private JFormattedTextField targetUserTextField;
    private JComboBox<String> targetDatabaseBox;
    private JPasswordField targetPasswordField;
    private JButton cancelButton;
    private JButton nextButton;
    private JPanel content;
    private JLabel sourceSchemaLabel;
    private JLabel targetSchemaLabel;
    private JComboBox<String> sourceSchema;
    private JComboBox<String> targetSchema;
    private DatabaseTypeMatcher sourceType;
    private DatabaseTypeMatcher targetType;
    private final List<DbDataSource> dataSources;
    private boolean isDialog;
    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public GeneralView(List<DbDataSource> dataSources, String currentDataSource) {
        this.dataSources = dataSources;
        if (currentDataSource != null) {
            //select database from dbdatasource.
            sourceDatabaseBox.setSelectedItem(currentDataSource);
            selectSchemasAndTypes(currentDataSource, sourceSchema, SOURCE);
            selectSchemasAndTypes(Objects.requireNonNull(targetDatabaseBox.getSelectedItem()).toString(), targetSchema, TARGET);
            setDialog(true);
        }

        nextButton.addActionListener(e -> {
            System.out.println(sourceUserTextField.getText());
            next();
        });
        cancelButton.addActionListener(e -> close(content));
        sourceDatabaseBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectSchemasAndTypes(e.getItem().toString(), sourceSchema, SOURCE);
            }
        });
        targetDatabaseBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectSchemasAndTypes(e.getItem().toString(), targetSchema, TARGET);
            }
        });
    }

    public void selectSchemasAndTypes(String item, JComboBox<String> box, String connectorId) {
        box.removeAllItems();
        Optional<DbDataSource> optionalSource = dataSources.stream().filter(dbDataSource -> dbDataSource.getName().equals(item)).findAny();
        optionalSource.ifPresent(source -> {
            DasUtil.getSchemas(source).forEach(schema -> box.addItem(schema.getName()));
            DatabaseTypeMatcher type = DatabaseTypeMatcher.valueOf(source.getDelegate().getDbms().getName());
            if (connectorId.equals(SOURCE)) {
                sourceType = type;
            } else {
                targetType = type;
            }
        });
    }

    public void next() {
        //check input
        if (!isValidInput()) {
            return;
        }
        Optional<DbDataSource> currentDB = dataSources.stream()
                .filter(source -> source.getName().equals(sourceDatabaseBox.getSelectedItem()))
                .findAny();
        Optional<DbDataSource> targetDB = dataSources.stream()
                .filter(source -> source.getName().equals(targetDatabaseBox.getSelectedItem()))
                .findAny();
        if ( currentDB.isPresent() || targetDB.isPresent()) {
            Messages.showErrorDialog("Cannot find selected database item(s)!", "Error!");
            return;
        }
            RawConnectionConfig sourceConnectionConfig = currentDB.get().getConnectionConfig();
            RawConnectionConfig targetConnectionConfig = targetDB.get().getConnectionConfig();
        assert sourceConnectionConfig != null;
        URLConnectorInfoImpl sourceConnectorInfo = new URLConnectorInfoImpl(sourceConnectionConfig.getUrl(), sourceUserTextField.getText(),
                    new String(sourcePasswordField.getPassword()), sourceConnectionConfig.getDriverClass(), Objects.requireNonNull(sourceSchema.getSelectedItem()).toString(),
                    DatabaseType.valueOf(sourceType.getGbType()));
        assert targetConnectionConfig != null;
        URLConnectorInfoImpl targetConnectorInfo = new URLConnectorInfoImpl(targetConnectionConfig.getUrl(), targetUserTextField.getText(),
                    new String(targetPasswordField.getPassword()), targetConnectionConfig.getDriverClass(), Objects.requireNonNull(targetSchema.getSelectedItem()).toString(),
                    DatabaseType.valueOf(targetType.getGbType()));

            // create repo
            final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
            connectorRepository.addConnectionInfo(SOURCE, sourceConnectorInfo);
            connectorRepository.addConnectionInfo(TARGET, targetConnectorInfo);

        //go to configuration
        Migration migration = new Migration(connectorRepository);
        OverView overView = new OverView(migration);
        if (isDialog()){
            JDialog parent = (JDialog) SwingUtilities.getWindowAncestor(this.content);
            Container contentPane = parent.getContentPane();
            contentPane.add(overView.getContent(), "2");
            CardLayout cl = (CardLayout) (contentPane.getLayout());
            cl.show(contentPane, "2");
            SwingUtilities.updateComponentTreeUI(parent);
        }
    }

    private boolean isValidInput() {
        if (Objects.equals(sourceDatabaseBox.getSelectedItem(), "") || Objects.equals(sourceSchema.getSelectedItem(), "")
                || sourceUserTextField.getText().equals("") || sourcePasswordField.getPassword().length == 0
                || Objects.equals(targetDatabaseBox.getSelectedItem(), "") || Objects.equals(targetSchema.getSelectedItem(), "")
                || targetUserTextField.getText().equals("") || targetPasswordField.getPassword().length == 0) {
            String inputFieldError = "Please fill out all input fields!";
            Messages.showErrorDialog(inputFieldError, "Error!");
            return false;
        }
        return true;
    }

    private void createUIComponents() {
        sourceDatabaseBox = new ComboBox<>();
        targetDatabaseBox = new ComboBox<>();
        sourceSchema = new ComboBox<>();
        targetSchema = new ComboBox<>();

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
