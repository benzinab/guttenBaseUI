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

public class ConfigurationView {
    
    private final List<DbDataSource> dataSources;
    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JFormattedTextField sourceUserTextField;
    private JComboBox sourceDatabaseBox;
    private JComboBox targetDatabaseBox;
    private JButton submitButton;
    private JButton cancelButton;
    private JComboBox targetSourceType;
    private JComboBox columnSourceType;
    private JTree databaseTree;
    private JButton backButton;
    private JButton tablePlusButton;
    private JPanel tableCustomFilter;
    private JButton testButton;

    private TableView<WhereClauseInfo> tableView;

    private int whereClauseCounter = 1;
    private DatabaseType sourceDatabaseType;
    private String currentDataSource;

    public ConfigurationView(List<DbDataSource> dataSources, String currentDataSource) {
        this.currentDataSource = currentDataSource;
        this.dataSources = dataSources;
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
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });
    }

    public void close() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }

    public void back() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
        GeneralView generalView = new GeneralView(dataSources, currentDataSource);
        parent.setContentPane(generalView.getContent());
        SwingUtilities.updateComponentTreeUI(parent);
    }

    public JPanel getContent() {
        return content;
    }

    private void createUIComponents() {
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
