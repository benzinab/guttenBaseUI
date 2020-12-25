package de.akquinet.jbosscc.gbplugin.ui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class AbstractView {

    public AbstractView() {
    }

    public void close(JPanel content) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(content);
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }

    public void backTo(JPanel currentContent, String targetContent) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(currentContent);
        Container contentPane = parent.getContentPane();
        CardLayout cl = (CardLayout) (contentPane.getLayout());
        cl.show(contentPane, targetContent);
        SwingUtilities.updateComponentTreeUI(parent);
    }
}
