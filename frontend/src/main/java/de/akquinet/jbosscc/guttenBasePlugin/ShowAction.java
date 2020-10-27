package de.akquinet.jbosscc.guttenBasePlugin;

import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.database.view.DatabaseView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.ui.ComponentUtil;
import de.akquinet.jbosscc.guttenbase.connector.DatabaseType;
import de.akquinet.jbosscc.guttenbase.meta.ColumnType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShowAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Component component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT);
        DatabaseView databaseView = ComponentUtil.getParentOfType(DatabaseView.class, component);
        Object o = databaseView
                .getTree()
                .getSelectionModel()
                .getSelectionPath()
                .getLastPathComponent();
        DbDataSource dbDataSource = (DbDataSource) o;
        DbPsiFacade facade = DbPsiFacade.getInstance(e.getProject());
        List dataSources = facade.getDataSources();
        JFrame frame = new GuttenWindow(dataSources, dbDataSource.getName());
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }
}
