package de.akquinet.jboscc.guttenBasePlugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class ShowAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showInputDialog("Source:", "The Source Of The Database To Migrate", Messages.getInformationIcon());
    }

    @Override
    public boolean isDumbAware() {
        return false;
    }
}
