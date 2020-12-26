package de.akquinet.jbosscc.gbplugin.ui.showactions;

import de.akquinet.jbosscc.gbplugin.data.GBAction;
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

    public RenameView(GBAction GBAction) {
        sourceRegExpField.setText(GBAction.getRegex());
        targetField.setText(GBAction.getReplace());
        nameField.setText(GBAction.getName());
    }

    public JPanel getContent() {
        return content;
    }

    public GBAction getAction() {
        return new GBAction(nameField.getText(), sourceRegExpField.getText(), ActionType.COLUMN_RENAME_ACTION);
    }
}
