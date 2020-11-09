package de.akquinet.jbosscc.guttenBasePlugin.ui;

import com.intellij.database.psi.DbDataSource;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

public class GeneralView  {
    private JTabbedPane tabbedPane1;
    private JPanel generalPanel;
    private JPanel sourcePanel;
    private JLabel sourceUserLabel;
    private JLabel sourcePasswordLabel;
    private JFormattedTextField sourceUserTextField;
    private JPasswordField sourcePasswordField;
    private JLabel sourceDatabase;
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

    private final List<DbDataSource> dataSources;
    private final String currentDataSource;


    public GeneralView(List<DbDataSource> dataSources, String currentDataSource) {
        this.currentDataSource = currentDataSource;
        this.dataSources = dataSources;
        if (currentDataSource != null) {
            //select database from dbdatasource.
            sourceDatabaseBox.setSelectedItem(currentDataSource);
        }
        else  {
            cancelButton.setEnabled(false);
        }

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(sourceUserTextField.getText());
                next();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }

    public void close() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }

    public void next() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
        ConfigurationView configurationView = new ConfigurationView(dataSources, currentDataSource);
        parent.setContentPane(configurationView.getContent());
        SwingUtilities.updateComponentTreeUI(parent);

    }

    private void createUIComponents() {
        sourceDatabaseBox = new ComboBox<String>();
        dataSources.forEach(dataSource -> sourceDatabaseBox.addItem(dataSource.getName()));
        targetDatabaseBox = new ComboBox<String>();
        dataSources.forEach(dataSource -> targetDatabaseBox.addItem(dataSource.getName()));
    }


    public JPanel getContent(){
        return content;
    }
}
