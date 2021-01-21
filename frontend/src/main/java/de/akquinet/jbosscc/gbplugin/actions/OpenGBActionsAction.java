package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import de.akquinet.jbosscc.gbplugin.ui.common.ContainerDialog;
import de.akquinet.jbosscc.gbplugin.ui.actions_view.GBActionsView;

public class OpenGBActionsAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        GBActionsView GBActionsView = new GBActionsView();
        new ContainerDialog(GBActionsView.getContent(), "Migration Actions");
    }
}
