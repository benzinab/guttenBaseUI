package de.akquinet.jbosscc.gbplugin.ui.common;

import javax.swing.*;
import java.awt.*;

public class ContainerFrame extends JFrame {

    public ContainerFrame(JPanel content, String title) {
        super();
        setSize(600, 700);
        content.setMaximumSize(content.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setTitle(title);

        this.getContentPane().setLayout(new CardLayout());
        this.getContentPane().add(content, "1");
    }

}
