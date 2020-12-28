package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import de.akquinet.jbosscc.gbplugin.ui.common.ContainerFrame;
import de.akquinet.jbosscc.gbplugin.ui.showgbactions.GBActionsView;
import org.jetbrains.annotations.NotNull;

public class OpenGBActionsAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GBActionsView GBActionsView = new GBActionsView();
        ContainerFrame frame = new ContainerFrame(GBActionsView.getContent(), "Migration Actions");
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }
}
