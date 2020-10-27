package de.akquinet.jbosscc.guttenBasePlugin;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;

public class Tabs extends JBTabsImpl {
    public Tabs(@NotNull Project project) {
        super(project);
    }
}
