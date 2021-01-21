package de.akquinet.jbosscc.gbplugin.ui.gbactions;

import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;

import javax.swing.*;

/**
 * @author siraj
 */
public abstract class View {
    public abstract JPanel getContent();

    public abstract GBAction getAction();
}
