package de.akquinet.jbosscc.gbplugin.actions.pluginActions;

import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.view.DatabaseView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.ui.ComponentUtil;
import de.akquinet.jbosscc.gbplugin.ui.common.ContainerDialog;
import de.akquinet.jbosscc.gbplugin.ui.migration.GeneralView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

/**
 * AnAction to show the dialog for migration.
 * @author siraj
 */
public class OpenMigrationAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Component component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT);
        DatabaseView databaseView;
        GeneralView generalView;
        try{
            databaseView = ComponentUtil.getParentOfType(DatabaseView.class, component);
            assert databaseView != null;
            Object o = databaseView
                    .getTree()
                    .getSelectionModel()
                    .getSelectionPath()
                    .getLastPathComponent();
            DbDataSource dbDataSource = (DbDataSource) o;
            generalView = new GeneralView(DbPsiFacade.getInstance(Objects.requireNonNull(e.getProject())).getDataSources(), dbDataSource.getName());
        } catch (Exception exception) {
            generalView = new GeneralView(DbPsiFacade.getInstance(Objects.requireNonNull(e.getProject())).getDataSources());
            exception.printStackTrace();
        }
        new ContainerDialog(generalView.getContent(), "Migrate Database");
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

}
