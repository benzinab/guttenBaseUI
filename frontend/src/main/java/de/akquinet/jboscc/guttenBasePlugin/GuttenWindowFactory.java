package de.akquinet.jboscc.guttenBasePlugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class GuttenWindowFactory implements ToolWindowFactory {
    @Override
    public boolean isApplicable(@NotNull Project project) {
        return true;
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        GuttenWindow guttenWindow = new GuttenWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(guttenWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);

    }

    @Override
    public void init(@NotNull ToolWindow toolWindow) {

    }
}
