package de.akquinet.jbosscc.guttenBasePlugin.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.util.Comparing;
import com.intellij.ui.*;
import com.intellij.util.ui.ColumnInfo;
import de.akquinet.jbosscc.guttenBasePlugin.data.Action;
import de.akquinet.jbosscc.guttenBasePlugin.data.ActionType;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ActionsUI {
    private final JPanel myRoot;
    private final JLabel myCountLabel;
    private final ActionsTable myActionsTable;
    private final List<AnAction> myAddActions = new ArrayList<>();
    private ToolbarDecorator decorator;
    private List<Action> myActions;

    public ActionsUI(List<Action> actions) {
        myActions = actions;
        myActionsTable = new ActionsTable(actions, createInfoColumns());
        decorator = ToolbarDecorator.createDecorator(myActionsTable);
        createActions(decorator);

        myRoot = new JPanel(new BorderLayout());
        myRoot.add(new TitledSeparator("actions places"), BorderLayout.NORTH);
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        myCountLabel = new JLabel();
        myCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        myCountLabel.setForeground(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES.getFgColor());
        myRoot.add(myCountLabel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return decorator.createPanel();
    }
    private void createActions(ToolbarDecorator decorator) {
        myAddActions.sort((o1, o2) -> Comparing.compare(o1.getTemplatePresentation().getText(), o2.getTemplatePresentation().getText()));
        decorator.disableUpDownActions();
        decorator.setAddActionUpdater(e -> !myAddActions.isEmpty());
        decorator.setAddAction(this::performAdd);
        decorator.setRemoveAction(button -> performRemove());
    }
    private List<Action> getSelectedActions() {
        List<Action> toRemove = new ArrayList<>();
        for (int row : myActionsTable.getSelectedRows()) {
            toRemove.add(myActionsTable.getItems().get(myActionsTable.convertRowIndexToModel(row)));
        }
        return toRemove;
    }

    private void performAdd(AnActionButton e) {
    }

    private void performRemove() {
        final int selectedRow = myActionsTable.getSelectedRow();
        if (selectedRow < 0) return;
        final List<Action> selected = getSelectedActions();
        for (Action action : selected) {
            myActions.remove(action);
        }
        myActionsTable.getListTableModel().setItems(myActions);
        final int index = Math.min(myActionsTable.getListTableModel().getRowCount() - 1, selectedRow);
        myActionsTable.getSelectionModel().setSelectionInterval(index, index);
        TableUtil.scrollSelectionToVisible(myActionsTable);
    }

    public ColumnInfo[] createInfoColumns() {
        final ColumnInfo[] columnInfos = {new ColumnInfo<Action, String>("name") {
            @Override
            public @Nullable String valueOf(Action action) {
                return action.getName();
            }
        }, new ColumnInfo< Action, ActionType>(("column.info.name")) {
            @Override
            public ActionType valueOf(final Action action) {
                return action.getActionType();
            }
        }};
        return columnInfos;
    }


}

