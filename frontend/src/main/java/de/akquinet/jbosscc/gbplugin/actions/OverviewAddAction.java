package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.DumbAwareActionButton;
import com.intellij.util.PlatformIcons;
import de.akquinet.jbosscc.gbplugin.data.gbactions.GBAction;
import de.akquinet.jbosscc.gbplugin.helper.Migration;
import org.jetbrains.annotations.NotNull;

public class OverviewAddAction extends DumbAwareActionButton {

    private final GBAction gbAction;
    private Migration migration;

    public OverviewAddAction(@NotNull GBAction gbAction, Migration migration) {
        super(gbAction.getName(), "", PlatformIcons.NEW_PARAMETER);
        this.gbAction = gbAction;
        this.migration = migration;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        migration.addGBAction(gbAction);
        System.out.println(migration.getGbActions().size());
    }

    @Override
    public boolean displayTextInToolbar() {
        return true; // show action names
    }
}
