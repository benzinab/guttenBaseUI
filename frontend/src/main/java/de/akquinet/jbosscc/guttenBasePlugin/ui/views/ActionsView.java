package de.akquinet.jbosscc.guttenBasePlugin.ui.views;

import de.akquinet.jbosscc.guttenBasePlugin.data.Action;
import de.akquinet.jbosscc.guttenBasePlugin.data.ActionType;
import de.akquinet.jbosscc.guttenBasePlugin.ui.ActionsUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ActionsView extends AbstractView{
    private JPanel content;
    private JButton cancelButton;
    private JButton saveButton;
    private JScrollPane scrollPane1;
    private List<Action> actions;

    public ActionsView(List<Action> actions) {
        this.actions = actions;
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> close(content));
    }

    public void save() {
    }

    private void createUIComponents() {
        ArrayList<Action> myActions = new ArrayList<>();
        Action act1 = new Action("action1", "RegEx", ActionType.COLUMN_RENAME_ACTION);
        Action act2 = new Action("action2", "RegEx", ActionType.COLUMN_RENAME_ACTION);
        Action act3 = new Action("action3", "RegEx", ActionType.COLUMN_RENAME_ACTION);
        myActions.add(act1);
        myActions.add(act2);
        myActions.add(act3);
        ActionsUI actionsUI = new ActionsUI(myActions);
        content.add(actionsUI.getPanel());
    }

    public JPanel getContent() {
        return content;
    }
}
