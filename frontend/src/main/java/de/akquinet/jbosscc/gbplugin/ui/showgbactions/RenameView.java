package de.akquinet.jbosscc.gbplugin.ui.showgbactions;

import com.intellij.openapi.ui.ComboBoxWithWidePopup;
import de.akquinet.jbosscc.gbplugin.data.gbactions.RenameGBAction;
import de.akquinet.jbosscc.gbplugin.data.nodes.RenameType;

import javax.swing.*;

public class RenameView extends View {
    private JPanel content;
    private JPanel container;
    private JPanel sourcePanel;
    private JPanel targetPanel;
    private JComboBox comboBox1;
    private JComboBox renameTypeBox;
    private JTextField sourceRegExpField;
    private JTextField targetField;
    private JTextField nameField;

    public RenameView(){
    }

    public RenameView(RenameGBAction gbAction) {
        sourceRegExpField.setText(gbAction.getRegExp());
        targetField.setText(gbAction.getReplace());
        nameField.setText(gbAction.getName());
        renameTypeBox.setSelectedItem(gbAction.getRenameType());
    }

    public JPanel getContent() {
        return content;
    }

    public RenameGBAction getAction() {
        System.out.println(renameTypeBox.getSelectedItem());
        return new RenameGBAction(nameField.getText(), sourceRegExpField.getText(), targetField.getText(), (RenameType) renameTypeBox.getSelectedItem());
    }

    private void createUIComponents() {
        renameTypeBox = new ComboBoxWithWidePopup<>(RenameType.values());
    }
}
