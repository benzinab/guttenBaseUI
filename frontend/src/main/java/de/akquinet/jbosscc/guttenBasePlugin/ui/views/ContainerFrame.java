package de.akquinet.jbosscc.guttenBasePlugin.ui.views;

import de.akquinet.jbosscc.guttenBasePlugin.data.Action;
import de.akquinet.jbosscc.guttenBasePlugin.data.ActionType;
import de.akquinet.jbosscc.guttenBasePlugin.ui.ActionsUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ContainerFrame extends JFrame {

    public ContainerFrame(JPanel content, String title) {
        super();
        setSize(600, 700);
        content.setMaximumSize(content.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setTitle(title);

        this.getContentPane().setLayout(new CardLayout());

        ArrayList<de.akquinet.jbosscc.guttenBasePlugin.data.Action> myActions = new ArrayList<>();
        de.akquinet.jbosscc.guttenBasePlugin.data.Action act1 = new de.akquinet.jbosscc.guttenBasePlugin.data.Action("action1", "RegEx", ActionType.COLUMN_RENAME_ACTION);
        de.akquinet.jbosscc.guttenBasePlugin.data.Action act2 = new de.akquinet.jbosscc.guttenBasePlugin.data.Action("action2", "RegEx", ActionType.COLUMN_RENAME_ACTION);
        de.akquinet.jbosscc.guttenBasePlugin.data.Action act3 = new Action("action3", "RegEx", ActionType.COLUMN_RENAME_ACTION);
        myActions.add(act1);
        myActions.add(act2);
        myActions.add(act3);
        ActionsUI actionsUI = new ActionsUI(myActions);
        this.getContentPane().add(actionsUI.getPanel(), "1");

    }
}
