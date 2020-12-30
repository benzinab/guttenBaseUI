package de.akquinet.jbosscc.gbplugin.ui.showgbactions;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.gbplugin.data.GBAction;
import de.akquinet.jbosscc.gbplugin.helper.GsonHelper;
import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBActionsView extends AbstractView {
    private JPanel content;
    private JButton cancelButton;
    private JButton saveButton;
    private JScrollPane scrollPane1;
    private JPanel actionsPanel;
    private List<GBAction> gbActions;

    public GBActionsView() {
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> close(content));
    }

    public void save() {
        String path;
        try {
            path = GsonHelper.exportJSON(gbActions, "actions.json");
        } catch (IOException e) {
            e.printStackTrace();
            Messages.showErrorDialog(e.getMessage(), "Error While Saving Actions!");
            return;
        }
        close(content);
        Messages.showInfoMessage("Actions are successfully saved in: " + path, "File Saved!");
    }

    private void createUIComponents() {
        try {
            gbActions = GsonHelper.importJSON("actions.json");

        } catch (FileNotFoundException e) {
            gbActions = new ArrayList<>();
        }
        actionsPanel = new GBActionsUI(gbActions).getPanel();
    }

    public JPanel getContent() {
        return content;
    }
}
