package de.akquinet.jbosscc.gbplugin.extensions;

import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.akquinet.jbosscc.gbplugin.ui.migration.GeneralView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GBWindowFactory implements ToolWindowFactory {
    @Override
    public boolean isApplicable(@NotNull Project project) {
        return true;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        DbPsiFacade facade = DbPsiFacade.getInstance(project);
        List<DbDataSource> dataSources = facade.getDataSources();
        GeneralView generalView = new GeneralView(dataSources, null);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        JPanel panel = new JPanel();
        panel.setLayout(new CardLayout());
        panel.add(generalView.getContent(), "1");
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {

    }
}
