package de.akquinet.jbosscc.gbplugin.ui.showactions;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.helper.GsonHelper;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActionsView extends AbstractView {
    private JPanel content;
    private JButton cancelButton;
    private JButton saveButton;
    private JScrollPane scrollPane1;
    private JPanel actionsPanel;
    private List<GBAction> GBActions;

    public ActionsView() {
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> close(content));
    }

    public void save() {
        try {
            String path = GsonHelper.exportJSON(GBActions, "actions.json");
            Messages.showInfoMessage("Actions are successfully saved in: " + path, "File Saved!");
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "Error While Saving Actions!");
            return;
        }
        close(content);
    }

    private void createUIComponents() {
        try {
            GBActions = GsonHelper.importJSON("actions.json");
        } catch (FileNotFoundException e) {
            GBActions = new ArrayList<>();
        }
        actionsPanel = new GBActionsUI(GBActions).getPanel();
    }

    public JPanel getContent() {
        return content;
    }
}
