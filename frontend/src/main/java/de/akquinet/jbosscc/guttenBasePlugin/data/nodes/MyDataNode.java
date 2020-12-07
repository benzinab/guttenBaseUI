package de.akquinet.jbosscc.guttenBasePlugin.data.nodes;

import de.akquinet.jbosscc.guttenBasePlugin.table.Action;

import java.util.ArrayList;
import java.util.List;

public class MyDataNode {

    private final String name;
    private final Integer size;
    private final Action[] actions;
    private final Boolean checked;

    private List<MyDataNode> children;

    public MyDataNode(String name, Integer size, Action[] actions, Boolean checked, List<MyDataNode> children) {
        this.name = name;
        this.size = size;
        this.actions = actions;
        this.checked = checked;
        this.children = children;
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public Action[] getActions() {
        return actions;
    }

    public Boolean isChecked() {
        return checked;
    }

    public List<MyDataNode> getChildren() {
        return children;
    }

    public void addNode(MyDataNode node) {
        children.add(node);
    }

    /**
     * Knotentext vom JTree.
     */
    public String toString() {
        return name;
    }
}