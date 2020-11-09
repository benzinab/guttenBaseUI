package de.akquinet.jbosscc.guttenBasePlugin.extensions;

import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbPsiFacade;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import de.akquinet.jbosscc.guttenBasePlugin.ui.ConfigurationView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuttenWindowFactory implements ToolWindowFactory {
    @Override
    public boolean isApplicable(@NotNull Project project) {
        return true;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        DbPsiFacade facade = DbPsiFacade.getInstance(project);
        List<DbDataSource> dataSources = facade.getDataSources();
        ConfigurationView ConfigurationView = new ConfigurationView(dataSources, null);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(ConfigurationView.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {

    }
}
