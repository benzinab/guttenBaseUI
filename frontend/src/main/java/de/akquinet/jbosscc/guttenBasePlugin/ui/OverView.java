package de.akquinet.jbosscc.guttenBasePlugin.ui;

import com.intellij.database.psi.DbDataSource;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.guttenBasePlugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.guttenBasePlugin.data.nodes.DatabaseNode;
import de.akquinet.jbosscc.guttenBasePlugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.guttenBasePlugin.data.nodes.TableNode;
import de.akquinet.jbosscc.guttenBasePlugin.migration.Migration;
import de.akquinet.jbosscc.guttenBasePlugin.table.MyAbstractTreeTableModel;
import de.akquinet.jbosscc.guttenBasePlugin.table.MyDataModel;
import de.akquinet.jbosscc.guttenBasePlugin.table.MyTreeTable;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OverView extends AbstractView{

    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JButton submitButton;
    private JButton cancelButton;
    private JButton backButton;
    private JPanel overviewContainer;
    private MyTreeTable myTreeTable1;

    private final List<DbDataSource> dataSources;
    private final ConnectorRepository connectorRepository;
    private DatabaseMetaData metaData;

    public static final String SOURCE = "source";
    public static final String TARGET = "target";

    public OverView(ConnectorRepository connectorRepository, List<DbDataSource> dataSources) {
        this.dataSources = dataSources;
        this.connectorRepository = connectorRepository;

        //action listeners
        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> close(content));
        backButton.addActionListener(e -> back());
    }

    public void submit() {
        try {
            Migration.migrate(connectorRepository);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
            return;
        }
        close(content);
    }

    public void fillData() {
        try {
            this.metaData = connectorRepository.getDatabaseMetaData("source");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
            return;
        }


        MyDataNode root = null;
        try {
            root = new DatabaseNode(metaData);
        } catch (SQLException throwables) {
            Messages.showErrorDialog("Cannot get database name!", "Error!");
            throwables.printStackTrace();
            return;
        }
        MyDataNode finalRoot = root;
        metaData.getTableMetaData().forEach(table -> {
                TableNode tableNode = new TableNode(table);
                table.getColumnMetaData().forEach(column -> tableNode.addNode(new ColumnNode(column)));
                finalRoot.addNode(tableNode);
            });


        MyAbstractTreeTableModel treeTableModel = new MyDataModel(root);
        myTreeTable1 = new MyTreeTable(treeTableModel);
    }

    public void back() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
        Container contentPane = parent.getContentPane();
        CardLayout cl = (CardLayout) (contentPane.getLayout());
        cl.show(contentPane, "1");
        SwingUtilities.updateComponentTreeUI(parent);
    }

    public JPanel getContent() {
        return content;
    }

    private void createUIComponents() {
        fillData();
    }

    class TreePopup extends JPopupMenu {
        public TreePopup(JTree tree) {
            JMenuItem rename = new JMenuItem("Rename");
            rename.addActionListener(ae -> System.out.println("rename child"));
            add(rename);
        }
    }
}
