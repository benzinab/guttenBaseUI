package de.akquinet.jbosscc.gbplugin.ui.showgbactions;

import de.akquinet.jbosscc.gbplugin.data.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.data.nodes.RenameType;
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

    public RenameView(RenameGBAction GBAction) {
        sourceRegExpField.setText(GBAction.getRegExp());
        targetField.setText(GBAction.getReplace());
        nameField.setText(GBAction.getName());
    }

    public JPanel getContent() {
        return content;
    }

    public RenameGBAction getAction() {
        return new RenameGBAction(nameField.getText(), sourceRegExpField.getText(), targetField.getText(), RenameType.ADD_SUFFIX);
    }
}
