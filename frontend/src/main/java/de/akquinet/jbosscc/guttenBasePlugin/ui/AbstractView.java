package de.akquinet.jbosscc.guttenBasePlugin.ui;

import com.intellij.database.psi.DbDataSource;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AbstractView extends JFrame {

    public AbstractView(JPanel content) {
        super();
        setSize(600, 700);
        content.setMaximumSize(content.getPreferredSize());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
        setTitle("Migrate Database");
        setContentPane(content);
    }
}
