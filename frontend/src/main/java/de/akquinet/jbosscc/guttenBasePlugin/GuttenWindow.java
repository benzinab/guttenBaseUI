package de.akquinet.jbosscc.guttenBasePlugin;

import com.intellij.database.psi.DbDataSource;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

public class GuttenWindow  extends JFrame{
    private final List dataSources;
    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JPanel generalPanel;
    private JPanel hintsPanel;
    private JFormattedTextField sourceUserTextField;
    private JPasswordField sourcePasswordField;
    private JComboBox sourceDatabaseBox;
    private JPanel sourcePanel;
    private JFormattedTextField targetUrlTextField;
    private JFormattedTextField targetUserTextField;
    private JComboBox targetDatabaseTypeBox;
    private JPasswordField targetPasswordField;
    private JPanel targetPanel;
    private JLabel sourceUserLabel;
    private JLabel sourcePasswordLabel;
    private JLabel sourceDatabase;
    private JLabel targetUrlPanel;
    private JLabel targetPasswordLabel;
    private JLabel targetUserLabel;
    private JLabel targetDatabaseTypeLabel;
    private JButton submitButton;
    private JButton cancelButton;
    private JTabbedPane tabbedPane2;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JFormattedTextField formattedTextField1;
    private JComboBox comboBox3;
    private JFormattedTextField formattedTextField3;
    private JFormattedTextField formattedTextField4;
    private JTextField textField1;
    private JFormattedTextField formattedTextField2;
    private JFormattedTextField formattedTextField5;
    private JComboBox comboBox4;
    private JFormattedTextField formattedTextField6;
    private JFormattedTextField formattedTextField7;
    private JFormattedTextField formattedTextField8;
    private JFormattedTextField formattedTextField9;
    private JSeparator generalPanelSeperator;
    private JButton testButton;

    private DatabaseType sourceDatabaseType;
    private String currentDataSource;

    public GuttenWindow(List<DbDataSource> dataSources, String currentDataSource) {
        super();
        this.currentDataSource = currentDataSource;
        this.dataSources = dataSources;
        setSize(600, 700);
        content.setMaximumSize(content.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setTitle("Migrate Database");
        setContentPane(content);
        for (DbDataSource dbDataSource : dataSources) {
            sourceDatabaseBox.addItem(dbDataSource.getName());
        }
        if (currentDataSource != null) {
            //select database from dbdatasource.
            sourceDatabaseBox.setSelectedItem(currentDataSource);
        }
        else  {
            cancelButton.setEnabled(false);
        }
        //set all enum values in target database type.
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(sourceUserTextField.getText());
                close();
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
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public JPanel getContent() {
        return content;
    }

    private void createUIComponents() {
        targetDatabaseTypeBox = new ComboBox<String>();
        Arrays.asList(DatabaseType.values()).forEach(type -> targetDatabaseTypeBox.addItem(type.toString()));
    }
}
