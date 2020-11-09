package de.akquinet.jbosscc.guttenBasePlugin.ui;

import com.intellij.database.psi.DbDataSource;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.table.TableView;
import com.intellij.ui.treeStructure.Tree;
import de.akquinet.jbosscc.guttenBasePlugin.WhereClauseInfo;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;

public class GBFrame extends JFrame{
    
    private final List<DbDataSource> dataSources;
    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JPanel generalPanel;
    private JFormattedTextField sourceUserTextField;
    private JPasswordField sourcePasswordField;
    private JComboBox sourceDatabaseBox;
    private JPanel sourcePanel;
    private JFormattedTextField targetUserTextField;
    private JComboBox targetDatabaseBox;
    private JPasswordField targetPasswordField;
    private JPanel targetPanel;
    private JLabel sourceUserLabel;
    private JLabel sourcePasswordLabel;
    private JLabel sourceDatabase;
    private JLabel targetPasswordLabel;
    private JLabel targetUserLabel;
    private JLabel targetDatabaseTypeLabel;
    private JButton submitButton;
    private JButton cancelButton;
    private JComboBox targetSourceType;
    private JComboBox columnSourceType;
    private JSeparator generalPanelSeperator;
    private JTree databaseTree;
    private JButton tablePlusButton;
    private JPanel tableCustomFilter;
    private JButton testButton;

    private TableView<WhereClauseInfo> tableView;

    private int whereClauseCounter = 1;
    private DatabaseType sourceDatabaseType;
    private String currentDataSource;

    public GBFrame(List<DbDataSource> dataSources, String currentDataSource) {
        super();
        this.currentDataSource = currentDataSource;
        this.dataSources = dataSources;
        setSize(600, 700);
        content.setMaximumSize(content.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setTitle("Migrate Database");
        setContentPane(content);
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
        sourceDatabaseBox = new ComboBox<String>();
        dataSources.forEach(dataSource -> sourceDatabaseBox.addItem(dataSource.getName()));
        targetDatabaseBox = new ComboBox<String>();
        dataSources.forEach(dataSource -> targetDatabaseBox.addItem(dataSource.getName()));
        columnSourceType = new ComboBox<String>();
        Arrays.asList(ColumnType.values()).forEach(type -> columnSourceType.addItem(type.toString()));
        targetSourceType = new ComboBox<String>();
        Arrays.asList(ColumnType.values()).forEach(type -> targetSourceType.addItem(type.toString()));
        tableCustomFilter = new JPanel();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Database");
        DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode("table");
        DefaultMutableTreeNode columnNode = new DefaultMutableTreeNode("column");
        tableNode.add(columnNode);
        rootNode.add(tableNode);
        databaseTree = new Tree(rootNode);
        final TreePopup treePopup = new TreePopup(databaseTree);
        databaseTree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    treePopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        ((DefaultTreeModel)databaseTree.getModel()).reload();
    }
    class TreePopup extends JPopupMenu {
        public TreePopup(JTree tree) {
            JMenuItem rename = new JMenuItem("Rename");
            rename.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    System.out.println("rename child");
                }
            });
            add(rename);
        }
    }
}
