package de.akquinet.jbosscc.gbplugin.ui.extension;

import javax.swing.*;

/**
 * @author siraj
 */
public class HomeView {
    private JPanel content;
    private JButton button1;
    private JButton button2;

    public HomeView() {
        content = new JPanel();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("HomeView");
        frame.setContentPane(new HomeView().getContent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getContent() {
        return content;
    }
}
