package de.akquinet.jbosscc.guttenBasePlugin.ui;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class AbstractView {

    public AbstractView() {
    }

    public void close(JPanel content) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(content);
        parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
    }
}
