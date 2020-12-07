package de.akquinet.jbosscc.guttenBasePlugin.table;

import de.akquinet.jbosscc.guttenBasePlugin.data.nodes.MyDataNode;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyTreeTableMain extends JFrame {

    public MyTreeTableMain() {

        super("Tree Table Demo");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(0, 1));

        MyAbstractTreeTableModel treeTableModel = new MyDataModel(createDataStructure());

        MyTreeTable myTreeTable = new MyTreeTable(treeTableModel);

        Container cPane = getContentPane();

        cPane.add(new JScrollPane(myTreeTable));

        setSize(1000, 800);
        setLocationRelativeTo(null);
    }


    private static MyDataNode createDataStructure() {
        java.util.List<MyDataNode> children1 = new ArrayList<MyDataNode>();
        children1.add(new MyDataNode( "name1", 50, null, Boolean.FALSE,null));
        children1.add(new MyDataNode( "name2", 50, null, Boolean.TRUE,null));
        children1.add(new MyDataNode( "name3", 50, null, Boolean.TRUE,null));

        java.util.List<MyDataNode> children2 = new ArrayList<MyDataNode>();
        children2.add(new MyDataNode( "name1", 50, null, Boolean.TRUE,children1));

        List<MyDataNode> rootNodes = new ArrayList<MyDataNode>();
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.FALSE,children1));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.TRUE,children1));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.TRUE,children1));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.FALSE,children1));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.TRUE,children2));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.FALSE,children2));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.TRUE,children2));
        rootNodes.add(new MyDataNode( "rootnode1", 50, null, Boolean.TRUE,children2));
        MyDataNode root = new MyDataNode( "root", 50, null, Boolean.FALSE,rootNodes);

        return root;
    }

    public static void main(final String[] args) {
        Runnable gui = new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MyTreeTableMain().setVisible(true);
            }
        };
        SwingUtilities.invokeLater(gui);
    }

}
