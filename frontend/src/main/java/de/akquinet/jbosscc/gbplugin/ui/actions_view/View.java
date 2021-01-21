package de.akquinet.jbosscc.gbplugin.ui.actions_view;

import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;

import javax.swing.*;

public abstract class View {
    public abstract JPanel getContent();

    public abstract GBAction getAction();
}
