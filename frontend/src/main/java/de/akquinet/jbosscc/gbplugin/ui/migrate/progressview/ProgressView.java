package de.akquinet.jbosscc.gbplugin.ui.migrate.progressview;

import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;

import javax.swing.*;

public class ProgressView  extends AbstractView {
    private JPanel content;
    private JProgressBar totalBar;
    private JTextArea logArea;
    private JButton OKButton;
    private JPanel barContainer;
    private JPanel logPane;
    private JPanel container;
    private JPanel logContainer;
    private JButton backButton;
    private JTabbedPane tabbedPane1;
    private JLabel statementTimeElapsed;
    private JLabel totalTimeElapsed;
    private JLabel status;
    private JTextArea appenderArea;

    public ProgressView(JTextArea appenderArea) {
        this.appenderArea = appenderArea;
        OKButton.addActionListener(e -> close(content));
    }

    public JPanel getContent() {
        return content;
    }

    public JTextArea getTextArea() {
        return logArea;
    }

    private void createUIComponents() {
        logArea = appenderArea;
    }

    public JProgressBar getTotalBar() {
        return totalBar;
    }

    public JTextArea getLogArea() {
        return logArea;
    }

    public JLabel getStatementTimeElapsed() {
        return statementTimeElapsed;
    }

    public JLabel getTotalTimeElapsed() {
        return totalTimeElapsed;
    }

    public JLabel getStatus() {
        return status;
    }
}
