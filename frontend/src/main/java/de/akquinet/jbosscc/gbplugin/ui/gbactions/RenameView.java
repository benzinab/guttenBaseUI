package de.akquinet.jbosscc.gbplugin.ui.gbactions;

import de.akquinet.jbosscc.gbplugin.data.Action;
import de.akquinet.jbosscc.gbplugin.data.ActionType;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;

import javax.swing.*;

public class RenameView extends AbstractView {
    private JPanel content;
    private JPanel container;
    private JPanel sourcePanel;
    private JPanel targetPanel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTextField sourceRegExpField;
    private JTextField targetField;
    private JTextField nameField;

    public RenameView(){
    }

    public RenameView(Action action) {
        sourceRegExpField.setText(action.getRegex());
        targetField.setText(action.getReplace());
        nameField.setText(action.getName());
    }

    public JPanel getContent() {
        return content;
    }

    public Action getAction() {
        return new Action(nameField.getText(), sourceRegExpField.getText(), ActionType.COLUMN_RENAME_ACTION);
    }
}
