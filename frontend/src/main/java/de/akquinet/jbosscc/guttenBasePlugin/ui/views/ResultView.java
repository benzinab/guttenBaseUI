package de.akquinet.jbosscc.guttenBasePlugin.ui.views;

import com.intellij.openapi.ui.Messages;
import de.akquinet.jbosscc.guttenBasePlugin.migration.Migration;
import de.akquinet.jbosscc.guttenbase.repository.ConnectorRepository;

import javax.swing.*;
import java.sql.SQLException;

public class ResultView extends AbstractView {

    private JPanel content;
    private JTabbedPane tabbedPane1;
    private JButton cancelButton;
    private JButton submitButton;
    private JTable table1;
    private JButton backButton;

    private final ConnectorRepository connectorRepository;

    public ResultView(ConnectorRepository connectorRepository1) {
        this.connectorRepository = connectorRepository1;
        backButton.addActionListener(e -> backTo(content, "2"));
        cancelButton.addActionListener(e -> close(content));
        submitButton.addActionListener(e -> submit());
        String str = "wtf";
        str.matches("_ewe");
    }

    public void submit() {
        try {
            Migration.migrate(connectorRepository);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Messages.showErrorDialog(throwables.getMessage(), "Error!");
            return;
        }
        close(content);
    }

    public JPanel getContent() {
        return content;
    }
}
