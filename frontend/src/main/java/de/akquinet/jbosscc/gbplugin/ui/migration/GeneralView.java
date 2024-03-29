package de.akquinet.jbosscc.gbplugin.ui.migration;

import com.intellij.database.model.RawConnectionConfig;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.util.DasUtil;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.DatabaseTypeMatcher;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.gbplugin.ui.migration.overview.OverView;
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

/**
 * @author siraj
 */
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
    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public GeneralView(List<DbDataSource> dataSources, String currentDataSource) {
        this(dataSources);
        //select database from db datasource.
        sourceDatabaseBox.setSelectedItem(currentDataSource);
        selectSchemasAndTypes(currentDataSource, sourceSchema, SOURCE);
    }
    public GeneralView(List<DbDataSource> dataSources){
        this.dataSources = dataSources;
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
        selectSchemasAndTypes(Objects.requireNonNull(sourceDatabaseBox.getSelectedItem()).toString(), sourceSchema, SOURCE);
        selectSchemasAndTypes(Objects.requireNonNull(targetDatabaseBox.getSelectedItem()).toString(), targetSchema, TARGET);
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

    /**
     * Validates, creates a connector repository and switches the current view.
     */
    public void next() {
        if (!isValidInput()) {
            return;
        }
        //check selected databases.
        Optional<DbDataSource> currentDB = dataSources.stream()
                .filter(source -> source.getName().equals(sourceDatabaseBox.getSelectedItem()))
                .findAny();
        Optional<DbDataSource> targetDB = dataSources.stream()
                .filter(source -> source.getName().equals(targetDatabaseBox.getSelectedItem()))
                .findAny();
        if ( currentDB.isEmpty() || targetDB.isEmpty()) {
            Messages.showErrorDialog("Cannot find selected database item(s)!", "Error!");
            return;
        }
        //create connectors
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

        final ConnectorRepository connectorRepository = createRepo(sourceConnectorInfo, targetConnectorInfo);
        goToOverview(connectorRepository);
    }

    /**
     * Validates input fields before connecting the repository.
     * @return {@code true} if the input fields are valid, else {@code false}.
     */
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

    /**
     * Creates a connector repository fro a given source and target connector.
     * @param source The source connector.
     * @param target The target connector.
     * @return The resulting connector repository.
     */
    private ConnectorRepository createRepo(URLConnectorInfoImpl source, URLConnectorInfoImpl target) {
        final ConnectorRepository connectorRepository = new ConnectorRepositoryImpl();
        connectorRepository.addConnectionInfo(SOURCE, source);
        connectorRepository.addConnectionInfo(TARGET, target);
        return connectorRepository;
    }

    /**
     * Switch the current UI to the next panel (overview)
     * @param connectorRepository The connector repository.
     */
    private void goToOverview(ConnectorRepository connectorRepository) {
        Migration migration = new Migration(connectorRepository);
        OverView overView = new OverView(migration);
        JDialog parent = (JDialog) SwingUtilities.getWindowAncestor(this.content);
        Container contentPane = parent.getContentPane();
        contentPane.add(overView.getContent(), "2");
        CardLayout cl = (CardLayout) (contentPane.getLayout());
        cl.show(contentPane, "2");
        SwingUtilities.updateComponentTreeUI(parent);
    }

    /**
     * custom creation of UI components.
     */
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

}
