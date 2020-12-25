package de.akquinet.jbosscc.gbplugin.ui.gbactions;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.Action;
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
    private List<Action> actions;

    public ActionsView() {
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> close(content));
    }

    public void save() {
        try {
            String path = GsonHelper.exportJSON(actions, "actions.json");
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
            actions = GsonHelper.importJSON("actions.json");
        } catch (FileNotFoundException e) {
            actions = new ArrayList<>();
        }
        actionsPanel = new GBActionsUI(actions).getPanel();
    }

    public JPanel getContent() {
        return content;
    }
}
