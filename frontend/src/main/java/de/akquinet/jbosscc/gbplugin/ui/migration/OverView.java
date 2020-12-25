package de.akquinet.jbosscc.gbplugin.ui.migration;

import com.intellij.database.psi.DbDataSource;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.nodes.ColumnNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.DatabaseNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.MyDataNode;
import de.akquinet.jbosscc.gbplugin.data.nodes.TableNode;
import de.akquinet.jbosscc.gbplugin.ui.common.table.MyAbstractTreeTableModel;
import de.akquinet.jbosscc.gbplugin.ui.common.table.MyDataModel;
import de.akquinet.jbosscc.gbplugin.ui.common.table.MyTreeTable;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;
import de.akquinet.jbosscc.guttenbase.meta.DatabaseMetaData;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class OverView extends AbstractView {

    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JButton nextButton;
    private JButton cancelButton;
    private JButton backButton;
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
        nextButton.addActionListener(e -> next());
        cancelButton.addActionListener(e -> close(content));
        backButton.addActionListener(e -> backTo(content, "1"));
    }

    public void next() {
            ResultView resultView = new ResultView(connectorRepository);
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this.content);
            Container contentPane = parent.getContentPane();
            contentPane.add(resultView.getContent(), "3");
            CardLayout cl = (CardLayout) (contentPane.getLayout());
            cl.show(contentPane, "3");
            SwingUtilities.updateComponentTreeUI(parent);
    }

    public void fillData() {
        try {
            this.metaData = connectorRepository.getDatabaseMetaData("source");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
            return;
        }
        MyDataNode root;
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
        designTable();
    }

    private void designTable() {
        // set max width.
        myTreeTable1.getColumnModel().getColumn(1).setMaxWidth(90);
        myTreeTable1.getColumnModel().getColumn(2).setMaxWidth(200);
        myTreeTable1.getColumnModel().getColumn(3).setMaxWidth(90);

        //set column horizontal alignment.
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.CENTER);
        myTreeTable1.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        myTreeTable1.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        UIManager.put("Tree.leafIcon", AllIcons.Nodes.DataColumn);

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

    public JPanel getContent() {
        return content;
    }
}
