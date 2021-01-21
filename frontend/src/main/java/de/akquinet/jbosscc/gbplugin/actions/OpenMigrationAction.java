package de.akquinet.jbosscc.gbplugin.actions;

import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.view.DatabaseView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.ui.ComponentUtil;
import de.akquinet.jbosscc.gbplugin.ui.common.ContainerDialog;
import de.akquinet.jbosscc.gbplugin.ui.migration_views.GeneralView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class OpenMigrationAction extends AnAction {

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Component component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT);
        DatabaseView databaseView = ComponentUtil.getParentOfType(DatabaseView.class, component);
        assert databaseView != null;
        Object o = databaseView
                .getTree()
                .getSelectionModel()
                .getSelectionPath()
                .getLastPathComponent();
        DbDataSource dbDataSource = (DbDataSource) o;
        DbPsiFacade facade = DbPsiFacade.getInstance(Objects.requireNonNull(e.getProject()));
        List<DbDataSource> dataSources = facade.getDataSources();
        // create connection + create repository
        //URLConnectorInfo connectorInfo = new URLConnectorInfoImpl(dbDataSource.getConnectionConfig().get)
        GeneralView generalView = new GeneralView(dataSources, dbDataSource.getName());
        new ContainerDialog(generalView.getContent(), "Migrate Database");
    }
}
