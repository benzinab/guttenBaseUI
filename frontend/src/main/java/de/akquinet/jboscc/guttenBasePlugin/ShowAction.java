package de.akquinet.jboscc.guttenBasePlugin;

import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogPanel;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.panel.ComponentPanelBuilder;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.ui.render.LabelBasedRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ShowAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showErrorDialog("test", "testtitle");

        DbDataSource dbDataSource = DbPsiFacade
                .getInstance(e.getProject())
                .getDataSources()
                .get(0);
        String name = dbDataSource.getName();
        String dbmsName = dbDataSource.getConnectionConfig().getName();
        String dbmsUrl = dbDataSource.getConnectionConfig().getUrl();
        String dbmsDriverClass = dbDataSource.getConnectionConfig().getDriverClass();

        System.out.println(name + dbmsName + dbmsUrl + dbmsDriverClass);

        //LocalDataSource

    }
}
