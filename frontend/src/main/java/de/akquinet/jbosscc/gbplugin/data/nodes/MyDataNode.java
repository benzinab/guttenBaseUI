package de.akquinet.jbosscc.gbplugin.data.nodes;

import com.sun.istack.Nullable;
import de.akquinet.jbosscc.gbplugin.ui.common.table.Action;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author siraj
 */
public class MyDataNode implements TreeNode {

    private final String name;
    private final Integer size;
    private final Action[] actions;
    private final Boolean checked;
    private final String type;

    private List<MyDataNode> children;

    public MyDataNode(String name, Integer size, Action[] actions, Boolean checked, String type, List<MyDataNode> children) {
        this.name = name;
        this.size = size;
        this.actions = actions;
        this.checked = checked;
        this.type = type;
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

    public String getType() {
        return type;
    }

    @Override
    public MyDataNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Nullable
    @Override
    public TreeNode getParent() {
        return null;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    @Override
    public Enumeration<? extends TreeNode> children() {
        return (Enumeration<? extends TreeNode>) children;
    }

    public String toString() {
        return name;
    }
}