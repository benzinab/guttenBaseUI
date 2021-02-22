package de.akquinet.jbosscc.gbplugin.ui.migration;

import de.akquinet.jbosscc.gbplugin.ui.common.AbstractView;

import javax.swing.*;

/**
 * @author siraj
 */
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
    private JLabel hint;
    private JTextArea appenderArea;
    private boolean isBackEnabled;

    public ProgressView(JTextArea appenderArea) {
        this.appenderArea = appenderArea;
        OKButton.addActionListener(e -> close(content));
        backButton.addActionListener(e -> backTo(content, "3"));
    }

    public void enableBack() {
        backButton.setEnabled(true);
    }

    public void disableBack() {
        backButton.setEnabled(false);
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

    public JLabel getHint() {
        return hint;
    }

    public void setHint(JLabel hint) {
        this.hint = hint;
    }
}
