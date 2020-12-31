package de.akquinet.jbosscc.gbplugin.ui.showgbactions;

import de.akquinet.jbosscc.gbplugin.data.gbactions.ChangeTypeGBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBActionType;

import javax.swing.*;

public class ChangeTypeView extends View {
    private JTextField actionNameField;
    private JPanel content;
    private JPanel actionNamePanel;
    private JPanel sourceTypePanel;
    private JPanel targetTypePanel;
    private JTextField sourceTypeField;
    private JTextField targetTypeField;

    public ChangeTypeView() {
    }
    public ChangeTypeView(ChangeTypeGBAction changeTypeGBAction) {
        actionNameField.setText(changeTypeGBAction.getName());
        sourceTypeField.setText(changeTypeGBAction.getSourceTypeName());
        targetTypeField.setText(changeTypeGBAction.getTargetTypeName());
    }

    public JPanel getContent() {
        return content;
    }

    @Override
    public GBAction getAction() {
        return new ChangeTypeGBAction(actionNameField.getText(), GBActionType.CHANGE_COLUMN_TYPE,
                sourceTypeField.getText(), targetTypeField.getText());
    }


}
