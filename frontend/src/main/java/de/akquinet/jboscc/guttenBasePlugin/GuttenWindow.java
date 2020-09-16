package de.akquinet.jboscc.guttenBasePlugin;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class GuttenWindow {


    private JPanel content;
    private JToolBar toolBar;
    private JButton migrationButton;
    private JButton configurationButton;
    private JButton otherButton;
    private JTextArea sourcePathTextArea1;
    private JTextArea sourcePathTextArea2;
    private JPanel targetPanel;
    private JPanel sourcePanel;
    private JLabel sourceLabel;
    private JLabel targetLabel;
    private JPanel container;
    private JButton testButton;

    public GuttenWindow(ToolWindow toolWindow) {
    }

    public JPanel getContent() {
        return content;
    }

}
